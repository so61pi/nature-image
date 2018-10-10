#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Utilities for managing POSIX Access Control Lists"
HOMEPAGE = "http://savannah.nongnu.org/projects/acl/"

LICENSE = "LGPLv2.1+ & GPLv2+"
LICENSE_${PN} = "GPLv2+"
LICENSE_lib${BPN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=c781d70ed2b4d48995b790403217a249         \
                    file://doc/COPYING.LGPL;md5=9e9a206917f8af112da634ce3ab41764    \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
require ea-acl.inc


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI += " ${SAVANNAH_GNU_MIRROR}/acl/acl-${PV}.src.tar.gz;name=source    \
             file://configure.ac;subdir=${S}                                \
             file://acl-fix-the-order-of-expected-output-of-getfacl.patch   \
             file://Makefile-libacl-should-depend-on-include.patch          \
             "
SRC_URI[source.md5sum] = "a61415312426e9c2212bd7dc7929abda"
SRC_URI[source.sha256sum] = "179074bb0580c06c4b4137be4c5a92a701583277967acdb5546043c7874e0d23"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "attr"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
# avoid RPATH hardcode to staging dir
do_configure_append() {
    sed -i ${S}/config.status -e s,^\\\(hardcode_into_libs=\\\).*$,\\1\'no\',
    ${S}/config.status
}


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
