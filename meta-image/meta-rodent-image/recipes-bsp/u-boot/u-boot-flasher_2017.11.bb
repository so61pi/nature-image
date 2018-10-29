#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "uEnv.txt for rodent flasher"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit deploy


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = " file://uEnv.txt "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Copied from u-boot.inc
UBOOT_ENV = "uEnv"
UBOOT_ENV_SUFFIX ?= "txt"
UBOOT_ENV_BINARY ?= "${UBOOT_ENV}.${UBOOT_ENV_SUFFIX}"
UBOOT_ENV_IMAGE ?= "${UBOOT_ENV}-${MACHINE}-${PV}-${PR}.${UBOOT_ENV_SUFFIX}"
UBOOT_ENV_SYMLINK ?= "${UBOOT_ENV}-${MACHINE}.${UBOOT_ENV_SUFFIX}"

DEPLOY_DIR_IMAGE_append = "/flasher"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
deltask do_populate_sysroot
do_package[noexec] = "1"
deltask do_package_qa
do_packagedata[noexec] = "1"
deltask do_package_write_ipk
deltask do_package_write_deb
deltask do_package_write_rpm


do_deploy () {
    install -m 644 ${WORKDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}/${UBOOT_ENV_IMAGE}
    rm -f ${DEPLOYDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}/${UBOOT_ENV_SYMLINK}
    ln -sf ${UBOOT_ENV_IMAGE} ${DEPLOYDIR}/${UBOOT_ENV_BINARY}
    ln -sf ${UBOOT_ENV_IMAGE} ${DEPLOYDIR}/${UBOOT_ENV_SYMLINK}
}

addtask deploy before do_build


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
