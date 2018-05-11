#include <cassert>
#include <cstring>
#include <exception>
#include <iostream>
#include <memory>
#include <stdexcept>
#include <string>
#include <utility>
#include <vector>

#include <fcntl.h>
#include <ftw.h>
#include <sys/mount.h>
#include <sys/stat.h>
#include <sys/syscall.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include <blkid/blkid.h>
#include <libmount/libmount.h>
#include <libudev.h>

#include <getopt.h>


// /boot (ESP)
// |-efi
// | `-boot
// |   `-bootx64.efi
// |-loader
// | |-loader.conf
// | `-entries
// |   |-<part1-label>.conf
// |   `-<part2-label>.conf
// `-kernel
//   |-<part1-label>
//   | |-loader.conf
//   | |-bzImage
//   | `-dtb
//   `-<part2-label>
//     |-loader.conf
//     |-bzImage
//     `-dtb


// If we use template, we will need a helper class
// (something like MakeScopeExit) and deal with correct
// move constructor. std::function could be used,
// but it's not efficient. We should not bring guaranteed
// copy elision here as class template argument deduction
// is meant to replace make_ functions. Therefore, class
// template argument deduction is the way to go.
template<typename T>
class ScopeExit {
public:
    ScopeExit(T f) : func{ std::move(f) } {}
    ~ScopeExit() { func(); }

    ScopeExit(ScopeExit const&) = delete;
    ScopeExit(ScopeExit&&) = delete;
    ScopeExit& operator=(ScopeExit const&) = delete;
    ScopeExit& operator=(ScopeExit&&) = delete;

private:
    T func;
};


#define CONCAT_HELPER(x,y) x##y
#define CONCAT(x,y) CONCAT_HELPER(x,y)
#define SCOPE_EXIT(func) ScopeExit CONCAT(SCOPE_EXIT_, __LINE__)(func)


#define STRINGIFY_HELPER(x) #x
#define STRINGIFY(x) STRINGIFY_HELPER(x)
#define LOCATION __FILE__ ":" STRINGIFY(__LINE__)


#define THROWMSG(msg)   \
    do {                \
        throw std::runtime_error{ LOCATION + std::string{" - "} + msg };  \
    } while (0)         \
    /**/

#define THROWERRNO(func)        \
    do {                        \
        auto const e = errno;   \
        throw std::runtime_error{ LOCATION +                        \
                                  std::string{" - " func " : "} +   \
                                  std::strerror(e) };               \
    } while (0)                 \
    /**/


void CreateSymlink(std::string const& target, std::string const& linkname) {
    if (symlink(target.c_str(), linkname.c_str()) < 0) {
        THROWERRNO("symlink");
    }
}


bool IsDirectory(std::string const& path) {
    struct stat sb;
    if (stat(path.c_str(), &sb) < 0) {
        THROWERRNO("stat");
    }
    return !!S_ISDIR(sb.st_mode);
}


struct Options {
    std::string BootDirectory = "/boot";
    std::string SymlinksDirectory = "/var/lib/swupdate-helper";
    std::string KernelFileName = "bzImage";
};


std::vector<std::string> ValidateOptions(Options const& opts) {
    std::vector<std::string> errors;

    if (!IsDirectory(opts.BootDirectory)) {
        errors.emplace_back(opts.BootDirectory + " is not a directory");
    }

    // if (!IsDirectory(opts.SymlinksDirectory)) {
    //     errors.emplace_back(opts.SymlinksDirectory + " is not a directory");
    // }

    return errors;
}


std::string GetTagValueFromUdev(std::string const& devname, std::string const& tagname)
{
    udev* udev = udev_new();
    if (!udev) {
        THROWMSG("udev_new");
    }
    SCOPE_EXIT([&]{ udev_unref(udev); });

    auto newdevname = realpath(devname.c_str(), nullptr);
    if (!newdevname) {
        THROWERRNO("realpath");
    }
    SCOPE_EXIT([newdevname]{ free(newdevname); });

    if (std::strncmp(newdevname, "/dev/", std::strlen("/dev/")) == 0) {
        newdevname += std::strlen("/dev/");
    }

    udev_device* dev = udev_device_new_from_subsystem_sysname(udev, "block", newdevname);
    if (!dev) {
        THROWMSG("udev_device_new_from_subsystem_sysname");
    }
    SCOPE_EXIT([&]{ udev_device_unref(dev); });

    return udev_device_get_property_value(dev, tagname.c_str());
}


