#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "lizard image extra files"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://os-release                \
           file://hwrevision                \
           file://setup-runrootfs           \
           file://setup-runrootfs.service   \
           "
S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
INHIBIT_DEFAULT_DEPS = "1"


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install () {
    install -d ${D}${libdir}
    install -m 0644 ${WORKDIR}/os-release ${D}${libdir}/os-release

    install -d ${D}${sysconfdir}
    ln -sr ${D}${libdir}/os-release ${D}${sysconfdir}/os-release

    # For swupdate.
    install -m 0644 ${WORKDIR}/hwrevision ${D}${sysconfdir}/hwrevision

    # Create an empty /etc/machine-id
    # systemd will try to bind mount it to /run/machine-id
    # Yes, you can bind mount file to file!
    #
    # Search for machine_id_setup in systemd/src/core/main.c for more info
    install -m 0444 /dev/null ${D}${sysconfdir}/machine-id

    # These symlinks will be fulfilled by setup-runrootfs
    # symlinks are preferred to bind mount
    ln -sr ${D}/run/rootfs/etc/hostname ${D}${sysconfdir}/hostname

    # runrootfs
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/setup-runrootfs ${D}${bindir}/setup-runrootfs

    install -m 0755 -d ${D}${systemd_system_unitdir}/
    install -m 0644 ${WORKDIR}/setup-runrootfs.service  ${D}${systemd_system_unitdir}/setup-runrootfs.service
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
FILES_${PN} = "/"


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#
inherit systemd-static-simple

SYSTEMD_STATIC_PACKAGES = " ${PN} "
SYSTEMD_STATIC_SERVICE_${PN} = " setup-runrootfs.service local-fs.target "


#======================================#
#                 TEST                 #
#--------------------------------------#
