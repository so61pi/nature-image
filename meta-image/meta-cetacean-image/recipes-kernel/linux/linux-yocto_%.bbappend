FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://squashfs-frag.cfg     \
                   file://efi-frag.cfg          \
                   file://cgroup-frag.cfg       \
                   file://bpf-frag.cfg          \
                   "
