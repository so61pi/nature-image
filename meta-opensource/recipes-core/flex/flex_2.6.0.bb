#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Flex (The Fast Lexical Analyzer)"
HOMEPAGE = "http://sourceforge.net/projects/flex/"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=e4742cf92e89040b39486a6219b68067"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools gettext texinfo


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${SOURCEFORGE_MIRROR}/flex/flex-${PV}.tar.bz2;name=source                \
           file://do_not_create_pdf_doc.patch                                       \
           file://0001-tests-add-a-target-for-building-tests-without-runnin.patch   \
           file://0002-avoid-c-comments-in-c-code-fails-with-gcc-6.patch            \
           file://CVE-2016-6354.patch                                               \
           file://disable-tests.patch                                               \
           "

SRC_URI[source.md5sum] = "266270f13c48ed043d95648075084d59"
SRC_URI[source.sha256sum] = "24e611ef5a4703a191012f80c1027dc9d12555183ce0ecd46f3636e587e9b8e9"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
RDEPENDS_${PN}_append = " m4 "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
M4 = "${bindir}/m4"
M4_class-native = "${STAGING_BINDIR_NATIVE}/m4"

EXTRA_OECONF = " ac_cv_path_M4=${M4} "
EXTRA_OEMAKE = " m4=${STAGING_BINDIR_NATIVE}/m4 "


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#


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
