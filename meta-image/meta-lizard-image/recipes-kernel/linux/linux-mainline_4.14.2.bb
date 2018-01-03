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
require recipes-kernel/linux/linux.inc


#======================================#
#                SOURCE                #
#--------------------------------------#
S = "${WORKDIR}/linux-${PV}"

SRC_URI = "https://www.kernel.org/pub/linux/kernel/v4.x/linux-${PV}.tar.xz;name=source \
           file://defconfig                                                            \
           "

SRC_URI[source.md5sum] = "0164a000bd7b302037de4e91dff3018b"
SRC_URI[source.sha256sum] = "e92690620a4e4811c6b37b2f1b6c9b32a1dde40aa12be6527c8dc215fb27464c"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
# Pull in the devicetree files into the rootfs
RDEPENDS_kernel-base_append = " kernel-devicetree "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


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
