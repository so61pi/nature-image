#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "UNIX Shell similar to the Korn shell"
HOMEPAGE = "http://www.zsh.org"

LICENSE = "zsh"
LIC_FILES_CHKSUM = "file://LICENCE;md5=1a4c4cda3e8096d2fd483ff2f4514fec"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${SOURCEFORGE_MIRROR}/zsh/zsh-${PV}.tar.xz;name=source     \
           file://zprofile                                            \
           file://zshrc                                               \
           file://00-common.zsh                                       \
           file://05-completion.zsh                                   \
           file://10-key-bindings.zsh                                 \
           "
SRC_URI[source.md5sum] = "afba2dfb445a3eb79bd73330fc005ef7"
SRC_URI[source.sha256sum] = "a80b187b6b770f092ea1f53e89021d06c03d8bbe6a5e996bcca3267de14c5e52"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " bison-native groff-native "
DEPENDS_append = " ncurses libcap libpcre gdbm "

RPROVIDES_${PN}_append = " /bin/zsh "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
EXTRA_OECONF = "                                            \
    --enable-etcdir=${sysconfdir}/zsh                       \
    --enable-fndir=${datadir}/${BPN}/${PV}/functions        \
    --enable-site-fndir=${datadir}/${BPN}/site-functions    \
                                                            \
    --with-term-lib='ncursesw ncurses'                      \
    --with-tcsetpgrp                                        \
    --enable-cap                                            \
    --enable-multibyte                                      \
    --enable-gdbm                                           \
                                                            \
    --disable-dynamic                                       \
    "

# prevent autotools.bbclass from removing the correct aclocal.m4
EXTRA_AUTORECONF_append = " --exclude=aclocal "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    # /etc/zsh/zshrc.d
    install -d ${D}${sysconfdir}/zsh/zshrc.d
    install -m 0644 ${WORKDIR}/zprofile     ${D}${sysconfdir}/zsh/zprofile
    install -m 0644 ${WORKDIR}/zshrc        ${D}${sysconfdir}/zsh/zshrc
    install -m 0644 ${WORKDIR}/00-common.zsh            ${D}${sysconfdir}/zsh/zshrc.d/00-common.zsh
    install -m 0644 ${WORKDIR}/05-completion.zsh        ${D}${sysconfdir}/zsh/zshrc.d/05-completion.zsh
    install -m 0644 ${WORKDIR}/10-key-bindings.zsh      ${D}${sysconfdir}/zsh/zshrc.d/10-key-bindings.zsh
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = "        \
    ${PN}-startup           \
    ${PN}-help              \
    ${PN}-completion        \
    "

FILES_${PN}-startup     = "${sysconfdir}"
FILES_${PN}-help        = "${datadir}/zsh/${PV}/help"
FILES_${PN}-completion  = "${datadir}/zsh/${PV}/functions ${datadir}/zsh/site-functions"

RDEPENDS_${PN}-startup    = " ${PN} ${PN}-completion "
RDEPENDS_${PN}-help       = " ${PN} "
RDEPENDS_${PN}-completion = " ${PN} "


#======================================#
#            INSTALL SCRIPT            #
#--------------------------------------#
pkg_postinst_${PN} () {
    touch $D${sysconfdir}/shells
    echo ${bindir}/zsh >> $D${sysconfdir}/shells
}


#======================================#
#             ALTERNATIVES             #
#--------------------------------------#


#======================================#
#           USERADD/SYSTEMD            #
#--------------------------------------#


#======================================#
#                 TEST                 #
#--------------------------------------#
