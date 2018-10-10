#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "An sh-compatible command language interpreter"
HOMEPAGE = "http://tiswww.case.edu/php/chet/bash/bashtop.html"

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/bash/bash-${PV}.tar.gz;name=source"

SRC_URI[source.md5sum] = "7c112970cbdcadfc331e10eeb5f6aa41"
SRC_URI[source.sha256sum] = "57d8432be54541531a496fd4904fdc08c12542f43605a9202594fa5d5f9f2331"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native bison-native "
DEPENDS_append = " virtual/gettext ncurses virtual/libiconv glibc-locale "

RPROVIDES_${PN}_append = " /bin/bash "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_AUTORECONF_append = " --exclude=autoheader "
EXTRA_OECONF = " --enable-job-control --without-bash-malloc "

# If NON_INTERACTIVE_LOGIN_SHELLS is defined, all login shells read the
# startup files, even if they are not interactive.
# This is what other major distros do. And this is what we wanted. See bug#5359 and bug#7137.
CFLAGS_append = " -DNON_INTERACTIVE_LOGIN_SHELLS "

CACHED_CONFIGUREVARS_append = " headersdir=${includedir}/${PN} "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_configure_prepend () {
    if [ ! -e ${S}/acinclude.m4 ]; then
        cat ${S}/aclocal.m4 > ${S}/acinclude.m4
    fi
}


do_install_append () {
    # Clean buildhost references in bashbug
    sed -i -e "s,--sysroot=${STAGING_DIR_TARGET},,g" \
        -e "s,-I${WORKDIR}/\S* ,,g" \
        -e 's|${DEBUG_PREFIX_MAP}||g' \
        ${D}${bindir}/bashbug

    # Clean buildhost references in bash.pc
    sed -i -e "s,--sysroot=${STAGING_DIR_TARGET},,g" \
         ${D}${libdir}/pkgconfig/bash.pc

    # Clean buildhost references in Makefile.inc
    sed -i -e "s,--sysroot=${STAGING_DIR_TARGET},,g" \
        -e 's|${DEBUG_PREFIX_MAP}||g' \
        -e 's:${HOSTTOOLS_DIR}/::g' \
        -e 's:${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}::g' \
        ${D}${libdir}/bash/Makefile.inc
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
FILES_${PN} = "${bindir}/bash"

PACKAGES_append = " ${PN}-bashbug "
FILES_${PN}-bashbug = "${bindir}/bashbug"

PACKAGES_prepend = " ${PN}-loadable "
FILES_${PN}-loadable = "${libdir}/bash"

RDEPENDS_${PN}-loadable_append = " ${PN} "


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#
pkg_postinst_${PN} () {
    touch $D${sysconfdir}/shells
    echo ${bindir}/bash >> $D${sysconfdir}/shells
}


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#


#======================================#
#                 TEST                 #
#--------------------------------------#
