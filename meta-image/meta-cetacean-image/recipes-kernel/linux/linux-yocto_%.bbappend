FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " file://squashfs-frag.cfg     \
                   file://efi-frag.cfg          \
                   file://cgroup-frag.cfg       \
                   file://bpf-frag.cfg          \
                   "

# Do not put bzImage in the final image (look in kernel.bbclass).
# (Note: This is already set in qemu.inc)
RDEPENDS_${KERNEL_PACKAGE_NAME}-base = ""
