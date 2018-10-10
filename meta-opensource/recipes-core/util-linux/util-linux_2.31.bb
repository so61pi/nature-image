#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
require util-linux-common.inc

inherit utility


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI_append = "          \
    file://login            \
    "

MAJORVER = "2.31"
SRC_URI[source.md5sum] = "5b6821c403c3cc6e7775f74df1882a20"
SRC_URI[source.sha256sum] = "f9be7cdcf4fc5c5064a226599acdda6bdf3d86c640152ba01ea642d91108dc8a"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS_append = " systemd libutil-linux libpam "

RDEPENDS_${PN}_append = " libutil-linux "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF_append = "         \
    --enable-all-programs       \
    --with-udev                 \
    --with-systemd              \
    "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    rm -f ${@binpath(d, 'setarch uname26 linux32 linux64 i386 x86_64', d.getVar('D'))}

    # remove libs and headers as they are already provided by libutil-linux
    rm ${D}${libdir}/lib*
    rm -r ${D}${libdir}/pkgconfig ${D}${includedir}

    install -d ${D}${sysconfdir}/pam.d
    install -m 0644 ${WORKDIR}/login ${D}${sysconfdir}/pam.d/login
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = "        \
    ${PN}-agetty            \
    ${PN}-chmem             \
    ${PN}-fdisk             \
    ${PN}-eject             \
    ${PN}-fallocate         \
    ${PN}-fdformat          \
    ${PN}-fsck              \
    ${PN}-fstrim            \
    ${PN}-hwclock           \
    ${PN}-ipcrm             \
    ${PN}-ipcs              \
    ${PN}-kill              \
    ${PN}-last              \
    ${PN}-line              \
    ${PN}-logger            \
    ${PN}-login             \
    ${PN}-losetup           \
    ${PN}-lslogins          \
    ${PN}-lsmem             \
    ${PN}-mesg              \
    ${PN}-more              \
    ${PN}-mount             \
    ${PN}-mountpoint        \
    ${PN}-newgrp            \
    ${PN}-nologin           \
    ${PN}-nsenter           \
    ${PN}-partx             \
    ${PN}-pg                \
    ${PN}-pivot-root        \
    ${PN}-raw               \
    ${PN}-rename            \
    ${PN}-rfkill            \
    ${PN}-rtcwake           \
    ${PN}-schedutils        \
    ${PN}-setterm           \
    ${PN}-sulogin           \
    ${PN}-swaponoff         \
    ${PN}-switch-root       \
    ${PN}-tunelp            \
    ${PN}-ul                \
    ${PN}-unshare           \
    ${PN}-utmpdump          \
    ${PN}-uuidd             \
    ${PN}-uuidgen           \
    ${PN}-vipw              \
    ${PN}-wall              \
    ${PN}-wdctl             \
    ${PN}-write             \
    ${PN}-zramctl           \
    "

