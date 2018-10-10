#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Tmpfs for /root"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://root.mount.m4         \
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
TMPFS_SIZE_ROOT ??= "4M"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile() {
    if [ -z ${TMPFS_SIZE_ROOT} ]; then
        m4 -P -DMACRO_SIZE ${WORKDIR}/root.mount.m4 > ${WORKDIR}/root.mount
    else
        m4 -P -DMACRO_SIZE=size=${TMPFS_SIZE_ROOT} ${WORKDIR}/root.mount.m4 > ${WORKDIR}/root.mount
    fi
}


do_install() {
    install -d -m 0700 ${D}${ROOT_HOME}

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/root.mount ${D}${systemd_system_unitdir}/root.mount
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
SYSTEMD_STATIC_SERVICE_${PN} = " root.mount local-fs.target "


#======================================#
#                 TEST                 #
#--------------------------------------#