std::string GetTagValueFromBlkid(std::string const& devpath, std::string const& tagname) {
    blkid_probe pr = blkid_new_probe_from_filename(devpath.c_str());
    if (!pr) {
        THROWMSG("blkid_new_probe_from_filename");
    }
    SCOPE_EXIT([&]{ blkid_free_probe(pr); });

    blkid_probe_enable_superblocks(pr, 1);
    blkid_probe_set_superblocks_flags(pr, BLKID_SUBLKS_LABEL | BLKID_SUBLKS_UUID | BLKID_SUBLKS_TYPE);

    blkid_probe_enable_partitions(pr, 1);
    blkid_probe_set_partitions_flags(pr, BLKID_PARTS_ENTRY_DETAILS | BLKID_PARTS_FORCE_GPT);

    switch (blkid_do_safeprobe(pr)) {
    case 0: {
        char const* data = nullptr;
        if (blkid_probe_lookup_value(pr, tagname.c_str(), &data, nullptr) < 0) {
            THROWMSG("blkid_probe_lookup_value");
        }
        return data;
    }

    case 1:
        return "";

    case -1:
        THROWMSG("blkid_do_safeprobe");

    case -2:
        THROWMSG("ambivalen result detected");

    default:
        THROWMSG("unknown error from blkid_do_safeprobe");
    }
}


std::string GetTagValue(std::string const& devpath, std::string const& tagname, std::string const& utagname) {
    auto value = GetTagValueFromBlkid(devpath, tagname);
    if (value.empty()) {
        value = GetTagValueFromUdev(devpath, utagname);
    }
    return value;
}


std::vector<std::string> GetDevPathList() {
    blkid_cache cache = nullptr;
    if (blkid_get_cache(&cache, nullptr) < 0) {
        THROWMSG("blkid_get_cache");
    }
    SCOPE_EXIT([&]{ blkid_put_cache(cache); });

    if (blkid_probe_all(cache) < 0) {
        THROWMSG("blkid_probe_all");
    }

    std::vector<std::string> paths;
    blkid_dev_iterate iter = blkid_dev_iterate_begin(cache);
    SCOPE_EXIT([&]{ blkid_dev_iterate_end(iter); });

    blkid_dev dev = nullptr;
    while (blkid_dev_next(iter, &dev) == 0) {
        dev = blkid_verify(cache, dev);
        if (!dev) {
            continue;
        }
        auto const devname = blkid_dev_devname(dev);
        assert(devname);
        paths.emplace_back(devname);
    }

    return paths;
}


std::vector<std::string> GetDevPathListByPartType(std::string const& parttype) {
    std::vector<std::string> paths;
    for (auto&& path : GetDevPathList()) {
        auto value = GetTagValue(path, "PART_ENTRY_TYPE", "ID_PART_ENTRY_TYPE");
        if (value.empty()) {
            continue;
        }

        if (parttype == value) {
            paths.emplace_back(std::move(path));
        }
    }
    return paths;
}


std::vector<std::string> GetDevPathListByPartLabel(std::string const& partlabel) {
    std::vector<std::string> paths;
    for (auto&& path : GetDevPathList()) {
        auto value = GetTagValue(path, "PART_ENTRY_NAME", "ID_PART_ENTRY_NAME");
        if (value.empty()) {
            continue;
        }

        if (partlabel == value) {
            paths.emplace_back(std::move(path));
        }
    }
    return paths;
}


std::string GetRootPartDevPath() {
    auto ParseKernelMountinfo = []() -> libmnt_table* {
        libmnt_table* tb = mnt_new_table();
        if (!tb) {
            THROWMSG("mnt_new_table");
        }

        auto parserErrCb = [](libmnt_table* /*tb*/, char const* /*filename*/, int /*line*/) -> int
        {
            return 1;
        };

        if (mnt_table_set_parser_errcb(tb, parserErrCb) < 0) {
            mnt_unref_table(tb);
            THROWMSG("mnt_table_set_parser_errcb");
        }

        // /etc/mtab and /proc/mounts are deprecated
        if (mnt_table_parse_file(tb, "/proc/self/mountinfo")) {
            mnt_unref_table(tb);
            THROWMSG("mnt_table_parse_file");
        }

        return tb;
    };

    libmnt_table* tb = ParseKernelMountinfo();
    SCOPE_EXIT([&]{ mnt_unref_table(tb); });

    libmnt_fs* fs = nullptr;
    if (mnt_table_get_root_fs(tb, &fs) < 0 || !fs) {
        THROWMSG("mnt_table_get_root_fs");
    }

    auto const source = mnt_fs_get_srcpath(fs);
    if (!source) {
        THROWMSG("mnt_fs_get_srcpath");
    }
    return source;
}


