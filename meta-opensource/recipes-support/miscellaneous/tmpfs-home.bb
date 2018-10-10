#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Tmpfs for /home"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://home.mount.m4         \
           file://mkhomedir@.service    \
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
TMPFS_SIZE_HOME ??= "8M"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile() {
    if [ -z ${TMPFS_SIZE_HOME} ]; then
        m4 -P -DMACRO_SIZE ${WORKDIR}/home.mount.m4 > ${WORKDIR}/home.mount
    else
        m4 -P -DMACRO_SIZE=size=${TMPFS_SIZE_HOME} ${WORKDIR}/home.mount.m4 > ${WORKDIR}/home.mount
    fi
}


do_install() {
    install -d ${D}/home

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/home.mount           ${D}${systemd_system_unitdir}/home.mount
    install -m 0644 ${WORKDIR}/mkhomedir@.service   ${D}${systemd_system_unitdir}/mkhomedir@.service
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
SYSTEMD_STATIC_SERVICE_${PN} = " home.mount local-fs.target "


#======================================#
#                 TEST                 #
#--------------------------------------#
