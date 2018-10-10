#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Ext2 Filesystem Utilities"
DESCRIPTION = "The Ext2 Filesystem Utilities (e2fsprogs) contain all of the standard utilities for creating, fixing, configuring , and debugging ext2 filesystems."
HOMEPAGE = "http://e2fsprogs.sourceforge.net/"

LICENSE = "GPLv2 & LGPLv2 & BSD & MIT"
LICENSE_e2fsprogs-e2fsck = "GPLv2"
LICENSE_e2fsprogs-mke2fs = "GPLv2"
LICENSE_e2fsprogs-fsck = "GPLv2"
LICENSE_e2fsprogs-tune2fs = "GPLv2"
LICENSE_e2fsprogs-badblocks = "GPLv2"
LIC_FILES_CHKSUM = "file://NOTICE;md5=b48f21d765b875bd10400975d12c1ca2                                          \
                    file://lib/ext2fs/ext2fs.h;beginline=1;endline=9;md5=596a8dedcb4e731c6b21c7a46fba6bef       \
                    file://lib/e2p/e2p.h;beginline=1;endline=7;md5=8a74ade8f9d65095d70ef2d4bf48e36a             \
                    file://lib/uuid/uuid.h.in;beginline=1;endline=32;md5=dbb8079e114a5f841934b99e59c8820a       \
                    file://lib/uuid/COPYING;md5=58dcd8452651fc8b07d1f65ce07ca8af                                \
                    file://lib/et/et_name.c;beginline=1;endline=11;md5=ead236447dac7b980dbc5b4804d8c836         \
                    file://lib/ss/ss.h;beginline=1;endline=20;md5=6e89ad47da6e75fecd2b5e0e81e1d4a6              \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo pkgconfig


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/people/tytso/e2fsprogs/v${PV}/e2fsprogs-${PV}.tar.xz;name=source"

SRC_URI[source.md5sum] = "543bf56513092e06daaae4ead609b338"
SRC_URI[source.sha256sum] = "2a6367289047d68d9ba6a46cf89ab9a1efd0556cde02a51ebaf414ff51edded9"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext util-linux "


EXTRA_OECONF = "                                            \
    --enable-elf-shlibs                                     \
    --disable-libuuid --disable-uuidd --disable-libblkid    \
    --disable-fsck --disable-fuse2fs                        \
    "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install () {
    oe_runmake 'DESTDIR=${D}' install
    oe_runmake 'DESTDIR=${D}' install-libs
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = "    \
    ${PN}-e2fsck        \
    ${PN}-mke2fs        \
    libcomerr           \
    libss               \
    libe2p              \
    libext2fs           \
    "

FILES_${PN}-e2fsck = "${sbindir}/e2fsck ${sbindir}/fsck.ext*"
FILES_${PN}-mke2fs = "${sbindir}/mke2fs ${sbindir}/mkfs.ext* ${sysconfdir}/mke2fs.conf"
FILES_libcomerr    = "${libdir}/libcom_err${SOLIBS}"
FILES_libss        = "${libdir}/libss${SOLIBS}"
FILES_libe2p       = "${libdir}/libe2p${SOLIBS}"
FILES_libext2fs    = "${libdir}/e2initrd_helper ${libdir}/libext2fs${SOLIBS}"

FILES_${PN}-dev_append = "      \
    ${datadir}/*/*.awk          \
    ${datadir}/*/*.sed          \
    ${bindir}/compile_et        \
    ${bindir}/mk_cmds           \
    "


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
