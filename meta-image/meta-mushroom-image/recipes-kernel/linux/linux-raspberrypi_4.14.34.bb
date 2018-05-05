#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
DESCRIPTION = "Mainline Linux kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit kernel
require recipes-kernel/linux/linux-yocto.inc


#======================================#
#                SOURCE                #
#--------------------------------------#
S = "${WORKDIR}/linux-raspberrypi-kernel_1.20180417-1"

SRC_URI = "https://github.com/raspberrypi/linux/archive/raspberrypi-kernel_1.20180417-1.tar.gz;name=source \
           "
# SRCREV = "f70eae405b5d75f7c41ea300b9f790656f99a203"

SRC_URI[source.md5sum] = "b7ac4c4f84a76a8834bea2021799be6f"
SRC_URI[source.sha256sum] = "7808fe8fe260f6a8b1781cbd5585d739d900af57b6784aa04af3425d3632b03f"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
# Pull in the devicetree files into the rootfs
RDEPENDS_kernel-base_append = " kernel-devicetree "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
COMPATIBLE_MACHINE = "raspberrypi3"

KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG_raspberrypi3 = "bcm2709_defconfig"


#======================================#
#                TASKS                 #
#--------------------------------------#


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
