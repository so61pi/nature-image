#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Internationalized Domain Name support library"
HOMEPAGE = "http://www.gnu.org/software/libidn/"

LICENSE = "(LGPLv2.1+ | LGPLv3) & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=df4be47940a91ee69556f5f71eed4aec                 \
                    file://COPYING.LESSERv2;md5=4fbd65380cdd255951079008b364516c        \
                    file://COPYING.LESSERv3;md5=e6a600fd5e1d9cbde2d983680233ad02        \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263               \
                    file://COPYINGv3;md5=d32239bcb673463ab874e80d47fae504               \
                    file://lib/idna.h;endline=21;md5=37cffad24807f446a24de3e7371f20b9   \
                    file://src/idn.c;endline=20;md5=09e97034a8877b3451cb65065fc2c06e    \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit pkgconfig autotools texinfo gtk-doc


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/libidn/libidn-${PV}.tar.gz;name=source                 \
           file://libidn_fix_for_automake-1.12.patch                            \
           file://avoid_AM_PROG_MKDIR_P_warning_error_with_automake_1.12.patch  \
           file://dont-depend-on-help2man.patch                                 \
           file://0001-idn-fix-printf-format-security-warnings.patch            \
           "

SRC_URI[source.md5sum] = "a9aa7e003665de9c82bd3f9fc6ccf308"
SRC_URI[source.sha256sum] = "44a7aab635bb721ceef6beecc4d49dfd19478325e1b47f3196f7d2acc4930e19"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext virtual/libiconv "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = " --disable-csharp "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    rm -rf ${D}${datadir}/emacs
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
# command tool is under GPLv3+, while libidn itself is under LGPLv2.1+ or LGPLv3
# so package command into a separate package
PACKAGES_prepend = " idn "
FILES_idn = " ${bindir}/* "

LICENSE_${PN} = "LGPLv2.1+ | LGPLv3"
LICENSE_idn = "GPLv3+"


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
