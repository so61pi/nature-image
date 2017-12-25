FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://swupdate-net "

DEPENDS_append = " libarchive zlib systemd lua curl json-c u-boot-fw-utils "
RDEPENDS_${PN}_append = " u-boot-fw-utils grep "

do_install_append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/swupdate-net ${D}${bindir}/swupdate-net
}
