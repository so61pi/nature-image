#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "A small image just capable of allowing a device to boot."
LICENSE = "MIT"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit image


#======================================#
#                SOURCE                #
#--------------------------------------#


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
# Do not put bzImage in the final image (look in kernel.bbclass).
# (Note: This is already set in qemu.inc)
RDEPENDS_kernel-base = ""


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
IMAGE_LINGUAS = ""
IMAGE_FSTYPES = " squashfs wic.vdi "

IMAGE_INSTALL        = " packagegroup-base-simple base-files dash glibc-utils coreutils e2fsprogs dosfstools dbus less "
IMAGE_INSTALL_append = " systemd systemd-zsh-completion systemd-extra-files  "
IMAGE_INSTALL_append = " zsh zsh-startup zsh-completion "
IMAGE_INSTALL_append = " util-linux util-linux-mount util-linux-agetty util-linux-uuidd "
IMAGE_INSTALL_append = " procps procps-watch procps-pidof procps-free procps-kill procps-uptime procps-sysctl "
IMAGE_INSTALL_append = " swupdate swupdate-tools firmware-upgrade "
IMAGE_INSTALL_append = " ncurses-tools ncurses-terminfo ncurses-terminfo-base "

IMAGE_INSTALL_append = " libpam                                                             \
                         pam-plugin-unix pam-plugin-deny pam-plugin-permit pam-plugin-warn  \
                         "

IMAGE_INSTALL_append = " root-default-passwd tmpfs-home tmpfs-var symlink-tmp tmpfs-root default-locale "
IMAGE_INSTALL_append = " cetacean-extra-files "

IMAGE_INSTALL_append = " dropbear iputils vim-tiny tzdata   \
                         less grep findutils tree net-tools \
                         "

IMAGE_INSTALL_append = " mount-data "

# This is added to wic command (which is invoked in image_types_wic.bbclass)
# If we don't have this, the wic command will add an entry to etc/fstab, and
# can lead to unbootable system.
#
# Please look into image_types_wic.bbclass and direct.py::_write_fstab
WIC_CREATE_EXTRA_ARGS = " --no-fstab-update "

WKS_FILE = "cetacean-image.wks"


#======================================#
#                TASKS                 #
#--------------------------------------#
clean_rootfs() {
    rm -rf ${IMAGE_ROOTFS}/var
    mkdir ${IMAGE_ROOTFS}/var

    rm -rf ${IMAGE_ROOTFS}/run
    mkdir ${IMAGE_ROOTFS}/run

    # entry L+ /etc/mtab - - - - ../proc/self/mounts makes
    # the systemd-tmpfiles-setup.service failed
    # The other entries are already in the rootfs
    rm ${IMAGE_ROOTFS}/usr/lib/tmpfiles.d/etc.conf
}

python check_rootfs() {
    import rootfscheck

    checker = rootfscheck.BasicRootfsChecker(rootdir = d.getVar("IMAGE_ROOTFS"),
                                             skip_pattern_paths = rootfscheck.default_skip_pattern_paths,
                                             must_exist_paths = rootfscheck.default_must_exist_paths,
                                             must_empty_dirs = rootfscheck.default_must_empty_dirs,
                                             allowed_empty_dirs = { "/srv", "/mnt", "/root", "/data", "/home", "/var", "/boot" },
                                             allowed_empty_files = { "/etc/machine-id", "/etc/fstab" },
                                             allowed_dangling_symlinks = { "/etc/hostname", "/etc/mtab", "/tmp" })
    rootfscheck.check(checker)
    checker.print_result()
}

IMAGE_PREPROCESS_COMMAND_append = " clean_rootfs; check_rootfs; "


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#


#======================================#
#                 TEST                 #
#--------------------------------------#
