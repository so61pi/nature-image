#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Small and fast POSIX-compliant shell"
HOMEPAGE = "http://gondor.apana.org.au/~herbert/dash/"

LICENSE = "BSD & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b5262b4a1a1bff72b48e935531976d2e"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://git.kernel.org/pub/scm/utils/dash/dash.git/snapshot/dash-${PV}.tar.gz;name=source"

SRC_URI[source.md5sum] = "6da4e51e78de20c54310f42c85ec50bb"
SRC_URI[source.sha256sum] = "c34e1259c4179a6551dc3ceb41c668cf3be0135c5ec430deb2edfc17fff44da9"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
RPROVIDES_${PN}_append = " /bin/sh "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#
pkg_postinst_${PN} () {
    touch $D${sysconfdir}/shells
    echo ${bindir}/dash >> $D${sysconfdir}/shells
}


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#
inherit update-alternatives

ALTERNATIVE_${PN} = " sh "
ALTERNATIVE_LINK_NAME[sh] = "${bindir}/sh"
ALTERNATIVE_TARGET[sh] = "${bindir}/dash"


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#


#======================================#
#                 TEST                 #
#--------------------------------------#
