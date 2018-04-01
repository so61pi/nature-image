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
do_rootfs[depends] += " rodent-firmware:do_build u-boot-flasher:do_build "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
IMAGE_LINGUAS = ""
IMAGE_FSTYPES = " wic "

IMAGE_INSTALL        = " packagegroup-base-simple base-files dash glibc-utils coreutils e2fsprogs dosfstools dbus less "
IMAGE_INSTALL_append = " systemd systemd-zsh-completion systemd-extra-files  "
IMAGE_INSTALL_append = " zsh zsh-startup zsh-completion "
IMAGE_INSTALL_append = " util-linux util-linux-mount util-linux-agetty util-linux-uuidd "
IMAGE_INSTALL_append = " procps procps-watch procps-pidof procps-free procps-kill procps-uptime procps-sysctl "
IMAGE_INSTALL_append = " swupdate swupdate-tools "
IMAGE_INSTALL_append = " ncurses-tools ncurses-terminfo ncurses-terminfo-base "

IMAGE_INSTALL_append = " libpam                                                             \
                         pam-plugin-unix pam-plugin-deny pam-plugin-permit pam-plugin-warn  \
                         "

IMAGE_INSTALL_append = " root-default-passwd tmpfs-home tmpfs-var symlink-tmp tmpfs-root default-locale "
IMAGE_INSTALL_append = " rodent-extra-files initflash "

IMAGE_INSTALL_append = " dropbear iputils vim-tiny tzdata   \
                         less grep findutils tree net-tools \
                         "

IMAGE_INSTALL_append = " e2fsprogs-mke2fs "

# deploy directory for this image
DEPLOY_DIR_IMAGE_append = "/flasher"
IMAGE_BOOT_FILES_append = " uEnv.txt "

# This is added to wic command (which is invoked in image_types_wic.bbclass)
# If we don't have this, the wic command will add an entry to etc/fstab, and
# can lead to unbootable system.
#
# Please look into image_types_wic.bbclass and direct.py::_write_fstab
WIC_CREATE_EXTRA_ARGS = " --no-fstab-update "

WKS_FILE = "rodent-flasher-image.wks"


#======================================#
#                TASKS                 #
#--------------------------------------#
copy_initflash_files() {
    if [ ! -d ${IMAGE_ROOTFS}/initflash ]; then
        mkdir ${IMAGE_ROOTFS}/initflash
    fi

    # Both rodent-image and rodent-flasher-image use the same u-boot.
    cp -f ${DEPLOY_DIR_IMAGE}/../MLO        ${DEPLOY_DIR_IMAGE}/
    cp -f ${DEPLOY_DIR_IMAGE}/../u-boot.img ${DEPLOY_DIR_IMAGE}/

    cp -f ${DEPLOY_DIR_IMAGE}/../MLO                            ${IMAGE_ROOTFS}/initflash/
    cp -f ${DEPLOY_DIR_IMAGE}/../u-boot.img                     ${IMAGE_ROOTFS}/initflash/
    cp -f ${DEPLOY_DIR_IMAGE}/../rodent-firmware-beaglebone.swu ${IMAGE_ROOTFS}/initflash/
}

IMAGE_PREPROCESS_COMMAND_append = " copy_initflash_files; "


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
                                             must_exist_paths = rootfscheck.default_must_exist_paths.union({"/initflash"}),
                                             must_empty_dirs = rootfscheck.default_must_empty_dirs,
                                             allowed_empty_dirs = { "/srv", "/mnt", "/root", "/data", "/home", "/var" },
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
