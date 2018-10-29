#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "U-Boot bootloader fw_printenv/setenv utilities"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
require u-boot-common_${PV}.inc
inherit uboot-config


#======================================#
#                SOURCE                #
#--------------------------------------#


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
BBCLASSEXTEND = "cross"

INSANE_SKIP_${PN} = "already-stripped"
EXTRA_OEMAKE_class-target = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" V=1'
EXTRA_OEMAKE_class-cross = 'HOSTCC="${CC} ${CFLAGS} ${LDFLAGS}" V=1'


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile () {
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake envtools
}


do_install () {
    install -d ${D}${base_sbindir}
    install -d ${D}${sysconfdir}
    install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
    install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv
    install -m 0644 ${S}/tools/env/fw_env.config ${D}${sysconfdir}/fw_env.config
}


do_install_class-cross () {
    install -d ${D}${bindir_cross}
    install -m 755 ${S}/tools/env/fw_printenv ${D}${bindir_cross}/fw_printenv
    install -m 755 ${S}/tools/env/fw_printenv ${D}${bindir_cross}/fw_setenv
}


SYSROOT_DIRS_append_class-cross = " ${bindir_cross}"


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGE_ARCH = "${MACHINE_ARCH}"


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
