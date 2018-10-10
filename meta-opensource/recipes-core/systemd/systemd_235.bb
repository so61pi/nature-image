#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "systemd"
DESCRIPTION = "systemd"

LICENSE = "GPLv2 & bzip2"
LIC_FILES_CHKSUM = "file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit meson split-completion qemu


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://github.com/systemd/systemd/archive/v${PV}.tar.gz;name=source"

SRC_URI[source.md5sum] = "d53a925f1ca5b2e124de0a8aa65d0db2"
SRC_URI[source.sha256sum] = "25811f96f5a027bf2a4c9383495cf5b623e385d84da31e473cf375932b3e9c52"


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
    kmod                \
    curl                \
    libgcrypt           \
    libcap              \
    libcgroup           \
    libutil-linux       \
    gnutls              \
    elfutils            \
    acl                 \
    libseccomp          \
    "

RDEPENDS_${PN}        = " kmod dbus less "
RDEPENDS_${PN}_append = " util-linux-agetty util-linux-sulogin util-linux-mount "
RDEPENDS_${PN}_append = " pam-plugin-keyinit "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG ??= " zlib bzip2 xz lz4 pam idn-libidn utmp "

PACKAGECONFIG[utmp] = "-Dutmp=true, -Dutmp=false"

PACKAGECONFIG[remote]   = "-Dremote=true -Dmicrohttpd=true, -Dremote=false -Dmicrohttpd=false, libmicrohttpd"

PACKAGECONFIG[localed]  = "-Dlocaled=true, -Dlocaled=false"
PACKAGECONFIG[xkbcommon] = "-Dxkbcommon=true, -Dxkbcommon=false, libxkbcommon"

PACKAGECONFIG[zlib] = "-Dzlib=true, -Dzlib=false, zlib"
PACKAGECONFIG[bzip2] = "-Dbzip2=true, -Dbzip2=false, bzip2"
PACKAGECONFIG[xz] = "-Dxz=true, -Dxz=false, xz"
PACKAGECONFIG[lz4] = "-Dlz4=true, -Dlz4=false, lz4"

PACKAGECONFIG[pam] = "-Dpam=true, -Dpam=false, libpam"

PACKAGECONFIG[idn-libidn] = "-Didn=true -Dlibidn=true, -Didn=false, libidn"
PACKAGECONFIG[idn-libidn2] = "-Didn=true -Dlibidn2=true, -Didn=false, libidn2"

PACKAGECONFIG[machined] = "-Dmachined=true, -Dmachined=false"
PACKAGECONFIG[importd] = "-Dimportd=true, -Dimportd=false, zlib bzip2 xz"

# quotaon-path and quotacheck-path will be auto detected
PACKAGECONFIG[quotacheck] = "-Dquotacheck=true, -Dquotacheck=false, quota"

# loadkeys-path and setfont-path will be auto detected
PACKAGECONFIG[vconsole] = "-Dvconsole=true, -Dvconsole=false"

# kexec-path will be auto detected
PACKAGECONFIG[kexec] = ", , kexec-tools"

# Security
PACKAGECONFIG[apparmor] = "-Dapparmor=true, -Dapparmor=false"
PACKAGECONFIG[ima] = "-Dima=true, -Dima=false"
PACKAGECONFIG[audit] = "-Daudit=true, -Daudit=false, audit"
PACKAGECONFIG[tpm] = "-Dtpm=true, -Dtpm=false"
PACKAGECONFIG[smack] = "-Dsmack=true, -Dsmack=false"

# We could have /var/lib/systemd/random-seed link to a file on 
# non-volatile storage to save the seed.
#
# Read systemd/src/random-seed/random-seed.c for more info.
PACKAGECONFIG[randomseed] = "-Drandomseed=true, -Drandomseed=false"

PACKAGECONFIG[libcryptsetup] = "-Dlibcryptsetup=true, -Dlibcryptsetup=false, cryptsetup"
PACKAGECONFIG[qrencode] = "-Dqrencode=true, -Dqrencode=false, qrencode"
PACKAGECONFIG[libiptc] = "-Dlibiptc=true, -Dlibiptc=false, iptables"

# Configs that are rarely used
PACKAGECONFIG[rfkill] = "-Drfkill=true, -Drfkill=false"
PACKAGECONFIG[ldconfig] = "-Dldconfig=true, -Dldconfig=false"
PACKAGECONFIG[sysusers] = "-Dsysusers=true, -Dsysusers=false"


