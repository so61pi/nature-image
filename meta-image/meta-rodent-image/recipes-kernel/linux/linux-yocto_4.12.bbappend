FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://beaglebone-frag.cfg   \
                   file://squashfs-frag.cfg     \
                   file://usb-frag.cfg          \
                   file://cgroup-frag.cfg       \
                   file://bpf-frag.cfg          \
                   "

KBRANCH_beaglebone = "standard/beaglebone"
SRCREV_machine_beaglebone = "16de0149674ed12d983b77a453852ac2e64584b4"
COMPATIBLE_MACHINE_beaglebone = "beaglebone"
LINUX_VERSION_beaglebone = "4.12.12"
