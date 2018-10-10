#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Perl Compatible Regular Expressions"
HOMEPAGE = "http://www.pcre.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=60da32d84d067f53e22071c4ecb4384d"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
BINCONFIG = "${bindir}/pcre-config"

inherit autotools binconfig-disabled


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/pcre-${PV}.tar.bz2;name=source \
           file://pcre-cross.patch                                                              \
           file://fix-pcre-name-collision.patch                                                 \
           file://Makefile                                                                      \
           "

SRC_URI[source.md5sum] = "41a842bf7dcecd6634219336e2167d1d"
SRC_URI[source.sha256sum] = "00e27a29ead4267e3de8111fcaa59b132d0533cdfdbdddf4b0604279acbcf4f4"

S = "${WORKDIR}/pcre-${PV}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
PROVIDES_append = " pcre "
DEPENDS_append = " bzip2 zlib "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = "                \
    --enable-newline-is-lf      \
    --enable-rebuild-chartables \
    --enable-utf                \
    --with-link-size=2          \
    --with-match-limit=10000000 \
    "

# Set LINK_SIZE in BUILD_CFLAGS given that the autotools bbclass use it to
# set CFLAGS_FOR_BUILD, required for the libpcre build.
BUILD_CFLAGS_prepend = " -DLINK_SIZE=2 -I${B} "
CFLAGS_append = " -D_REENTRANT "
CXXFLAGS_append_powerpc = " -lstdc++"

export CCLD_FOR_BUILD ="${BUILD_CCLD}"


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGECONFIG ??= "pcre8 unicode-properties"

PACKAGECONFIG[pcre8] = "--enable-pcre8, --disable-pcre8"
PACKAGECONFIG[pcre16] = "--enable-pcre16, --disable-pcre16"
PACKAGECONFIG[pcre32] = "--enable-pcre32, --disable-pcre32"
PACKAGECONFIG[pcretest-readline] = "--enable-pcretest-libreadline, --disable-pcretest-libreadline, readline"
PACKAGECONFIG[unicode-properties] = "--enable-unicode-properties, --disable-unicode-properties"

PACKAGES_prepend = " libpcrecpp libpcreposix pcregrep pcregrep-doc pcretest pcretest-doc "

FILES_libpcrecpp = "${libdir}/libpcrecpp.so.*"
FILES_libpcreposix = "${libdir}/libpcreposix.so.*"
FILES_pcregrep = "${bindir}/pcregrep"
FILES_pcregrep-doc = "${mandir}/man1/pcregrep.1"
FILES_pcretest = "${bindir}/pcretest"
FILES_pcretest-doc = "${mandir}/man1/pcretest.1"


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
