#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Compound image for Raspberry Pi 3"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
# swupdate includes all file in SRC_URI to the swu,
# therefore we cannot specify the license file there
inherit swupdate


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://sw-description"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "swupdate-extra-files"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
# IMAGE_DEPENDS: list of Yocto images that contains a root filesystem
# it will be ensured they are built before creating swupdate image
IMAGE_DEPENDS = "mushroom-image"

# SWUPDATE_IMAGES: list of images that will be part of the compound image
# the list can have any binaries - images must be in the DEPLOY directory
SWUPDATE_IMAGES = "mushroom-image"

SWUPDATE_FILES = "swupdate-script.sh"

# Images can have multiple formats - define which image must be
# taken to be put in the compound image
SWUPDATE_IMAGES_FSTYPES[mushroom-image] = ".ext4.gz"

COMPATIBLE_MACHINE = "raspberrypi3"


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
