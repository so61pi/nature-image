#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
LICENSE = "GPL-2.0+ & LGPL-2.1+"
LICENSE_libkmod = "LGPL-2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343         \
                    file://libkmod/COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools pkgconfig split-completion


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${KERNELORG_MIRROR}/linux/utils/kernel/kmod/kmod-${PV}.tar.xz;name=source"

SRC_URI[source.md5sum] = "08297dfb6f2b3f625f928ca3278528af"
SRC_URI[source.sha256sum] = "610b8d1df172acc39a4fdf1eaa47a57b04873c82f32152e7a62e29b6ff9cb397"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG = " zlib xz "

PACKAGECONFIG[zlib]    = "--with-zlib, --without-zlib, zlib"
PACKAGECONFIG[xz]      = "--with-xz, --without-xz, xz"
PACKAGECONFIG[debug]   = "--enable-debug, --disable-debug"
PACKAGECONFIG[logging] = "--enable-logging, --disable-logging"

EXTRA_AUTORECONF_append = " --force --install --symlink "
EXTRA_OECONF_append     = " --enable-tools --disable-python --disable-manpages "


# The bash path maybe too long which could lead to build errors.
# Use this to limit the length.
CACHED_CONFIGUREVARS_append = " ac_cv_path_DOLT_BASH='/usr/bin/env bash' "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_configure_prepend() {
    touch ${S}/libkmod/docs/gtk-doc.make
}


do_configure_append() {
    sed -i 's#}libtool#}${TARGET_SYS}-libtool#' ${B}/doltlibtool
}


do_install_append() {
    for tool in lsmod insmod rmmod depmod modinfo modprobe; do
        ln -sr ${D}${bindir}/kmod ${D}${bindir}/${tool}
    done
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " libkmod "

FILES_libkmod = " ${libdir}/libkmod*${SOLIBS} ${libdir}/libkmod*${SOLIBS} "


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
