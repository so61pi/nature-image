#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "A lightweight SSH and SCP implementation"
HOMEPAGE = "http://matt.ucc.asn.au/dropbear/dropbear.html"

LICENSE = "MIT & BSD-3-Clause & BSD-2-Clause & PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5ec40cafba26fc4396d0b550f824e01"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://matt.ucc.asn.au/dropbear/releases/dropbear-${PV}.tar.bz2;name=source \
           file://0001-urandom-xauth-changes-to-options.h.patch \
           file://0003-configure.patch                          \
           file://0004-fix-2kb-keys.patch                       \
           file://0007-dropbear-fix-for-x32-abi.patch           \
           file://fix-libtomcrypt-libtommath-ordering.patch     \
           file://dropbear@.service                             \
           file://dropbear.socket                               \
           file://0005-dropbear-enable-pam.patch                \
           file://0006-dropbear-configuration-file.patch        \
           file://dropbear                                      \
           "

SRC_URI[source.md5sum] = "e57e9b9d25705dcb073ba15c416424fd"
SRC_URI[source.sha256sum] = "6cbc1dcb1c9709d226dff669e5604172a18cf5dbf9a201474d5618ae4465098c"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " zlib libpam "
RDEPENDS_${PN} = " dash "

RDEPENDS_${PN}_append = " libpam-runtime pam-plugin-deny pam-plugin-permit pam-plugin-unix "

RPROVIDES_${PN} = " ssh sshd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
SBINCOMMANDS = " dropbear dropbearkey dropbearconvert "
BINCOMMANDS = " dbclient ssh scp "

PACKAGECONFIG[system-libtom] = "--disable-bundled-libtom,--enable-bundled-libtom,libtommath libtomcrypt"

EXTRA_OEMAKE = 'MULTI=1 SCPPROGRESS=1 PROGRAMS="${SBINCOMMANDS} ${BINCOMMANDS}"'
EXTRA_OECONF = " --enable-pam "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install() {
    install -d ${D}${bindir} ${D}${sbindir} ${D}${sysconfdir}/dropbear

    install -m 0755 dropbearmulti ${D}${sbindir}/dropbearmulti

    for i in ${BINCOMMANDS}; do
        ln -sr ${D}${sbindir}/dropbearmulti ${D}${bindir}/$i
    done

    for i in ${SBINCOMMANDS}; do
        ln -sr ${D}${sbindir}/dropbearmulti ${D}${sbindir}/$i
    done

    # pam stuff
    install -d ${D}${sysconfdir}/pam.d
    install -m 0644 ${WORKDIR}/dropbear ${D}${sysconfdir}/pam.d/dropbear

    # systemd stuff
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/dropbear@.service    ${D}${systemd_system_unitdir}/dropbear@.service
    install -m 0644 ${WORKDIR}/dropbear.socket      ${D}${systemd_system_unitdir}/dropbear.socket
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
FILES_${PN}_append = " ${systemd_system_unitdir}/ "


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
SYSTEMD_SERVICE_${PN} = " dropbear.socket "


#======================================#
#                 TEST                 #
#--------------------------------------#
