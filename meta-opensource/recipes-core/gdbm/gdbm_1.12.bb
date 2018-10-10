#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Key/value database library with extensible hashing"
HOMEPAGE = "http://www.gnu.org/software/gdbm/"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=241da1b9fe42e642cbb2c24d5e0c4d24"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools gettext texinfo lib_package


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNU_MIRROR}/gdbm/gdbm-${PV}.tar.gz;name=source"

SRC_URI[source.md5sum] = "9ce96ff4c99e74295ea19040931c8fb9"
SRC_URI[source.sha256sum] = "d97b2166ee867fd6ca5c022efee80702d6f30dd66af0e03ed092285c3af9bcea"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
# Needed for dbm python module
EXTRA_OECONF = "-enable-libgdbm-compat"

# Stop presence of dbm/nbdm on the host contaminating builds
CACHED_CONFIGUREVARS_append = " ac_cv_lib_ndbm_main=no ac_cv_lib_dbm_main=no "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append () {
    # Create a symlink to ndbm.h and gdbm.h in include/gdbm to let other packages to find
    # these headers
    install -d ${D}${includedir}/gdbm
    ln -sf ../ndbm.h ${D}/${includedir}/gdbm/ndbm.h
    ln -sf ../gdbm.h ${D}/${includedir}/gdbm/gdbm.h
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " ${PN}-compat "
FILES_${PN}-compat = "${libdir}/libgdbm_compat${SOLIBS}"


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
