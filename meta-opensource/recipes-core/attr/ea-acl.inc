#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
# this build system is mostly shared by attr and acl


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools-brokensep


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI += " file://relative-libdir.patch;striplevel=0 "

# This patch should be applied after '(attr\|acl)-Missing-configure.ac.patch' 
SRC_URI_append = " file://0001-Added-configure-option-to-enable-disable-static-libr.patch "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS_append = " gettext-native "
DEPENDS_append = " virtual/gettext "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#.
# the package comes with a custom config.h.in, it cannot be
# overwritten by autoheader
EXTRA_AUTORECONF_append = " --exclude=autoheader "
EXTRA_OECONF_append = " INSTALL_USER=root INSTALL_GROUP=root "
EXTRA_OEMAKE_append = " PKG_LIB_DIR=${libdir} PKG_DEVLIB_DIR=${libdir} "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install () {
    oe_runmake install install-lib install-dev DIST_ROOT="${D}"
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " lib${BPN} "

FILES_lib${BPN} = "${libdir}/lib*${SOLIBS}"


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
