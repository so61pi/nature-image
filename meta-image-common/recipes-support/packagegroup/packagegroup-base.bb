#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Simplified version of oe-core packagegroup-base.bb"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit packagegroup


#======================================#
#                SOURCE                #
#--------------------------------------#


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
PROVIDES = "${PACKAGES}"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES = "                    \
    packagegroup-base-simple    \
    packagegroup-distro-base    \
    packagegroup-machine-base   \
    "

RDEPENDS_packagegroup-base-simple = "   \
    packagegroup-distro-base            \
    packagegroup-machine-base           \
    "

#
# packages added by distribution
#
SUMMARY_packagegroup-distro-base = "${DISTRO} extras"
DEPENDS_packagegroup-distro-base = "${DISTRO_EXTRA_DEPENDS}"
RDEPENDS_packagegroup-distro-base = "${DISTRO_EXTRA_RDEPENDS}"
RRECOMMENDS_packagegroup-distro-base = "${DISTRO_EXTRA_RRECOMMENDS}"

#
# packages added by machine config
#
SUMMARY_packagegroup-machine-base = "${MACHINE} extras"
RDEPENDS_packagegroup-machine-base = "${MACHINE_EXTRA_RDEPENDS}"
RRECOMMENDS_packagegroup-machine-base = "${MACHINE_EXTRA_RRECOMMENDS}"


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
