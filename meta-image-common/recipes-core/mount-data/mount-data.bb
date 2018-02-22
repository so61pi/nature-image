#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Mount /data"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://data.mount        \
           "
S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
RDEPENDS_${PN} = " systemd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
INHIBIT_DEFAULT_DEPS = "1"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install() {
    install -d ${D}/data

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/data.mount ${D}${systemd_system_unitdir}/data.mount
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
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
inherit systemd-static-simple

SYSTEMD_STATIC_PACKAGES = " ${PN} "
SYSTEMD_STATIC_SERVICE_${PN} = " data.mount local-fs.target "


#======================================#
#                 TEST                 #
#--------------------------------------#
