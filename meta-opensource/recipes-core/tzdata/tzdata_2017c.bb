#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Timezone data"
HOMEPAGE = "http://www.iana.org/time-zones"

LICENSE = "PD & BSD & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c679c9d6b02bc2757b3eaf8f53c43fba"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit allarch


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "http://www.iana.org/time-zones/repository/releases/tzdata${PV}.tar.gz;name=source"

SRC_URI[source.md5sum] = "1e751e7e08f8b68530674f04619d894d"
SRC_URI[source.sha256sum] = "d6543f92a929826318e2f44ff3a7611ce5f565a43e10250b42599d0ba4cbd90b"

S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "tzcode-native"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
DEFAULT_TIMEZONE ??= "UTC"

TZONES= "africa antarctica asia australasia europe northamerica southamerica    \
         factory            \
         etcetera           \
         backward systemv   \
         "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile () {
    for zone in ${TZONES}; do
        ${STAGING_BINDIR_NATIVE}/zic -d ${WORKDIR}${datadir}/zoneinfo       -L /dev/null        -y ${S}/yearistype.sh ${S}/${zone} ;
        ${STAGING_BINDIR_NATIVE}/zic -d ${WORKDIR}${datadir}/zoneinfo/posix -L /dev/null        -y ${S}/yearistype.sh ${S}/${zone} ;
        ${STAGING_BINDIR_NATIVE}/zic -d ${WORKDIR}${datadir}/zoneinfo/right -L ${S}/leapseconds -y ${S}/yearistype.sh ${S}/${zone} ;
    done
}


do_install () {
    install -d ${D}${datadir}/zoneinfo
    cp -r ${S}/${datadir}/zoneinfo/* ${D}${datadir}/zoneinfo/

    # libc is removing zoneinfo files from package
    cp "${S}/zone.tab"      ${D}${datadir}/zoneinfo/zone.tab
    cp "${S}/zone1970.tab"  ${D}${datadir}/zoneinfo/zone1970.tab
    cp "${S}/iso3166.tab"   ${D}${datadir}/zoneinfo/iso3166.tab

    # Install default timezone
    if [ -e ${D}${datadir}/zoneinfo/${DEFAULT_TIMEZONE} ]; then
        install -d ${D}${sysconfdir}
        echo ${DEFAULT_TIMEZONE} > ${D}${sysconfdir}/timezone
        ln -sr ${datadir}/zoneinfo/${DEFAULT_TIMEZONE} ${D}${sysconfdir}/localtime
    else
        bberror "DEFAULT_TIMEZONE is set to an invalid value."
        exit 1
    fi
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
