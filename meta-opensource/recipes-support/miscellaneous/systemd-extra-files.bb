#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Some files for systemd"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://80-dhcp.network                   \
           file://60-persistent-storage-ide.rules   \
           file://legacy.lock.conf                  \
           file://licenses/COPYING.MIT              \
           "
S = "${WORKDIR}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
RDEPENDS_${PN} = " systemd "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install() {
    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${WORKDIR}/80-dhcp.network ${D}${systemd_unitdir}/network/80-dhcp.network

    install -d ${D}${libdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/60-persistent-storage-ide.rules ${D}${libdir}/udev/rules.d/60-persistent-storage-ide.rules

    install -d ${D}${libdir}/tmpfiles.d
    install -m 0644 ${WORKDIR}/legacy.lock.conf ${D}${libdir}/tmpfiles.d/legacy.lock.conf
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


#======================================#
#                 TEST                 #
#--------------------------------------#
