#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "A stream-oriented XML parser library"
HOMEPAGE = "http://expat.sourceforge.net/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c3ee559c6f9dcee1043ead112139f4f"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools lib_package


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${SOURCEFORGE_MIRROR}/expat/expat-${PV}.tar.bz2;name=source 	\
           file://0001-autotools.patch                                 	\
           "

SRC_URI[source.md5sum] = "2f47841c829facb346eb6e3fab5212e2"
SRC_URI[source.sha256sum] = "d9e50ff2d19b3538bd2127902a89987474e1a4db8e43a66a4d1a712ab9a504ff"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS_append = " pigz-native "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_unpack[depends] += " pigz-native:do_populate_sysroot "


do_configure_prepend () {
    rm -f ${S}/conftools/libtool.m4
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
