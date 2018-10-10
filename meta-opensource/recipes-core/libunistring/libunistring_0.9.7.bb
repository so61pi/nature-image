#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Library for manipulating C and Unicode strings"

HOMEPAGE = "http://www.gnu.org/software/libunistring/"

LICENSE = "LGPLv3+ | GPLv2"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
                    file://README;beginline=45;endline=65;md5=08287d16ba8d839faed8d2dc14d7d6a5 \
                    file://doc/libunistring.texi;md5=b86c9fd7acaac623017239640631912c \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/libunistring/libunistring-${PV}.tar.xz;name=source \
           file://iconv-m4-remove-the-test-to-convert-euc-jp.patch          \
           "

SRC_URI[source.md5sum] = "82e0545363d111bfdfec2ddbfe62ffd3"
SRC_URI[source.sha256sum] = "2e3764512aaf2ce598af5a38818c0ea23dedf1ff5460070d1b6cee5c3336e797"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


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

