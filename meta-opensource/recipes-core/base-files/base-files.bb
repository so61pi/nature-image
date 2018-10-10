#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Miscellaneous files for the base system"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://login.defs                                                    \
           file://passwd                                                        \
           file://group                                                         \
           file://shadow                                                        \
           file://services                                                      \
           file://profile                                                       \
           file://nsswitch.conf                                                 \
           file://licenses/COPYING.MIT                                          \
           "
S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
d555 = "/proc               \
        /sys                \
        "

d755 = "/boot               \
        /dev                \
        /mnt                \
        /run                \
        /data               \
        ${sysconfdir}       \
        ${localstatedir}    \
        ${datadir}          \
        ${bindir}           \
        ${sbindir}          \
        ${libdir}           \
        ${libexecdir}       \
        ${servicedir}       \
        "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install () {
    for d in ${d555}; do
        install -m 0555 -d ${D}${d}
    done
    for d in ${d755}; do
        install -m 0755 -d ${D}${d}
    done

    install -m 0640 ${WORKDIR}/login.defs       ${D}${sysconfdir}/login.defs
    install -m 0640 ${WORKDIR}/shadow           ${D}${sysconfdir}/shadow
    install -m 0644 ${WORKDIR}/passwd           ${D}${sysconfdir}/passwd
    install -m 0644 ${WORKDIR}/group            ${D}${sysconfdir}/group

    install -m 0644 /dev/null                   ${D}${sysconfdir}/shells
    install -m 0644 ${WORKDIR}/services         ${D}${sysconfdir}/services
    install -m 0644 ${WORKDIR}/nsswitch.conf    ${D}${sysconfdir}/nsswitch.conf
    install -m 0644 ${WORKDIR}/profile          ${D}${sysconfdir}/profile

    ln -sr ${D}/proc/self/mounts                ${D}${sysconfdir}/mtab
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


#======================================#
#                 TEST                 #
#--------------------------------------#
