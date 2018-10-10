#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Root password"

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
RDEPENDS_${PN} = "base-files"


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
python __anonymous() {
    home = d.getVar("ROOT_HOME")
    if not home:
        bb.fatal("{} : ROOT_HOME is not set".format(d.getVar("FILE", False)))

    shell = d.getVar("ROOT_SHELL")
    if not shell:
        bb.fatal("{} : ROOT_SHELL is not set".format(d.getVar("FILE", False)))
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
ALLOW_EMPTY_${PN} = "1"


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#
inherit useradd-simple

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN}  = " --uid 0 --gid 0 --system --shell ${ROOT_SHELL} --home-dir ${ROOT_HOME} --password '${ROOT_PASSWD}' root; "
GROUPADD_PARAM_${PN} = " --gid 0 root; "


#======================================#
#                 TEST                 #
#--------------------------------------#
