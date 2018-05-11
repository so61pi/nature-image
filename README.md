## Overview

This repo contains the yocto recipes to build several Linux images for embedded system. Currently, there are 4 images for 4 different platforms:

| Image          | Platform/Board   | Firmware          | Description                                                                                                           |
|----------------|------------------|-------------------|-----------------------------------------------------------------------------------------------------------------------|
| cetacean-image | QEMU x86_64      | cetacean-firmware | The cetacean-image can be used to run in a hypervisor (QEMU, VirtualBox, VMWare).                                     |
| rodent-image   | BeagleBone Black | rodent-firmware   | To use this image, you have to build the init flasher (rodent-flasher-image) to flash the image to the on board eMMC. |
| lizard-image   | Cubieboard2      | lizard-firmware   |                                                                                                                       |
| mushroom-image | RPi3             | mushroom-firmware |                                                                                                                       |

## How to build

There are 2 ways to build the images:

- Build with docker (recommended).
    - You have to create the docker image first by running `make docker-build`.
- Or build directly on your system without docker.

You can also build all the images together or seperately.

### Build all images

This will take a very long time to complete for first build, you should leave it run at night.

#### Build with docker

```shell
make
```

#### Build without docker

```shell
cd scripts
./buildall
```

### Build single image

#### 1. Get into `scripts` directory

- With docker

```shell
make docker-shell
```

- Without docker

```shell
cd scripts
```

#### 2. Setup build env

```shell
IMAGE=<image-name> . ./setup

# Example:
# IMAGE=cetacean-image . ./setup
```

#### 3. Building

- Build firmware

```shell
bitbake <firmware-name>

# Example:
# bitbake cetacean-firmware
```

- Build wic

```shell
bitbake <image-name>

# Example:
# bitbake mushroom-image
```

- Build init flasher

```shell
bitbake <init-flasher-name>

# Example:
# bitbake rodent-flasher-image
```

## How to use

- The output of the build process (firmware, wic...) is at `build/build-<image-name>/tmp/deploy/images/<machine>/` (like `build/build-mushroom-image/tmp/deploy/images/raspberrypi3/`).
- For cetacean image
    - You could use VirtualBox or VMWare to create a machine and point the disk to `cetacean-image-qemux86-64.wic.vdi`.
    - Note that cetacean uses GPT, please configurate your virtual machine accordingly.
- For other images
    - Copy the wic file to micro SD.
    - Example for mushroom image `dd if=mushroom-image-raspberrypi3.wic of=/dev/<your-sd-dev> status=progress`

## How to upgrade

```shell
swupdate-net <url-to-swu>
```
