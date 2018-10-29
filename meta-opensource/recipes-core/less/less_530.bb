#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Text file viewer similar to more"
HOMEPAGE = "http://www.greenwoodsoftware.com/"

# (GPLv2+ (<< 418), GPLv3+ (>= 418)) | less
# Including email author giving permissing to use BSD
#
# From: Mark Nudelman <markn@greenwoodsoftware.com>
# To: Elizabeth Flanagan <elizabeth.flanagan@intel.com
# Date: 12/19/11
#
# Hi Elizabeth,
# Using a generic BSD license for less is fine with me.
# Thanks,
#
# --Mark
#

LICENSE = "GPLv3+ | BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://LICENSE;md5=2ef3e4b8dafc85612bc5254b8081e234 \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "http://www.greenwoodsoftware.com/less/less-${PV}.tar.gz;name=source"

SRC_URI[source.md5sum] = "6a39bccf420c946b0fd7ffc64961315b"
SRC_URI[source.sha256sum] = "503f91ab0af4846f34f0444ab71c4b286123f0044a4964f1ae781486c617f2e2"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "ncurses"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install() {
    oe_runmake 'bindir=${D}${bindir}' 'mandir=${D}${mandir}' install
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
