#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "systemd-boot"
DESCRIPTION = "systemd-boot"

LICENSE = "GPLv2 & bzip2"
LIC_FILES_CHKSUM = "file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit meson
inherit deploy


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://github.com/systemd/systemd/archive/v${PV}.tar.gz;name=source     \
           file://0001-missing-system-calls.patch                                   \
           file://0002-fix-efi-meson-build.patch                                    \
           "

SRC_URI[source.md5sum] = "d53a925f1ca5b2e124de0a8aa65d0db2"
SRC_URI[source.sha256sum] = "25811f96f5a027bf2a4c9383495cf5b623e385d84da31e473cf375932b3e9c52"

S = "${WORKDIR}/systemd-235"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = "             \
    libxslt-native      \
    intltool-native     \
    gperf-native        \
    gettext-native      \
    "

DEPENDS_append = "      \
    virtual/gettext     \
    libutil-linux       \
    libcap              \
    gnu-efi             \
    "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OEMESON = "                   \
    -D efi=true                     \
    -D efi-cc='${CC}'               \
    -D efi-ld='${LD}'               \
    -D efi-libdir=${STAGING_LIBDIR} \
    -D efi-ldsdir=${STAGING_LIBDIR} \
    -D efi-includedir=${STAGING_INCDIR}/efi         \
    -D gnu-efi=true                                 \
    -D man=false                                    \
    -D html=false                                   \
    -D libblkid=true                                \
    "

TUNE_CCARGS_remove = "-mfpmath=sse"
COMPATIBLE_HOST = "(x86_64.*|i.86.*)-linux"


#======================================#
#                TASKS                 #
#--------------------------------------#
def efi_arch(d):
    import re
    tarch = d.getVar("HOST_ARCH")
    if re.match("i[3456789]86", tarch):
        return "x86"
    return tarch


do_install() {
    # Bypass systemd installation with a NOP
    # TODO: What about bootctl?
    :
}


do_deploy () {
    install ${B}/src/boot/efi/systemd-boot*.efi ${DEPLOYDIR}
}


addtask deploy before do_build after do_compile


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
