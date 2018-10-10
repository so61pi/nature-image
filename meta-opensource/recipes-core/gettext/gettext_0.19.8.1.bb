#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Utilities and libraries for producing multi-lingual messages"
HOMEPAGE = "http://www.gnu.org/software/gettext/gettext.html"

LICENSE = "GPLv3+ & LGPL-2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/gettext/gettext-${PV}.tar.gz;name=source       \
           file://parallel.patch                                        \
           file://add-with-bisonlocaledir.patch                         \
           file://cr-statement.c-timsort.h-fix-formatting-issues.patch  \
           "

SRC_URI[source.md5sum] = "97e034cf8ce5ba73a28ff6c3c0638092"
SRC_URI[source.sha256sum] = "ff942af0e438ced4a8b0ea4b0b6e0d6d657157c5e2364de57baa279c1c125c43"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/libiconv expat "

PROVIDES = " virtual/libintl virtual/gettext "

RCONFLICTS_${PN} = " proxy-libintl "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG[msgcat-curses] = "--with-libncurses-prefix=${STAGING_LIBDIR}/..,--disable-curses,ncurses"

EXTRA_OECONF_append = "                     \
    --without-lispdir                       \
    --disable-csharp                        \
    --disable-libasprintf                   \
    --disable-java                          \
    --disable-native-java                   \
    --disable-openmp                        \
    --disable-acl                           \
    --with-included-glib                    \
    --without-emacs                         \
    --without-cvs                           \
    --without-git                           \
    --with-included-libxml                  \
    --with-included-libcroco                \
    --with-included-libunistring            \
    --with-bisonlocaledir=${datadir}/locale \
    "

acpaths = '-I ${S}/gettext-runtime/m4 \
           -I ${S}/gettext-tools/m4'


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    rm -f ${D}${libdir}/preloadable_libintl.so
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#

# these lack the .x behind the .so, but shouldn't be in the -dev package
# Otherwise you get the following results:
# 7.4M    glibc/images/ep93xx/Angstrom-console-image-glibc-ipk-2008.1-test-20080104-ep93xx.rootfs.tar.gz
# 25M     uclibc/images/ep93xx/Angstrom-console-image-uclibc-ipk-2008.1-test-20080104-ep93xx.rootfs.tar.gz
# because gettext depends on gettext-dev, which pulls in more -dev packages:
# 15228   KiB /ep93xx/libstdc++-dev_4.2.2-r2_ep93xx.ipk
# 1300    KiB /ep93xx/uclibc-dev_0.9.29-r8_ep93xx.ipk
# 140     KiB /armv4t/gettext-dev_0.14.1-r6_armv4t.ipk
# 4       KiB /ep93xx/libgcc-s-dev_4.2.2-r2_ep93xx.ipk

PACKAGES_prepend = " libgettextlib libgettextsrc "

FILES_libgettextlib = "${libdir}/libgettextlib-*.so*"
FILES_libgettextsrc = "${libdir}/libgettextsrc-*.so*"


PACKAGES_prepend = " gettext-runtime gettext-runtime-dev "

FILES_${PN}_append = " ${libdir}/${BPN}/* "

# The its/Makefile.am has defined:
# itsdir = $(pkgdatadir)$(PACKAGE_SUFFIX)/its
# not itsdir = $(pkgdatadir), so use wildcard to match the version.
FILES_${PN}_append = " ${datadir}/${BPN}-*/* "

FILES_gettext-runtime = "       \
    ${bindir}/gettext           \
    ${bindir}/ngettext          \
    ${bindir}/envsubst          \
    ${bindir}/gettext.sh        \
    ${libdir}/libasprintf.so*   \
    ${libdir}/GNU.Gettext.dll   \
    "

FILES_gettext-runtime-dev_append = "    \
    ${libdir}/libasprintf.a             \
    ${includedir}/autosprintf.h         \
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


#======================================#
#                 TEST                 #
#--------------------------------------#
