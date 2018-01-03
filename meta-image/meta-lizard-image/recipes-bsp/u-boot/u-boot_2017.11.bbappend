#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit pythonnative


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI_append = "                              \
    file://u-boot-pylibfdt-native-build.patch   \
    file://crappy-fix-mmc-voltage.patch         \
    file://boot.cmd                             \
    "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS_append = " swig-native python-native "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OEMAKE_append = ' CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TOOLCHAIN_OPTIONS}" V=1 '
EXTRA_OEMAKE_append = ' HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" '
EXTRA_OEMAKE_append = ' STAGING_INCDIR_NATIVE="${STAGING_INCDIR_NATIVE}" STAGING_LIBDIR_NATIVE="${STAGING_LIBDIR_NATIVE}" '
EXTRA_OEMAKE_append = ' HOSTLDSHARED="${BUILD_CC} -shared ${BUILD_LDFLAGS} ${BUILD_CFLAGS}" '


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile_append() {
    ${B}/tools/mkimage -C none -A arm -T script -d ${WORKDIR}/boot.cmd ${WORKDIR}/${UBOOT_ENV_BINARY}
}


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
