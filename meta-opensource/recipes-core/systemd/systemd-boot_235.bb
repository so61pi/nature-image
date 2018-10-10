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
SRC_URI = "https://github.com/systemd/systemd/archive/v${PV}.tar.gz;name=source"

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
    -D efi-cc=${CC}                 \
    -D efi-ld=${LD}                 \
    -D efi-libdir=${STAGING_LIBDIR} \
    -D efi-ldsdir=${STAGING_LIBDIR} \
    -D efi-includedir=${STAGING_INCDIR}/efi         \
    -D gnu-efi=true                                 \
    -D man=false                                    \
    -D html=false                                   \
    -D libblkid=true                                \
    "


TARGET_CC_ARCH_append = " ${LDFLAGS} "

C_COMPILER = "${HOST_PREFIX}gcc ${MESON_C_ARGS}"

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

do_write_config() {
    # This needs to be Py to split the args into single-element lists
    cat >${WORKDIR}/meson.cross <<EOF
[binaries]
c = [${@meson_array('C_COMPILER', d)}]
cpp = '${HOST_PREFIX}g++'
ar = '${HOST_PREFIX}ar'
ld = '${HOST_PREFIX}ld'
strip = '${HOST_PREFIX}strip'
readelf = '${HOST_PREFIX}readelf'
pkgconfig = 'pkg-config'

[properties]
needs_exe_wrapper = true
c_args = [${@meson_array('MESON_C_ARGS', d)}]
c_link_args = [${@meson_array('MESON_LINK_ARGS', d)}]
cpp_args = [${@meson_array('MESON_C_ARGS', d)}]
cpp_link_args = [${@meson_array('MESON_LINK_ARGS', d)}]

[host_machine]
system = '${HOST_OS}'
cpu_family = '${@efi_arch(d)}'
cpu = '${HOST_ARCH}'
endian = '${MESON_HOST_ENDIAN}'

[target_machine]
system = '${TARGET_OS}'
cpu_family = '${TARGET_ARCH}'
cpu = '${TARGET_ARCH}'
endian = '${MESON_TARGET_ENDIAN}'
EOF

}


do_install() {
    # Bypass systemd installation with a NOP
    # TODO: What about bootctl?
    :
}


do_deploy () {
    #install ${B}/systemd-boot*.efi ${DEPLOYDIR}
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
