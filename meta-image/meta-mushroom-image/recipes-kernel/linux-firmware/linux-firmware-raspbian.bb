#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Firmware files for use with Linux kernel"

LICENSE = "Firmware-broadcom_bcm43xx"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc"

# These are not common licenses, set NO_GENERIC_LICENSE for them
# so that the license files will be copied from fetched source
NO_GENERIC_LICENSE[Firmware-broadcom_bcm43xx] = "LICENCE.broadcom_bcm43xx"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit allarch


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "git://github.com/RPi-Distro/firmware-nonfree;protocol=https;name=source"

SRCREV = "86e88fbf0345da49555d0ec34c80b4fbae7d0cd3"
PV = "0.0+git${SRCPV}"

S = "${WORKDIR}/git"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
INHIBIT_DEFAULT_DEPS = "1"
CLEANBROKEN = "1"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile[noexec] = "1"

do_install() {
	install -d ${D}${nonarch_base_libdir}/firmware/brcm
	cp ./LICENCE.broadcom_bcm43xx ${D}${nonarch_base_libdir}/firmware
	cp -r ./brcm/brcmfmac43430* ${D}${nonarch_base_libdir}/firmware/brcm
	cp -r ./brcm/brcmfmac43455* ${D}${nonarch_base_libdir}/firmware/brcm
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
# Firmware files are generally not ran on the CPU, so they can be
# allarch despite being architecture specific
INSANE_SKIP = "arch"

PACKAGES = " \
	${PN}-broadcom-license \
	${PN}-bcm43430 \
	${PN}-bcm43455 \
	"

LICENSE_${PN}-bcm43430 = "Firmware-broadcom_bcm43xx"
LICENSE_${PN}-bcm43455 = "Firmware-broadcom_bcm43xx"
LICENSE_${PN}-broadcom-license = "Firmware-broadcom_bcm43xx"
FILES_${PN}-broadcom-license = "${nonarch_base_libdir}/firmware/LICENCE.broadcom_bcm43xx"
FILES_${PN}-bcm43430 = "${nonarch_base_libdir}/firmware/brcm/brcmfmac43430*"
FILES_${PN}-bcm43455 = "${nonarch_base_libdir}/firmware/brcm/brcmfmac43455*"
RDEPENDS_${PN}-bcm43430 += "${PN}-broadcom-license"
RDEPENDS_${PN}-bcm43455 += "${PN}-broadcom-license"


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
