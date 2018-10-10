#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Very high-quality data compression program"
HOMEPAGE = "http://www.bzip.org/"

LICENSE = "bzip2"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=8;endline=37;md5=40d9d1eb05736d1bfc86cfdd9106e6b2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools relative_symlinks


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "http://www.bzip.org/${PV}/${BP}.tar.gz;name=source           \
           file://fix-bunzip2-qt-returns-0-for-corrupt-archives.patch   \
           file://configure.ac;subdir=${BP}                             \
           file://Makefile.am;subdir=${BP}                              \
           file://CVE-2016-3189.patch                                   \
           "

SRC_URI[source.md5sum] = "00b516f4704d4a7cb50a1d97e6e8e15b"
SRC_URI[source.sha256sum] = "a2848f34fcd5d6cf47def00461fcb528a0484d8edef8208d6d2e2909dc61d9cd"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
CFLAGS_append = " -fPIC -fpic -Winline -fno-strength-reduce -D_FILE_OFFSET_BITS=64 "

#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " libbz2 "

FILES_libbz2 = " ${libdir}/lib*${SOLIBS} "


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#
inherit update-alternatives

ALTERNATIVE_${PN} = " bunzip2 bzcat "


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#


#======================================#
#                 TEST                 #
#--------------------------------------#