std::string GetRootPartLabel() {
    auto value = GetTagValue(GetRootPartDevPath(), "PART_ENTRY_NAME", "ID_PART_ENTRY_NAME");
    if (value.empty()) {
        THROWMSG("cannot get root partition label");
    }
    return value;
}


std::string GetSiblingPartLabel() {
    auto const rootPartLabel = GetRootPartLabel();
    auto siblingPartLabel = rootPartLabel;

    auto const lastIdx = rootPartLabel.size() - 1;
    if (rootPartLabel[lastIdx] == '1') {
        siblingPartLabel[lastIdx] = '2';
    } else if (rootPartLabel[lastIdx] == '2') {
        siblingPartLabel[lastIdx] = '1';
    } else {
        THROWMSG("invalid root partition label");
    }
    return siblingPartLabel;
}


std::string GetSiblingPartDevPath(std::string const& partlabel) {
    std::vector<std::string> paths = GetDevPathListByPartLabel(partlabel);
    if (paths.empty()) {
        THROWMSG("no partition has label [" + partlabel + "]");
    }

    if (paths.size() > 1) {
        THROWMSG("more than 1 partition have label [" + partlabel + "]");
    }

    return std::move(*paths.begin());
}


void MountBoot(std::string const& bootdir) {
    auto GetEspPartDevPath = []() -> std::string {
        auto const guidUpper = "C12A7328-F81F-11D2-BA4B-00A0C93EC93B";
        auto const guidLower = "c12a7328-f81f-11d2-ba4b-00a0c93ec93b";
        auto devpathListLower = GetDevPathListByPartType(guidLower);
        auto devpathListUpper = GetDevPathListByPartType(guidUpper);

        if (devpathListLower.empty() && devpathListUpper.empty()) {
            THROWMSG("no EFI system partition");
        } else if (devpathListLower.size() > 1 || devpathListUpper.size() > 1) {
            THROWMSG("more than one EFI system partitions");
        } else if (devpathListLower.size() == 1 && devpathListUpper.size() == 1) {
            if (*devpathListLower.begin() != *devpathListUpper.begin()) {
                THROWMSG("upper GUID and lower GUID give different dev path");
            }
            return std::move(*devpathListLower.begin());
        } else {
            if (devpathListLower.size()) {
                return std::move(*devpathListLower.begin());
            }
            if (devpathListUpper.size()) {
                return std::move(*devpathListUpper.begin());
            }

            THROWMSG("unreachable");
        }
    };

    auto const espdevpath = GetEspPartDevPath();
    if (mount(espdevpath.c_str(), bootdir.c_str(), "vfat", 0, nullptr) < 0) {
        THROWERRNO("mount");
    }
}


void UmountBoot(std::string const& bootdir) {
    umount(bootdir.c_str());
}


std::string GetSiblingKernelPath(std::string const& bootdir, std::string const& partlabel,
                                 std::string const& kernelfilename)
{
    /* /boot/kernel/<part-label>/bzImage */
    return bootdir + "/kernel/" + partlabel + "/" + kernelfilename;
};


std::string GetSiblingBootConfSymlinkPath(std::string const& symlinksdir) {
    /* /var/lib/swupdate-helper/sibling-bootconf */
    return symlinksdir + "/sibling-bootconf";
}


std::string GetCurrentBootConfSymlinkPath(std::string const& symlinksdir) {
    /* /var/lib/swupdate-helper/current-bootconf */
    return symlinksdir + "/current-bootconf";
}


std::string GetSiblingRootDevSymlinkPath(std::string const& symlinksdir) {
    /* /var/lib/swupdate-helper/sibling-rootdev */
    return symlinksdir + "/sibling-rootdev";
}


std::string GetSiblingKernelSymlinkPath(std::string const& symlinksdir) {
    /* /var/lib/swupdate-helper/sibling-kernel */
    return symlinksdir + "/sibling-kernel";
}


void CreateSymlinksDirectory(std::string const& symlinksdir) {
    if (mkdir(symlinksdir.c_str(), 0755) < 0 && errno != EEXIST) {
        THROWERRNO("mkdir");
    }
}


void RemoveSymlinksDirectory(std::string const& symlinksdir) {
    remove(GetSiblingRootDevSymlinkPath(symlinksdir).c_str());
    remove(GetSiblingKernelSymlinkPath(symlinksdir).c_str());
    remove(GetSiblingBootConfSymlinkPath(symlinksdir).c_str());
    remove(GetCurrentBootConfSymlinkPath(symlinksdir).c_str());
}


