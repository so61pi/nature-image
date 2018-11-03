.. title:: Nature Linux's documentation!


Overview
========

This repository contains the yocto recipes to build several Linux images for embedded system.

Currently, there are 4 images for 4 different platforms:

==============  ================  =================  =====================================================
Image           Platform/Board    Firmware           Description
==============  ================  =================  =====================================================
cetacean-image  QEMU x86_64       cetacean-firmware  The cetacean-image can be used to run in a hypervisor
                                                     (QEMU, VirtualBox, VMWare).
rodent-image    BeagleBone Black  rodent-firmware    To use this image, you have to build the init flasher
                                                     (``rodent-flasher-image``) to flash the image to the
                                                     on board eMMC.
lizard-image    Cubieboard2       lizard-firmware
mushroom-image  RPi3              mushroom-firmware
==============  ================  =================  =====================================================


Contents
========

.. toctree::
   :maxdepth: 2

   doc/how-to


Search
======

* :ref:`search`