FILES_${PN}-agetty          = "${@binpath(d, 'agetty')}"
FILES_${PN}-chmem           = "${@binpath(d, 'chmem')}"
FILES_${PN}-fdisk           = "${@binpath(d, 'fdisk sfdisk')}"
FILES_${PN}-eject           = "${@binpath(d, 'eject')}"
FILES_${PN}-fallocate       = "${@binpath(d, 'fallocate')}"
FILES_${PN}-fdformat        = "${@binpath(d, 'fdformat')}"
FILES_${PN}-fsck            = "${@binpath(d, 'fsck')}"
FILES_${PN}-fstrim          = "${@binpath(d, 'fstrim')} ${systemd_system_unitdir}/fstrim.*"
FILES_${PN}-hwclock         = "${@binpath(d, 'hwclock')}"
FILES_${PN}-ipcrm           = "${@binpath(d, 'ipcrm')}"
FILES_${PN}-ipcs            = "${@binpath(d, 'ipcs')}"
FILES_${PN}-kill            = "${@binpath(d, 'kill')}"
FILES_${PN}-last            = "${@binpath(d, 'last lastb')}"
FILES_${PN}-line            = "${@binpath(d, 'line')}"
FILES_${PN}-logger          = "${@binpath(d, 'logger')}"
FILES_${PN}-login           = "${@binpath(d, 'login')} ${sysconfdir}/pam.d/login"
FILES_${PN}-losetup         = "${@binpath(d, 'losetup')}"
FILES_${PN}-lslogins        = "${@binpath(d, 'lslogins')}"
FILES_${PN}-lsmem           = "${@binpath(d, 'lsmem')}"
FILES_${PN}-mesg            = "${@binpath(d, 'mesg')}"
FILES_${PN}-more            = "${@binpath(d, 'more')}"
FILES_${PN}-mount           = "${@binpath(d, 'mount umount')}"
FILES_${PN}-mountpoint      = "${@binpath(d, 'mountpoint')}"
FILES_${PN}-newgrp          = "${@binpath(d, 'newgrp')}"
FILES_${PN}-nologin         = "${@binpath(d, 'nologin')}"
FILES_${PN}-nsenter         = "${@binpath(d, 'nsenter')}"
FILES_${PN}-partx           = "${@binpath(d, 'addpart delpart partx')}"
FILES_${PN}-pg              = "${@binpath(d, 'pg')}"
FILES_${PN}-pivot-root      = "${@binpath(d, 'pivot_root')}"
FILES_${PN}-raw             = "${@binpath(d, 'raw')}"
FILES_${PN}-rename          = "${@binpath(d, 'rename')}"
FILES_${PN}-rfkill          = "${@binpath(d, 'rfkill')}"
FILES_${PN}-rtcwake         = "${@binpath(d, 'rtcwake')}"
FILES_${PN}-schedutils      = "${@binpath(d, 'chrt taskset ionice')}"
FILES_${PN}-setterm         = "${@binpath(d, 'setterm')}"
FILES_${PN}-sulogin         = "${@binpath(d, 'sulogin')}"
FILES_${PN}-swaponoff       = "${@binpath(d, 'swapon swapoff mkswap')}"
FILES_${PN}-switch-root     = "${@binpath(d, 'switch_root')}"
FILES_${PN}-tunelp          = "${@binpath(d, 'tunelp')}"
FILES_${PN}-ul              = "${@binpath(d, 'ul')}"
FILES_${PN}-unshare         = "${@binpath(d, 'unshare')}"
FILES_${PN}-utmpdump        = "${@binpath(d, 'utmpdump')}"
FILES_${PN}-uuidd           = "${@binpath(d, 'uuidd')} ${systemd_system_unitdir}/uuidd.*"
FILES_${PN}-uuidgen         = "${@binpath(d, 'uuidgen uuidparse')}"
FILES_${PN}-vipw            = "${@binpath(d, 'vipw vigr')}"
FILES_${PN}-wall            = "${@binpath(d, 'wall')}"
FILES_${PN}-wdctl           = "${@binpath(d, 'wdctl')}"
FILES_${PN}-write           = "${@binpath(d, 'write')}"
FILES_${PN}-zramctl         = "${@binpath(d, 'zramctl')}"


RDEPENDS_${PN}-agetty = " ${PN}-login "
RDEPENDS_${PN}-login  = "                                   \
    pam-plugin-securetty pam-plugin-nologin pam-plugin-env  \
    pam-plugin-group pam-plugin-limits pam-plugin-lastlog   \
    pam-plugin-motd pam-plugin-mail pam-plugin-loginuid     \
    pam-plugin-faildelay pam-plugin-time pam-plugin-access  \
    "

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

SYSTEMD_PACKAGES = " ${PN}-uuidd "
SYSTEMD_SERVICE_${PN}-uuidd = " uuidd.service "


#======================================#
#                 TEST                 #
#--------------------------------------#
