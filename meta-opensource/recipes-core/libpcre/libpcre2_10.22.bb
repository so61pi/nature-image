#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Perl Compatible Regular Expressions version 2"
HOMEPAGE = "http://www.pcre.org"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=ab9633efd38d6f799398df2c248b5aec"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
BINCONFIG = "${bindir}/pcre2-config"

inherit autotools binconfig-disabled


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://ftp.pcre.org/pub/pcre/pcre2-${PV}.tar.bz2;name=source    \
           file://pcre-cross.patch                                          \
           "

SRC_URI[source.md5sum] = "c0c02517938ee2b0d350d53edf450664"
SRC_URI[source.sha256sum] = "b2b44619f4ac6c50ad74c2865fd56807571392496fae1c9ad7a70993d018f416"

S = "${WORKDIR}/pcre2-${PV}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
PROVIDES_append = " pcre2 "
DEPENDS_append = " bzip2 zlib "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = "                \
    --enable-newline-is-lf      \
    --enable-rebuild-chartables \
    --with-link-size=2          \
    --with-match-limit=10000000 \
    "

# Set LINK_SIZE in BUILD_CFLAGS given that the autotools bbclass use it to
# set CFLAGS_FOR_BUILD, required for the libpcre build.
BUILD_CFLAGS_prepend = " -DLINK_SIZE=2 -I${B}/src "
CFLAGS_append = " -D_REENTRANT "
CXXFLAGS_append_powerpc = " -lstdc++"

export CCLD_FOR_BUILD ="${BUILD_CCLD}"


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " pcre2grep pcre2grep-doc pcre2test pcre2test-doc "

FILES_pcre2grep = "${bindir}/pcre2grep"
FILES_pcre2grep-doc = "${mandir}/man1/pcre2grep.1"
FILES_pcre2test = "${bindir}/pcre2test"
FILES_pcre2test-doc = "${mandir}/man1/pcre2test.1"


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
