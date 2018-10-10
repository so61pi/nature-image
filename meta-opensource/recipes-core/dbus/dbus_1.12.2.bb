#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "D-Bus message bus"
HOMEPAGE = "http://dbus.freedesktop.org"

LICENSE = "AFL-2 | GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=10dded3b58148f3f1fd804b26354af3e \
                    file://dbus/dbus.h;beginline=6;endline=20;md5=7755c9d7abccd5dbd25a6a974538bb3c"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools pkgconfig


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz;name=source  \
           file://tmpfiles-dbus.conf                                                \
           "

SRC_URI[source.md5sum] = "3361456cadb99aa6601bed5b48964254"
SRC_URI[source.sha256sum] = "272bb5091770b047c8188b926d5e6038fa4fe6745488b2add96b23e2d9a83d88"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " autoconf-archive-native "
DEPENDS_append = " expat virtual/libintl systemd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG[x11] = "--with-x --enable-x11-autolaunch,--without-x --disable-x11-autolaunch, virtual/libx11 libsm"

EXTRA_OECONF = "        \
    --enable-inotify    \
    --enable-epoll      \
    --disable-libaudit  \
                        \
    --enable-systemd                                        \
    --with-systemdsystemunitdir=${systemd_system_unitdir}   \
                                                            \
    --enable-user-session                                   \
    --with-systemduserunitdir=${systemd_user_unitdir}       \
    "

DEBIANNAME_${PN} = "dbus-1"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    # Remove /etc/dbus-1/{session.conf,system.conf} because they contain
    #   "This configuration file is no longer required and may be removed."
    # For more info, read the content of those files.
    rm -r ${D}${sysconfdir}/dbus-1

    # Install my tmpfiles-dbus.conf to additionally create "/var/lib/dbus" at bootup.
    install -d ${D}${libdir}/tmpfiles.d
    install -m 0644 ${WORKDIR}/tmpfiles-dbus.conf ${D}${libdir}/tmpfiles.d/dbus.conf
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
FILES_${PN}_append = "      \
    ${libdir}/tmpfiles.d    \
    ${libdir}/sysusers.d    \
    ${datadir}/dbus-1       \
    ${datadir}/xml          \
    ${systemd_unitdir}      \
    /run \
    "

FILES_${PN}-dev_append= " ${libdir}/cmake ${libdir}/dbus-1.0/include "


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#
inherit useradd-simple

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN}  = "--system --user-group --shell /bin/false --password '*' messagebus"


#======================================#
#                 TEST                 #
#--------------------------------------#
