#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Tmpfs for /var"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://var.mount.m4          \
           file://licenses/COPYING.MIT  \
           "
S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " m4-native "

RDEPENDS_${PN} = " systemd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
TMPFS_SIZE_VAR ??= ""


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile() {
    if [ -z ${TMPFS_SIZE_VAR} ]; then
        m4 -P -DMACRO_SIZE ${WORKDIR}/var.mount.m4 > ${WORKDIR}/var.mount
    else
        m4 -P -DMACRO_SIZE=size=${TMPFS_SIZE_VAR} ${WORKDIR}/var.mount.m4 > ${WORKDIR}/var.mount
    fi
}


do_install() {
    install -d ${D}/var

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/var.mount ${D}${systemd_system_unitdir}/var.mount
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
SYSTEMD_STATIC_SERVICE_${PN} = " var.mount local-fs.target "


#======================================#
#                 TEST                 #
#--------------------------------------#
