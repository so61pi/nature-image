#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Traditional Unix macro processor"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504             \
                    file://examples/COPYING;md5=4031593b2166d6c47cae282d944a7ede    \
                    "



#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/m4/m4-${PV}.tar.xz;name=source \
           file://ac_config_links.patch                 \
           file://remove-gets.patch                     \
           "

SRC_URI[source.md5sum] = "730bb15d96fffe47e148d1e09235af82"
SRC_URI[source.sha256sum] = "f2c1e86ca0a404ff281631bdc8377638992744b175afb806e25871a24a934e07"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OEMAKE = " 'infodir=${infodir}' "
EXTRA_OECONF = " --without-libsigsegv-prefix "

# Fix "Argument list too long" error when len(TMPDIR) = 410
acpaths = "-I ./m4"


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
