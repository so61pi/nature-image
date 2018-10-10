#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Utilities and libraries for handling compiled object files"
HOMEPAGE = "https://sourceware.org/elfutils"

LICENSE = "(GPLv3 & Elfutils-Exception)"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://sourceware.org/elfutils/ftp/${PV}/elfutils-${PV}.tar.bz2;name=source"

SRC_URI[source.md5sum] = "03599aee98c9b726c7a732a2dd0245d5"
SRC_URI[source.sha256sum] = "1f844775576b79bdc9f9c717a50058d08620323c1e935458223a12f249c9e066"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG ??= "zlib"

PACKAGECONFIG[zlib] = "--with-zlib, --without-zlib, zlib"
PACKAGECONFIG[bzlib] = "--with-bzlib, --without-bzlib, bzip2"

EXTRA_OECONF = "            \
    --program-prefix=eu-    \
    --without-lzma          \
    "


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " libelf libasm libdw "

FILES_libelf = "${libdir}/libelf-${PV}.so ${libdir}/libelf.so.*"
FILES_libasm = "${libdir}/libasm-${PV}.so ${libdir}/libasm.so.*"
FILES_libdw  = "${libdir}/libdw-${PV}.so ${libdir}/libdw.so.* ${libdir}/elfutils/lib*"

INSANE_SKIP_${MLPREFIX}libdw = "dev-so"


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