std::string SystemdBootGetSiblingBootConfPath(std::string const& bootdir, std::string const& partlabel) {
    /* /boot/kernel/<part-label>/loader.conf */
    return bootdir + "/kernel/" + partlabel + "/loader.conf";
};


std::string SystemdBootGetCurrentBootConfPath(std::string const& bootdir) {
    /* /boot/loader/loader.conf */
    return bootdir + "/loader/loader.conf";
};


void SetupLinks(Options const& opts) {
    UmountBoot(opts.BootDirectory);
    MountBoot(opts.BootDirectory);
    RemoveSymlinksDirectory(opts.SymlinksDirectory);
    CreateSymlinksDirectory(opts.SymlinksDirectory);

    auto const partlabel = GetSiblingPartLabel();

    //
    // /var/lib/swupdate-helper/sibling-rootdev   -> /dev/...
    // /var/lib/swupdate-helper/sibling-kernel    -> /boot/kernel/<partX-label>/bzImage
    // /var/lib/swupdate-helper/sibling-bootconf  -> /boot/kernel/<partX-label>/loader.conf
    // /var/lib/swupdate-helper/current-bootconf  -> /boot/loader/loader.conf
    //

    // For upgrading rootfs
    CreateSymlink(GetSiblingPartDevPath(partlabel),
                  GetSiblingRootDevSymlinkPath(opts.SymlinksDirectory));

    // For upgrading kernel
    CreateSymlink(GetSiblingKernelPath(opts.BootDirectory, partlabel, opts.KernelFileName),
                  GetSiblingKernelSymlinkPath(opts.SymlinksDirectory));

    // For upgrading loader.conf
    CreateSymlink(SystemdBootGetSiblingBootConfPath(opts.BootDirectory, partlabel),
                  GetSiblingBootConfSymlinkPath(opts.SymlinksDirectory));
    CreateSymlink(SystemdBootGetCurrentBootConfPath(opts.BootDirectory),
                  GetCurrentBootConfSymlinkPath(opts.SymlinksDirectory));
}


void Usage(char const* progname);


int main(int argc, char* argv[]) {
    try {
        // Make sure that getopt_long doesn't print out error message
        // on unrecognized options.
        opterr = 0;

        Options options;

        option const longopts[] = {
            {"help",                no_argument,        nullptr, 'h'},
            {"boot-manager",        required_argument,  nullptr, 'u'},
            {"bootdir",             required_argument,  nullptr, 'b'},
            {"symlinksdir",         required_argument,  nullptr, 's'},
            {"kernel-file-name",    required_argument,  nullptr, 'k'},
            {nullptr, 0, nullptr, 0}
        };

        while (true) {
            auto const c = getopt_long(argc, argv, "ha:Ru:Mb:Cs:k:i:", longopts, nullptr);
            if (c == -1) break;

            switch (c) {
            case 'h':
                Usage(*argv);
                return EXIT_SUCCESS;

            case 'u': {
                if (strcmp(optarg, "systemd-boot") != 0) {
                    std::cerr << "unknown argument for --boot-manager" << std::endl;
                    return EXIT_FAILURE;
                }
                break;
            }

            case 'b':
                options.BootDirectory = optarg;
                break;

            case 's':
                options.SymlinksDirectory = optarg;
                break;

            case 'k':
                options.KernelFileName = optarg;
                break;

            case ':':
                std::cerr << "missing argument for ";
                if (optopt) {
                    std::cerr << char(optopt);
                } else {
                    std::cerr << argv[optind - 1];
                }
                std::cerr << std::endl;
                return EXIT_FAILURE;

            case '?':
                std::cerr << "ignored unknown option: ";
                if (optopt) {
                    std::cerr << char(optopt);
                } else {
                    std::cerr << argv[optind - 1];
                }
                std::cerr << std::endl;
                break;

            default:
                THROWMSG("unreachable");
            }
        }

        auto const errors = ValidateOptions(options);
        if (!errors.empty()) {
            for (auto const& e : errors) {
                std::cerr << e << std::endl;
            }
            return EXIT_FAILURE;
        }

        SetupLinks(options);

        return EXIT_SUCCESS;
    } catch (std::exception const& e) {
        std::cerr << e.what() << std::endl;
        return EXIT_FAILURE;
    }
}


void Usage(char const* progname) {
    assert(progname);

    std::cout << progname << " <options>\n\n"
                 "Options\n"
                 " -h, --help                 display help\n"
                 " -u, --boot-manager         boot manager\n"
                 " -b, --bootdir              directory for mouting ESP\n"
                 " -k, --kernel-file-name     kernel file name\n"
              << std::endl;
}
