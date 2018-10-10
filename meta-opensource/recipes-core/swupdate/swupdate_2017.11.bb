#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY="Image updater for Yocto projects"
DESCRIPTION = "Application for automatic software update from USB Pen"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit pkgconfig


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://github.com/sbabic/swupdate/archive/${PV}.tar.gz;downloadfilename=swupdate-${PV}.tar.gz;name=source   \
           file://defconfig     \
           file://swupdate.cfg  \
           "

SRC_URI[source.md5sum] = "eb379adba60137e767fe1ac2d15b016d"
SRC_URI[source.sha256sum] = "1e15d9675cf7e23886dca7ea058498282c35679a555845dbc85ffe688f2cc681"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
TARGET_CC_ARCH_append = " ${LDFLAGS} "

EXTRA_OEMAKE_append = " V=1 ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} SKIP_STRIP=y "

DEPENDS_append = " libconfig kern-tools-native "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_configure() {
    cp ${WORKDIR}/defconfig ${S}/.config
}


do_compile() {
    unset LDFLAGS
    oe_runmake swupdate tools/progress
}


do_install () {
    install -d ${D}${bindir}
    install -m 0755 swupdate_unstripped ${D}${bindir}/swupdate
    install -m 0755 tools/progress_unstripped ${D}${bindir}/swupdate-progress

    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/swupdate.cfg ${D}${sysconfdir}

    install -d ${D}${libdir} ${D}${includedir}
    install -m 0644 ${S}/include/network_ipc.h ${D}${includedir}
    install -m 0644 ${S}/include/swupdate_status.h ${D}${includedir}
    install -m 0644 ${S}/include/progress.h ${D}${includedir}
    install -m 0755 ${S}/ipc/lib.a ${D}${libdir}/libswupdate.a
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " ${PN}-tools "

FILES_${PN} = "${bindir} ${sysconfdir}"
FILES_${PN}-dev = "${includedir}"
FILES_${PN}-staticdev = "${libdir}"
FILES_${PN}-tools = "${bindir}/swupdate-*"


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
