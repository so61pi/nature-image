#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "initflash"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://disklayout            \
           file://flash                 \
           file://initflash.service     \
           "
S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
RDEPENDS_${PN} = " dash swupdate "

# poweroff 
RDEPENDS_${PN}_append = " systemd "

# fw_setenv 
RDEPENDS_${PN}_append = " u-boot-fw-utils "

# dd mktemp sleep
RDEPENDS_${PN}_append = " coreutils "

# sfdisk mount
RDEPENDS_${PN}_append = " util-linux-fdisk util-linux-mount "

# mkfs.vfat
RDEPENDS_${PN}_append = " dosfstools "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install () {
    install -d ${D}/initflash
    install -m 0644 ${WORKDIR}/disklayout ${D}/initflash/disklayout
    install -m 0755 ${WORKDIR}/flash      ${D}/initflash/flash

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/initflash.service ${D}${systemd_system_unitdir}/initflash.service
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES = "${PN}"
FILES_${PN} = "/"


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#
inherit systemd-simple

SYSTEMD_PACKAGES = " ${PN} "
SYSTEMD_SERVICE_${PN} = " initflash.service "


#======================================#
#                 TEST                 #
#--------------------------------------#
