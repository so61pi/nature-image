#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Password strength checker library"
HOMEPAGE = "http://sourceforge.net/projects/cracklib"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=e3eda01d9815f8d24aae2dbd89b68b06"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools gettext

#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${SOURCEFORGE_MIRROR}/cracklib/cracklib-${PV}.tar.gz;name=source     \
           file://0001-packlib.c-support-dictionary-byte-order-dependent.patch  \
           file://0001-Apply-patch-to-fix-CVE-2016-6318.patch                   \
           file://0002-craklib-fix-testnum-and-teststr-failed.patch             \
           "

SRC_URI[source.md5sum] = "376790a95c1fb645e59e6e9803c78582"
SRC_URI[source.sha256sum] = "59ab0138bc8cf90cccb8509b6969a024d5e58d2d02bcbdccbb9ba9b88be3fa33"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " cracklib-native zlib "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = "--without-python --libdir=${libdir}"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    create-cracklib-dict -o ${D}${datadir}/cracklib/pw_dict ${D}${datadir}/cracklib/cracklib-small
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
