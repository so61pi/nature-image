#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Symlink /tmp to /var/tmp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://licenses/COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "file://licenses/COPYING.MIT"
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
	# We only need to create a symlink /tmp to /var/tmp, because
	# - systemd's tmp.mount only mounts tmpfs if /tmp is not a symlink
    # - /var/tmp is already specified in systemd's /usr/lib/tmpfiles.d/tmp.conf
    ln -sr ${D}${localstatedir}/tmp ${D}/tmp
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
