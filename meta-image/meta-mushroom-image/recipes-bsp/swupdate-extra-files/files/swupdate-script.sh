#!/bin/sh

mkdir -p /run/orgboot
umount /run/orgboot
mount /dev/mmcblk0p1 /run/orgboot
