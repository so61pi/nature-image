#======================================#
#           SUMMARY/LICENCE            #
#--------------------------------------#
SUMMARY = "A suite of basic system administration utilities"

LICENSE = "GPLv2+ & LGPLv2.1+ & BSD"
LIC_FILES_CHKSUM = "file://README.licensing;md5=1715f5ee3e01203ca1e1e0b9ee65918c                        \
                    file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263                                 \
                    file://Documentation/licenses/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263    \
                    file://Documentation/licenses/COPYING.LGPLv2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://Documentation/licenses/COPYING.BSD-3;md5=58dcd8452651fc8b07d1f65ce07ca8af    \
                    file://Documentation/licenses/COPYING.UCB;md5=263860f8968d8bafa5392cab74285262      \
                    file://libuuid/COPYING;md5=b442ffb762cf8d3e9df1b99e0bb4af70                         \
                    file://libmount/COPYING;md5=fb93f01d4361069c5616327705373b16                        \
                    file://libblkid/COPYING;md5=fb93f01d4361069c5616327705373b16                        \
                    "


#======================================#
#           REQUIRE/INHERIT            #
#--------------------------------------#
inherit autotools pkgconfig split-completion


#======================================#
#                SOURCE                #
#--------------------------------------#
SRC_URI = "${KERNELORG_MIRROR}/linux/utils/util-linux/v${MAJORVER}/util-linux-${PV}.tar.xz;name=source"

S = "${WORKDIR}/util-linux-${PV}"


#======================================#
#           DEPENDS/PROVIDES           #
#--------------------------------------#
DEPENDS        = " gettext-native "
DEPENDS_append = " zlib virtual/gettext "


#======================================#
#         BUILD CONFIGURATIONS         #
#--------------------------------------#
PACKAGECONFIG[cramfs] = "--enable-cramfs, --disable-cramfs"
PACKAGECONFIG[bfs]    = "--enable-bfs,    --disable-bfs"
PACKAGECONFIG[minix]  = "--enable-minix,  --disable-minix"

EXTRA_OECONF = "                        \
    --disable-use-tty-group             \
    --disable-pg-bell                   \
    --disable-pylibmount                \
    --disable-plymouth_support          \
    --without-readline                  \
    --without-python                    \
    "

CACHED_CONFIGUREVARS_append = " scanf_cv_alloc_modifier=ms "


#======================================#
#                TASKS                 #
#--------------------------------------#


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