EXTRA_OEMESON_append = "                    \
    -D split-usr=false                      \
                                            \
    -D sysvinit-path=''                     \
    -D sysvrcnd-path=''                     \
                                            \
    -D telinit-path='/usr/lib/sysvinit/telinit'     \
    -D quotaon-path='/usr/sbin/quotaon'             \
    -D quotacheck-path='/usr/sbin/quotacheck'       \
    -D kill-path='/usr/bin/kill'                    \
    -D kmod-path='/usr/bin/kmod'                    \
    -D kexec-path='/usr/sbin/kexec'                 \
    -D sulogin-path='/usr/sbin/sulogin'             \
    -D mount-path='/usr/bin/mount'                  \
    -D umount-path='/usr/bin/umount'                \
    -D loadkeys-path='/usr/bin/loadkeys'            \
    -D setfont-path='/usr/bin/setfont'              \
                                            \
    -D hibernate=false                      \
    -D resolve=true                         \
    -D environment-d=true                   \
    -D binfmt=false                         \
    -D coredump=true                        \
    -D logind=true                          \
    -D hostnamed=true                       \
    -D networkd=true                        \
    -D timedated=true                       \
    -D timesyncd=true                       \
    -D firstboot=true                       \
                                            \
    -D nss-systemd=true                     \
                                            \
    -D backlight=false                      \
    -D tmpfiles=true                        \
    -D hwdb=false                           \
                                            \
    -D man=false                            \
    -D html=false                           \
                                            \
    -D rpmmacrosdir=no                      \
                                            \
    -D system-uid-max=999                   \
    -D system-gid-max=999                   \
    -D adm-group=false                      \
    -D wheel-group=false                    \
    -D default-kill-user-processes=true     \
    -D gshadow=false                        \
                                            \
    -D selinux=false                        \
    -D smack=false                          \
    -D polkit=false                         \
    -D seccomp=true                         \
                                            \
    -D libblkid=true                        \
    -D kmod=true                            \
    -D libcurl=true                         \
    -D gcrypt=true                          \
    -D gnutls=true                          \
    -D elfutils=true                        \
    -D glib=false                           \
    -D dbus=false                           \
    -D acl=true                             \
    -D efi=false                            \
                                            \
    -D bashcompletiondir=${datadir}/bash-completion/completions     \
    -D zshcompletiondir=${datadir}/zsh/site-functions               \
                                            \
    -D tests=true                           \
    -D slow-tests=false                     \
    -D install-tests=false                  \
    "

MESON_LINK_ARGS = "${MESON_C_ARGS} ${TARGET_LDFLAGS}"

TARGET_CC_ARCH_append = " ${TARGET_LDFLAGS} "

C_COMPILER = "${HOST_PREFIX}gcc ${MESON_C_ARGS}"


#======================================#
#                TASKS                 #
#--------------------------------------#
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
cpu_family = '${HOST_ARCH}'
cpu = '${HOST_ARCH}'
endian = '${MESON_HOST_ENDIAN}'

[target_machine]
system = '${TARGET_OS}'
cpu_family = '${TARGET_ARCH}'
cpu = '${TARGET_ARCH}'
endian = '${MESON_TARGET_ENDIAN}'
EOF

}


do_install_append() {
    install -d ${D}${sysconfdir}
    ln -sr ${D}${libdir}/systemd/resolv.conf ${D}${sysconfdir}/resolv.conf

    # Temporarily remove kernel-install files as they use bashisms
    rm ${D}${libdir}/kernel/install.d/50-depmod.install
    rm ${D}${libdir}/kernel/install.d/90-loaderentry.install
    rm ${D}${bindir}/kernel-install
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
FILES_${PN} = "/"


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#
PACKAGE_WRITE_DEPS_append = " qemu-native "
pkg_postinst_${PN} () {
    ${@qemu_run_binary(d, '$D', '${bindir}/udevadm')} hwdb --update --root $D
    chown root:root $D${sysconfdir}/udev/hwdb.bin
}


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#
inherit update-alternatives

ALTERNATIVE_${PN} = " init halt reboot shutdown poweroff "

ALTERNATIVE_TARGET[init] = "${libdir}/systemd/systemd"
ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"

ALTERNATIVE_TARGET[halt] = "${bindir}/systemctl"
ALTERNATIVE_LINK_NAME[halt] = "${sbindir}/halt"

ALTERNATIVE_TARGET[reboot] = "${bindir}/systemctl"
ALTERNATIVE_LINK_NAME[reboot] = "${sbindir}/reboot"

ALTERNATIVE_TARGET[shutdown] = "${bindir}/systemctl"
ALTERNATIVE_LINK_NAME[shutdown] = "${sbindir}/shutdown"

ALTERNATIVE_TARGET[poweroff] = "${bindir}/systemctl"
ALTERNATIVE_LINK_NAME[poweroff] = "${sbindir}/poweroff"


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#
inherit useradd-simple

USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN}        = " --system --user-group --shell /bin/false --password '*' systemd-resolve; "
USERADD_PARAM_${PN}_append = " --system --user-group --shell /bin/false --password '*' systemd-timesync; "
USERADD_PARAM_${PN}_append = " --system --user-group --shell /bin/false --password '*' systemd-network; "
GROUPADD_PARAM_${PN}        = " --system systemd-journal; "
GROUPADD_PARAM_${PN}_append = " --system utmp; "

# udevd groups
GROUPADD_PARAM_${PN}_append = " --system audio; "
GROUPADD_PARAM_${PN}_append = " --system cdrom; "
GROUPADD_PARAM_${PN}_append = " --system dialout; "
GROUPADD_PARAM_${PN}_append = " --system disk; "
GROUPADD_PARAM_${PN}_append = " --system input; "
GROUPADD_PARAM_${PN}_append = " --system kmem; "
GROUPADD_PARAM_${PN}_append = " --system kvm; "
GROUPADD_PARAM_${PN}_append = " --system lp; "
GROUPADD_PARAM_${PN}_append = " --system tape; "
GROUPADD_PARAM_${PN}_append = " --system tty; "
GROUPADD_PARAM_${PN}_append = " --system video; "


#======================================#
#                 TEST                 #
#--------------------------------------#
