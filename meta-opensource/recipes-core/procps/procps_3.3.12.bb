#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "System and process monitoring utilities"

LICENSE = "GPLv2+ & LGPLv2+"
LIC_FILES_CHKSUM = "                                            \
    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263         \
    file://COPYING.LIB;md5=4cf66a4984120007c9881cc871cf49db     \
    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools pkgconfig utility


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${SOURCEFORGE_MIRROR}/project/procps-ng/Production/procps-ng-${PV}.tar.xz;name=source"

SRC_URI[source.md5sum] = "957e42e8b193490b2111252e4a2b443c"
SRC_URI[source.sha256sum] = "6ed65ab86318f37904e8f9014415a098bec5bc53653e5d9ab404f95ca5e1a7d4"

S = "${WORKDIR}/procps-ng-${PV}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext ncurses systemd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = " --with-systemd "


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
# ps and top are in default package

PACKAGES_prepend = "        \
    ${PN}-free              \
    ${PN}-kill              \
    ${PN}-pgrep             \
    ${PN}-pidof             \
    ${PN}-pkill             \
    ${PN}-pmap              \
    ${PN}-pwdx              \
    ${PN}-slabtop           \
    ${PN}-sysctl            \
    ${PN}-tload             \
    ${PN}-uptime            \
    ${PN}-vmstat            \
    ${PN}-w                 \
    ${PN}-watch             \
    "

FILES_${PN}-free        = "${@binpath(d, 'free')}"
FILES_${PN}-kill        = "${@binpath(d, 'kill')}"
FILES_${PN}-pgrep       = "${@binpath(d, 'pgrep')}"
FILES_${PN}-pidof       = "${@binpath(d, 'pidof')}"
FILES_${PN}-pkill       = "${@binpath(d, 'pkill')}"
FILES_${PN}-pmap        = "${@binpath(d, 'pmap')}"
FILES_${PN}-pwdx        = "${@binpath(d, 'pwdx')}"
FILES_${PN}-slabtop     = "${@binpath(d, 'slabtop')}"
FILES_${PN}-sysctl      = "${@binpath(d, 'sysctl')}"
FILES_${PN}-tload       = "${@binpath(d, 'tload')}"
FILES_${PN}-uptime      = "${@binpath(d, 'uptime')}"
FILES_${PN}-vmstat      = "${@binpath(d, 'vmstat')}"
FILES_${PN}-watch       = "${@binpath(d, 'watch')}"
FILES_${PN}-w           = "${@binpath(d, 'w')}"


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
