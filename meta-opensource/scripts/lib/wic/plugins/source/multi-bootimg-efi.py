import logging
import os
import shutil

import distutils
from distutils import util

from wic import WicError
from wic.engine import get_custom_config
from wic.pluginbase import SourcePlugin
from wic.misc import (exec_cmd, exec_native_cmd, get_bitbake_var, BOOTDD_EXTRA_SPACE)

logger = logging.getLogger("wic")

# /boot (ESP)
# |-efi
# | `-boot
# |   `-bootx64.efi
# |-loader
# | |-loader.conf
# | `-entries
# |   |-<part1-label>.conf
# |   `-<part2-label>.conf
# `-kernel
#   |-<part1-label>
#   | |-loader.conf
#   | |-bzImage
#   | `-dtb
#   `-<part2-label>
#     |-loader.conf
#     |-bzImage
#     `-dtb

# The way --sourceparams splitted here is same as partition.py::prepare
# Although the comments there are probably wrong because valueless syntax
# is not working
#
# Split sourceparams string of the form key1=val1[,key2=val2,...]
# into a dict. Don't accepts valueless keys i.e. without =
def splitsrcparams(sourceparams):
    srcparams = {}
    if sourceparams:
        splitted = sourceparams.split(",")
        srcparams = dict(pair.split("=") for pair in splitted if pair)
    return srcparams


def isrootfs(srcparams):
    return "rootfs" in srcparams and distutils.util.strtobool(srcparams["rootfs"])


