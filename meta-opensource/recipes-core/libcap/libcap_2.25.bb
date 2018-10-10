#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Library for getting/setting POSIX.1e capabilities"
HOMEPAGE = "http://sites.google.com/site/fullycapable/"

# no specific GPL version required
LICENSE = "BSD | GPLv2"
LIC_FILES_CHKSUM = "file://License;md5=3f84fd6f29d453a56514cb7e4ead25f1"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit lib_package


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${KERNELORG_MIRROR}/linux/libs/security/linux-privs/libcap2/libcap-${PV}.tar.xz;name=source  \
           file://0001-ensure-the-XATTR_NAME_CAPS-is-defined-when-it-is-use.patch                       \
           file://0001-Fix-build-with-gperf-3.1.patch                                                   \
           "

SRC_URI[source.md5sum] = "6666b839e5d46c2ad33fc8aa2ceb5f77"
SRC_URI[source.sha256sum] = "693c8ac51e983ee678205571ef272439d83afe62dd8e424ea14ad9790bc35162"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " hostperl-runtime-native gperf-native libpam "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OEMAKE = "                            \
    INDENT=                                 \
    lib=${@os.path.basename('${libdir}')}   \
    RAISE_SETFCAP=no                        \
    DYNAMIC=yes                             \
    SYSTEM_HEADERS=${STAGING_INCDIR}        \
    PAM_CAP=yes                             \
    "

CFLAGS_append = " -D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64 "


#======================================#
#                TASKS                 #
#--------------------------------------#
# do NOT pass target cflags to host compilations
#
do_configure() {
    # libcap uses := for compilers, fortunately, it gives us a hint
    # on what should be replaced with ?=
    sed -e 's,:=,?=,g' -i Make.Rules
    sed -e 's,^BUILD_CFLAGS ?= $(.*CFLAGS),BUILD_CFLAGS := $(BUILD_CFLAGS),' -i Make.Rules

    # disable gperf detection
    sed -e '/shell gperf/cifeq (,yes)' -i libcap/Makefile
}


do_compile() {
    oe_runmake ${PACKAGECONFIG_CONFARGS}
}


do_install() {
    oe_runmake install \
        ${PACKAGECONFIG_CONFARGS} \
        DESTDIR="${D}" \
        prefix="${prefix}" \
        SBINDIR="${sbindir}"
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
FILES_${PN}-dev_append = " ${libdir}/*.so "

# pam files
FILES_${PN}_append = " ${libdir}/security/*.so "


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
