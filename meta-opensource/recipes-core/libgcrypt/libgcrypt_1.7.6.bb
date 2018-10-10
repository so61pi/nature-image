#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "General purpose cryptographic library based on the code from GnuPG"
HOMEPAGE = "http://directory.fsf.org/project/libgcrypt/"

# helper program gcryptrnd and getrandom are under GPL, rest LGPL
LICENSE = "GPLv2+ & LGPLv2.1+ & GPLv3+"
LICENSE_${PN} = "LGPLv2.1+"
LICENSE_${PN}-dev = "GPLv2+ & LGPLv2.1+"
LICENSE_dumpsexp-dev = "GPLv3+"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f     \
                    file://COPYING.LIB;md5=bbb461211a33b134d42ed5ee802b37ff \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
BINCONFIG = "${bindir}/libgcrypt-config"

inherit autotools texinfo binconfig-disabled pkgconfig


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${GNUPG_MIRROR}/libgcrypt/libgcrypt-${PV}.tar.gz;name=source         \
           file://add-pkgconfig-support.patch                                   \
           file://libgcrypt-fix-building-error-with-O2-in-sysroot-path.patch    \
           file://fix-ICE-failure-on-mips-with-option-O-and-g.patch             \
           file://fix-undefined-reference-to-pthread.patch                      \
           file://0001-ecc-Store-EdDSA-session-key-in-secure-memory.patch       \
           file://CVE-2017-7526.patch                                           \
           "

SRC_URI[source.md5sum] = "eac6d11999650e8a1493674c1bdbc7f8"
SRC_URI[source.sha256sum] = "fc0aec7714d75d812b665bd510d66031b1b2ce8fa855cc2c02238c954ea36982"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS = " libgpg-error "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG ??= " capabilities "
PACKAGECONFIG[capabilities] = "--with-capabilities, --without-capabilities, libcap"

EXTRA_OECONF = " --disable-asm "

ARM_INSTRUCTION_SET = " arm "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_configure_prepend () {
    # Else this could be used in preference to the one in aclocal-copy
    rm -f ${S}/m4/gpg-error.m4
}


# libgcrypt.pc is added locally and thus installed here
do_install_append() {
    install -d ${D}/${libdir}/pkgconfig
    install -m 0644 ${B}/src/libgcrypt.pc ${D}/${libdir}/pkgconfig/
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " dumpsexp-dev "

FILES_${PN}-dev_append = " ${bindir}/hmac256 "
FILES_dumpsexp-dev_append = " ${bindir}/dumpsexp "


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