# sourceparams
#   rootfs=(true|false)
#   boot-title=<string>
class MultiBootimgEFIPlugin(SourcePlugin):
    """
    Create EFI boot partition.
    """

    name = "multi-bootimg-efi"

    @classmethod
    def do_configure_systemdboot(cls, hdddir, creator, cr_workdir, source_params):
        """
        Create loader-specific systemd-boot config
        """
        if source_params.get("initrd"):
            logger.debug("Ignoring initrd")

        install_cmd = "install -d {}/loader".format(hdddir)
        exec_cmd(install_cmd)

        install_cmd = "install -d {}/loader/entries".format(hdddir)
        exec_cmd(install_cmd)

        bootloader = creator.ks.bootloader

        copied = False
        for p in creator.ks.partitions:
            srcparams = splitsrcparams(p.sourceparams)
            if not isrootfs(srcparams):
                continue

            def write_entry_conf(conf, cfgpath):
                logger.debug("Writing systemd-boot entry config")
                cfg = open(cfgpath, "w")
                cfg.write(conf)
                cfg.close()

            def write_part_loader_conf(conf, cfgpath):
                logger.debug("Writing systemd-boot partition loader.conf")
                cfg = open(cfgpath, "w")
                cfg.write(loaderconf)
                cfg.close()

            if not p.label:
                raise WicError("Missing --label for rootfs")

            kernel = "/kernel/{}/bzImage".format(p.label)
            title = srcparams["boot-title"] if "boot-title" in srcparams else p.label

            bootconf = ""
            bootconf += "title {}\n".format(title)
            bootconf += "linux {}\n".format(kernel)
            bootconf += "options LABEL={} root=PARTUUID={} {}\n".format(p.label, p.uuid, bootloader.append)
            write_entry_conf(bootconf, "{}/hdd/boot/loader/entries/{}.conf".format(cr_workdir, p.label))

            install_cmd = "install -d {}/hdd/boot/kernel/{}".format(cr_workdir, p.label)
            exec_cmd(install_cmd)
            loaderconf = ""
            loaderconf += "default {}\n".format(p.label)
            loaderconf += "timeout {}\n".format(bootloader.timeout)
            confpath = "{}/hdd/boot/kernel/{}/loader.conf".format(cr_workdir, p.label)
            write_part_loader_conf(loaderconf, confpath)

            if not copied:
                shutil.copyfile(confpath, "{}/hdd/boot/loader/loader.conf".format(cr_workdir))
                copied = True

        assert copied, "copied must be True here"

    @classmethod
    def do_configure_partition(cls, part, source_params, creator, cr_workdir,
                               oe_builddir, bootimg_dir, kernel_dir,
                               native_sysroot):
        """
        Called before do_prepare_partition(), creates loader-specific config
        """
        if "loader" not in source_params:
            raise WicError("multi-bootimg-efi requires a loader, none specified")

        hdddir = "{}/hdd/boot".format(cr_workdir)

        install_cmd = "install -d {}/EFI/BOOT".format(hdddir)
        exec_cmd(install_cmd)

        if source_params["loader"] == "systemd-boot":
            cls.do_configure_systemdboot(hdddir, creator, cr_workdir, source_params)
        else:
            raise WicError("Unrecognized multi-bootimg-efi loader: {}".format(source_params["loader"]))


    @classmethod
    def do_prepare_partition(cls, part, source_params, creator, cr_workdir,
                             oe_builddir, bootimg_dir, kernel_dir,
                             rootfs_dir, native_sysroot):
        """
        Called to do the actual content population for a partition i.e. it
        'prepares' the partition to be incorporated into the image.
        In this case, prepare content for an EFI (grub) boot partition.
        """
        if not kernel_dir:
            kernel_dir = get_bitbake_var("DEPLOY_DIR_IMAGE")
            if not kernel_dir:
                raise WicError("Could not find DEPLOY_DIR_IMAGE")

        if "loader" not in source_params:
            raise WicError("multi-bootimg-efi requires a loader, none specified")

        staging_kernel_dir = kernel_dir

        hdddir = "{}/hdd/boot".format(cr_workdir)

        # Initially, install same kernel for all boot entries
        #
        # Do not change 'p' below to part unless you want to have name clashing
        # and you love headaches
        for p in creator.ks.partitions:
            srcparams = splitsrcparams(p.sourceparams)
            if not isrootfs(srcparams):
                continue

            kerneldir = "{}/kernel/{}".format(hdddir, p.label)
            install_cmd = "install -d {}".format(kerneldir)
            exec_cmd(install_cmd)

            install_cmd = "install -m 0644 {}/bzImage {}/bzImage".format(staging_kernel_dir, kerneldir)
            exec_cmd(install_cmd)

        if source_params["loader"] == "systemd-boot":
            for mod in [e for e in os.listdir(kernel_dir) if e.startswith("systemd-")]:
                cp_cmd = "cp {}/{} {}/EFI/BOOT/{}".format(kernel_dir, mod, hdddir, mod[8:])
                exec_cmd(cp_cmd, True)
        else:
            raise WicError("Unrecognized multi-bootimg-efi loader: {}".format(source_params["loader"]))

        startup = os.path.join(kernel_dir, "startup.nsh")
        if os.path.exists(startup):
            cp_cmd = "cp {} {}/".format(startup, hdddir)
            exec_cmd(cp_cmd, True)

        du_cmd = "du -bks {}".format(hdddir)
        out = exec_cmd(du_cmd)
        blocks = int(out.split()[0])

        extra_blocks = part.get_extra_block_count(blocks)

        if extra_blocks < BOOTDD_EXTRA_SPACE:
            extra_blocks = BOOTDD_EXTRA_SPACE

        blocks += extra_blocks

        logger.debug("Added {} extra blocks to {} to get to {} total blocks".format(extra_blocks, part.mountpoint, blocks))

        # dosfs image, created by mkdosfs
        bootimg = "{}/boot.img".format(cr_workdir)

        dosfs_cmd = "mkdosfs -n efi -C {} {}".format(bootimg, blocks)
        exec_native_cmd(dosfs_cmd, native_sysroot)

        mcopy_cmd = "mcopy -i {} -s {}/* ::/".format(bootimg, hdddir)
        exec_native_cmd(mcopy_cmd, native_sysroot)

        chmod_cmd = "chmod 644 {}".format(bootimg)
        exec_cmd(chmod_cmd)

        du_cmd = "du -Lbks {}".format(bootimg)
        out = exec_cmd(du_cmd)
        bootimg_size = out.split()[0]

        part.size = int(bootimg_size)
        part.source_file = bootimg
