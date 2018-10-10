#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Tmpfs for /tmp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://tmp.mount.m4          \
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
TMPFS_SIZE_TMP ??= ""


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile() {
    if [ -z ${TMPFS_SIZE_TMP} ]; then
        m4 -P -DMACRO_SIZE ${WORKDIR}/tmp.mount.m4 > ${WORKDIR}/tmp.mount
    else
        m4 -P -DMACRO_SIZE=size=${TMPFS_SIZE_TMP} ${WORKDIR}/tmp.mount.m4 > ${WORKDIR}/tmp.mount
    fi
}


do_install() {
    install -d -m 1777 ${D}/tmp

    # Shadow stock tmp.mount
    install -d ${D}${sysconfdir}/systemd/system
    install -m 0644 ${WORKDIR}/tmp.mount ${D}${sysconfdir}/systemd/system/tmp.mount
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
inherit systemd-simple

SYSTEMD_PACKAGES = " ${PN} "
SYSTEMD_SERVICE_${PN} = " tmp.mount "


#======================================#
#                 TEST                 #
#--------------------------------------#
