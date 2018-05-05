#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "U-boot boot scripts for Raspberry Pi"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit deploy nopackages


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = " file://boot.cmd.in "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "u-boot-mkimage-native"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
INHIBIT_DEFAULT_DEPS = "1"
COMPATIBLE_MACHINE = "raspberrypi3"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile() {
    sed -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@KERNEL_BOOTCMD@@/${KERNEL_BOOTCMD}/' \
        "${WORKDIR}/boot.cmd.in" > "${WORKDIR}/boot.cmd"
    mkimage -A arm -T script -C none -n "Boot script" -d "${WORKDIR}/boot.cmd" boot.scr
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 boot.scr ${DEPLOYDIR}
}

addtask do_deploy after do_compile before do_build


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
