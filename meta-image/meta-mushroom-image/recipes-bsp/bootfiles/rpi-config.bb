#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "config.txt file for the Raspberry Pi"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit deploy nopackages


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = " file://config.txt "

S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
INHIBIT_DEFAULT_DEPS = "1"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_deploy() {
    install -d ${DEPLOYDIR}/bcm2835-bootfiles
    cp ${S}/config.txt ${DEPLOYDIR}/bcm2835-bootfiles/
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/bcm2835-bootfiles"


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGE_ARCH = "${MACHINE_ARCH}"


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
