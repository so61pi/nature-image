#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Linux control group abstraction library"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools pkgconfig


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${SOURCEFORGE_MIRROR}/project/libcg/libcgroup/v${PV}/libcgroup-${PV}.tar.bz2;name=source"

SRC_URI[source.md5sum] = "3dea9d50b8a5b73ff0bf1cdcb210f63f"
SRC_URI[source.sha256sum] = "e4e38bdc7ef70645ce33740ddcca051248d56b53283c0dc6d404e17706f6fb51"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " bison-native flex-native libpam "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = " --enable-pam-module-dir=${libdir}/security --enable-pam=yes "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    # pam modules in ${base_libdir}/security/ should be binary .so files, not symlinks.
    if [ -f ${D}${libdir}/security/pam_cgroup.so.0.0.0 ]; then
        mv -f ${D}${libdir}/security/pam_cgroup.so.0.0.0 ${D}${libdir}/security/pam_cgroup.so
        rm -f ${D}${libdir}/security/pam_cgroup.so.*
    fi
}



#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " cgroups-pam-plugin "

FILES_${PN}-dev_append = " ${libdir}/security/*.la "
FILES_cgroups-pam-plugin = " ${libdir}/security/pam_cgroup.so* "



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
