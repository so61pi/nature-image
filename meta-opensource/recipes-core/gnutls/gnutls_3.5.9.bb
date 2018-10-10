#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "GNU Transport Layer Security Library"
HOMEPAGE = "http://www.gnu.org/software/gnutls/"

LICENSE = "GPLv3+ & LGPLv2.1+"
LICENSE_${PN} = "LGPLv2.1+"
LICENSE_${PN}-xx = "LGPLv2.1+"
LICENSE_${PN}-bin = "GPLv3+"
LICENSE_${PN}-openssl = "GPLv3+"

LIC_FILES_CHKSUM = "file://LICENSE;md5=71391c8e0c1cfe68077e7fce3b586283             \
                    file://doc/COPYING;md5=d32239bcb673463ab874e80d47fae504         \
                    file://doc/COPYING.LESSER;md5=a6f89e2100d9b6cdffcea4f398e37343  \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools texinfo binconfig pkgconfig lib_package gtk-doc


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "https://www.gnupg.org/ftp/gcrypt/gnutls/v3.5/gnutls-${PV}.tar.xz;name=source     \
           file://correct_rpl_gettimeofday_signature.patch                                  \
           file://0001-configure.ac-fix-sed-command.patch                                   \
           file://use-pkg-config-to-locate-zlib.patch                                       \
           file://arm_eabi.patch                                                            \
           "

SRC_URI[source.md5sum] = "0ab25eb6a1509345dd085bc21a387951"
SRC_URI[source.sha256sum] = "82b10f0c4ef18f4e64ad8cef5dbaf14be732f5095a41cf366b4ecb4050382951"



#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " virtual/gettext nettle gmp virtual/libiconv libunistring "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG ??= " libidn zlib seccomp "

# You must also have CONFIG_SECCOMP enabled in the kernel for
# seccomp to work.
#
PACKAGECONFIG[seccomp] = "ac_cv_libseccomp=yes, ac_cv_libseccomp=no, libseccomp"

PACKAGECONFIG[libidn] = "--with-idn, --without-idn, libidn"
PACKAGECONFIG[libtasn1] = "--with-included-libtasn1=no, --with-included-libtasn1, libtasn1"
PACKAGECONFIG[p11-kit] = "--with-p11-kit, --without-p11-kit, p11-kit"
PACKAGECONFIG[tpm] = "--with-tpm, --without-tpm, trousers"
PACKAGECONFIG[zlib] = "--with-zlib, --without-zlib, zlib"

EXTRA_OECONF = "      \
    --enable-doc      \
    --disable-libdane \
    --disable-guile   \
    --disable-rpath   \
    --enable-local-libopts          \
    --enable-openssl-compatibility  \
    --with-libpthread-prefix=${STAGING_DIR_HOST}${prefix}   \
    --without-libunistring-prefix   \
    "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_configure_prepend() {
    for dir in . lib; do
        rm -f ${dir}/aclocal.m4 ${dir}/m4/libtool.m4 ${dir}/m4/lt*.m4
    done
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " ${PN}-openssl ${PN}-xx "

FILES_${PN}-dev_append = " ${bindir}/gnutls-cli-debug "
FILES_${PN}-openssl = " ${libdir}/libgnutls-openssl.so.* "
FILES_${PN}-xx = " ${libdir}/libgnutlsxx.so.* "


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
