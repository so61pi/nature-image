#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "GNU multiprecision arithmetic library"
HOMEPAGE = "http://gmplib.org/"

LICENSE = "GPLv2+ | LGPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504             \
                    file://COPYING.LESSERv3;md5=6a6a8e020838b23406c81b19c1d46df6    \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263           \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo


#======================================#
#                SOURCE                #
#--------------------------------------#
REVISION = ""
SRC_URI = "https://gmplib.org/download/${BPN}/${BP}${REVISION}.tar.bz2;name=source  \
           file://amd64.patch                                                       \
           file://use-includedir.patch                                              \
           file://0001-Append-the-user-provided-flags-to-the-auto-detected-.patch   \
           file://0001-confiure.ac-Believe-the-cflags-from-environment.patch        \
           "

SRC_URI[source.md5sum] = "8ddbb26dc3bd4e2302984debba1406a5"
SRC_URI[source.sha256sum] = "5275bb04f4863a13516b2f39392ac5e272f5e1bb8057b18aec1c9b79d73d8fb2"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG[readline] = "--with-readline=yes,--with-readline=no,readline"

EXTRA_OECONF_append = " --enable-cxx=detect "
EXTRA_OECONF_mipsarchr6_append = " --disable-assembly "

ARM_INSTRUCTION_SET_armv4 = "arm"
ARM_INSTRUCTION_SET_armv5 = "arm"

# Doesn't compile in MIPS16e mode due to use of hand-written
# assembly
MIPS_INSTRUCTION_SET = "mips"

SSTATE_SCAN_FILES += "gmp.h"

acpaths = ""


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    sed -i "s|--sysroot=${STAGING_DIR_HOST}||g" ${D}${includedir}/gmp.h
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " libgmpxx "
FILES_libgmpxx = " ${libdir}/libgmpxx${SOLIBS} "


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
