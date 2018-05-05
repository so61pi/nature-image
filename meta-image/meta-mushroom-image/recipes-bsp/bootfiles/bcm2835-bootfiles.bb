#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit deploy nopackages


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://github.com/raspberrypi/firmware/archive/${SRCREV}.tar.gz"
SRCREV = "af994023ab491420598bfd5648b9dcab956f7e11"

SRC_URI[md5sum] = "0c388f3631093368ac92a15a262d0f7c"
SRC_URI[sha256sum] = "e2afa23886de586856a1d604da2a85e4559715816dccbd70ae828b840beafc21"

PV = "20180313"
S = "${WORKDIR}/firmware-${SRCREV}/boot"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "rpi-config rpi-u-boot-scr"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
INHIBIT_DEFAULT_DEPS = "1"
COMPATIBLE_MACHINE = "raspberrypi3"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_deploy() {
    install -d ${DEPLOYDIR}/${PN}

    for i in ${S}/*.elf ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
    for i in ${S}/*.dat ; do
        cp $i ${DEPLOYDIR}/${PN}
    done
    for i in ${S}/*.bin ; do
        cp $i ${DEPLOYDIR}/${PN}
    done

    # Add stamp in deploy directory
    touch ${DEPLOYDIR}/${PN}/${PN}-${PV}.stamp
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"


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
