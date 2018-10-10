#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "DOS FAT Filesystem Utilities"
HOMEPAGE = "https://github.com/dosfstools/dosfstools"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools pkgconfig


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://github.com/dosfstools/dosfstools/releases/download/v${PV}/dosfstools-${PV}.tar.xz;name=source"

SRC_URI[source.md5sum] = "07a1050db1a898e9a2e03b0c4569c4bd"
SRC_URI[source.sha256sum] = "e6b2aca70ccc3fe3687365009dd94a2e18e82b688ed4e260e04b7412471cc173"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " systemd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = "                        \
    --with-udev                         \
    --enable-compat-symlinks			\
    "

CFLAGS_append = " -D_GNU_SOURCE -D_LARGEFILE_SOURCE -D_FILE_OFFSET_BITS=64 "


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
