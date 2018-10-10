#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Utilities for managing LZMA compressed files"
HOMEPAGE = "http://tukaani.org/xz/"

# The source includes bits of PD, GPLv2, GPLv3, LGPLv2.1+, but the only file
# which is GPLv3 is an m4 macro which isn't shipped in any of our packages,
# and the LGPL bits are under lib/, which appears to be used for libgnu, which
# appears to be used for DOS builds. So we're left with GPLv2+ and PD.
LICENSE = "GPLv2+ & GPL-3.0-with-autoconf-exception & LGPLv2.1+ & PD"
LICENSE_${PN} = "GPLv2+"
LICENSE_${PN}-dev = "GPLv2+"
LICENSE_${PN}-staticdev = "GPLv2+"
LICENSE_${PN}-doc = "GPLv2+"
LICENSE_${PN}-dbg = "GPLv2+"
LICENSE_${PN}-locale = "GPLv2+"
LICENSE_liblzma = "PD"

LIC_FILES_CHKSUM = "file://COPYING;md5=c475b6c7dca236740ace4bba553e8e1c 				\
                    file://COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 			\
                    file://COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 			\
                    file://COPYING.LGPLv2.1;md5=4fbd65380cdd255951079008b364516c 		\
                    file://lib/getopt.c;endline=23;md5=2069b0ee710572c03bb3114e4532cd84	\
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "http://tukaani.org/xz/xz-${PV}.tar.xz;name=source"

SRC_URI[source.md5sum] = "60fb79cab777e3f71ca43d298adacbd5"
SRC_URI[source.sha256sum] = "7876096b053ad598c31f6df35f7de5cd9ff2ba3162e5a5554e4fc198447e0347"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
export CONFIG_SHELL="/bin/sh"


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " liblzma "

FILES_liblzma = "${libdir}/liblzma*${SOLIBS}"


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
