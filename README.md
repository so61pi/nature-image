**`cetacean-image`**

The cetacean image can be used to run in a hypervisor (`QEMU`, `VirtualBox`, `VMWare`). To build the image and firmware, you can use the following commands

```shell
# Create build directory and configuration
. ./setup cetacean-image

# Build the image
bitbake cetacean-image

# Build the firmware
bitbake cetacean-firmware
```

To upgrade firmware, you can use `firmware-upgrade` (a wrapper for `swupdate`)

```shell
firmware-upgrade -i <url>
```


**`rodent-image`**

This image can be used in the `BeagleBone Black` board. To build the firmware and initial flasher image (which can be used to flash the board's eMMC), you can use the following commands

```shell
# Create build directory and configuration
. ./setup rodent-image

# Build the firmware
bitbake rodent-firmware

# Build the initial flasher image
# You can dd the output image to and SD-Card and use it to flash the device
bitbake rodent-flasher-image
```

To upgrade firmware, you can use `swupdate-net` (a wrapper for `swupdate`)

```shell
swupdate-net <url>
```
