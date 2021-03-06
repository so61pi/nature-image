#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
require util-linux-common.inc
inherit no-completion


#======================================#
#                SOURCE                #
#--------------------------------------#
MAJORVER = "2.31"
SRC_URI[source.md5sum] = "5b6821c403c3cc6e7775f74df1882a20"
SRC_URI[source.sha256sum] = "f9be7cdcf4fc5c5064a226599acdda6bdf3d86c640152ba01ea642d91108dc8a"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF_append = "                 \
    --disable-all-programs              \
    --enable-libuuid                    \
    --enable-libuuid-force-uuidd        \
    --enable-libblkid                   \
    --enable-libmount                   \
    --disable-libmount-support-mtab     \
    --enable-libsmartcols               \
    --enable-libfdisk                   \
    "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    rmdir ${D}${bindir} ${D}${sbindir}
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
