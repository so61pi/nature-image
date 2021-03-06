#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI_append = " file://fw_env.config "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_compile () {
    # 2017.11 has problem with envtools (generated/timestamp_autogenerated.h is not generated),
    # temporarily use tools-only to gen it for now.
    oe_runmake ${UBOOT_MACHINE}
    oe_runmake tools-only
    oe_runmake envtools
}


do_install_append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config

    install -d ${D}${libdir}
    install -m 0644 ${S}/tools/env/lib.a ${D}${libdir}/libubootenv.a
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
