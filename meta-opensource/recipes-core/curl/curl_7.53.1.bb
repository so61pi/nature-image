#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "Command line tool and library for client-side URL transfers"
HOMEPAGE = "http://curl.haxx.se/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;beginline=8;md5=3a34942f4ae3fbf1a303160714e664ac"


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2;name=source  \
           file://0001-replace-krb5-config-with-pkg-config.patch        \
           "

# curl likes to set -g0 in CFLAGS, so we stop it
# from mucking around with debug options
#
SRC_URI_append = " file://configure_ac.patch      \
                   file://CVE-2017-1000100.patch  \
                   file://CVE-2017-1000101.patch  \
                   "

SRC_URI[source.md5sum] = "fb1f03a142236840c1a77c035fa4c542"
SRC_URI[source.sha256sum] = "1c7207c06d75e9136a944a2e0528337ce76f15b9ec9ae4bb30d703b59bf530e8"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
CVE_PRODUCT = "libcurl"
inherit autotools pkgconfig binconfig multilib_header


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'ipv6', d)} gnutls proxy threaded-resolver zlib"

PACKAGECONFIG[dict] = "--enable-dict,--disable-dict,"
PACKAGECONFIG[gnutls] = "--with-gnutls,--without-gnutls,gnutls"
PACKAGECONFIG[gopher] = "--enable-gopher,--disable-gopher,"
PACKAGECONFIG[imap] = "--enable-imap,--disable-imap,"
PACKAGECONFIG[ipv6] = "--enable-ipv6,--disable-ipv6,"
PACKAGECONFIG[ldap] = "--enable-ldap,--disable-ldap,"
PACKAGECONFIG[ldaps] = "--enable-ldaps,--disable-ldaps,"
PACKAGECONFIG[libidn] = "--with-libidn,--without-libidn,libidn"
PACKAGECONFIG[libssh2] = "--with-libssh2,--without-libssh2,libssh2"
PACKAGECONFIG[pop3] = "--enable-pop3,--disable-pop3,"
PACKAGECONFIG[proxy] = "--enable-proxy,--disable-proxy,"
PACKAGECONFIG[rtmpdump] = "--with-librtmp,--without-librtmp,rtmpdump"
PACKAGECONFIG[rtsp] = "--enable-rtsp,--disable-rtsp,"
PACKAGECONFIG[smb] = "--enable-smb,--disable-smb,"
PACKAGECONFIG[smtp] = "--enable-smtp,--disable-smtp,"
PACKAGECONFIG[ssl] = "--with-ssl --with-random=/dev/urandom,--without-ssl,openssl"
PACKAGECONFIG[telnet] = "--enable-telnet,--disable-telnet,"
PACKAGECONFIG[tftp] = "--enable-tftp,--disable-tftp,"
PACKAGECONFIG[threaded-resolver] = "--enable-threaded-resolver,--disable-threaded-resolver"
PACKAGECONFIG[zlib] = "--with-zlib=${STAGING_LIBDIR}/../,--without-zlib,zlib"
PACKAGECONFIG[krb5] = "--with-gssapi,--without-gssapi,krb5"

EXTRA_OECONF = "            \
    --enable-crypto-auth    \
    --with-ca-bundle=${sysconfdir}/ssl/certs/ca-certificates.crt    \
    --without-libmetalink   \
    --without-libpsl        \
    --without-nghttp2       \
    "


#======================================#
#                TASKS                 #
#--------------------------------------#
do_install_append() {
    oe_multilib_header curl/curlbuild.h

    # cleanup buildpaths from curl-config
    sed -i -e 's,${STAGING_DIR_HOST},,g' ${D}${bindir}/curl-config
}


#======================================#
#            PACKAGES/FILES            #
#--------------------------------------#
PACKAGES_prepend = " lib${BPN} "

FILES_${PN}_append = " ${datadir}/zsh "
FILES_lib${BPN} = " ${libdir}/lib*.so.* "


RRECOMMENDS_lib${BPN}_append = " ca-certificates "


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
