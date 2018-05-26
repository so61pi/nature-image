#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "swupdate helper"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit cmake


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://swupdate-helper.cpp;subdir=${S}       \
           file://CMakeLists.txt;subdir=${S}            \
           "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " swupdate-extra-files libutil-linux systemd "

RDEPENDS_${PN} = " swupdate "


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