if test -z "${rootdev}"; then
	rootdev=mmcblk0p2
fi
setenv bootargs console=${console} console=tty1 root=/dev/${rootdev} rootwait panic=10 ro rootfstype=ext4
load mmc 0:2 ${fdt_addr_r} boot/${fdtfile}
load mmc 0:2 ${kernel_addr_r} boot/uImage
bootm ${kernel_addr_r} - ${fdt_addr_r}
