#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "The basic file, shell and text manipulation utilities"
HOMEPAGE = "http://www.gnu.org/software/coreutils/"

LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504								\
                    file://src/ls.c;beginline=5;endline=16;md5=38b79785ca88537b75871782a2a3c6b8		\
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo utility


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/coreutils/coreutils-${PV}.tar.xz;name=source 	\
           file://disable-manpages.patch								\
		   "

SRC_URI[source.md5sum] = "e7cb20d0572cc40d9f47ede6454406d1"
SRC_URI[source.sha256sum] = "1117b1a16039ddd84d51a9923948307cfa28c2cea03d1a2438742253df0a0c65"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext gmp "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG ??= " libcap "

PACKAGECONFIG[acl] = "--enable-acl, --disable-acl, acl"
PACKAGECONFIG[xattr] = "--enable-xattr, --disable-xattr, attr"
PACKAGECONFIG[libcap] = "--enable-libcap, --disable-libcap, libcap"
PACKAGECONFIG[libsmack] = "--enable-libsmack, --disable-libsmack, smack"

EXTRA_OECONF = "                        \
    --enable-threads=posix              \
    "


#======================================#
#                TASKS                 #
#--------------------------------------#
PACKAGES_prepend = "        \
    ${PN}-install        	\
    ${PN}-kill              \
    ${PN}-uptime            \
    "

FILES_${PN}-install     = "${@binpath(d, 'install')}"
FILES_${PN}-kill        = "${@binpath(d, 'kill')}"
FILES_${PN}-uptime      = "${@binpath(d, 'uptime')}"


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
