#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI_append = " file://dropbear@.service     \
                   file://dropbearkey.service   \
                   "


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/dropbear@.service   ${D}${systemd_system_unitdir}/dropbear@.service
    install -m 0644 ${WORKDIR}/dropbearkey.service ${D}${systemd_system_unitdir}/dropbearkey.service
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
