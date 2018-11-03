.. title:: How To

.. contents::


Building
========

All Images
----------

Build with docker (recommended)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

#. First, you need to build a builder docker image by running ``make docker-img-build``.

   **Note:** This command pulls a base image that is updated frequently from docker registry. Each time it is updated, the command above needs to be executed again to download the correct version. Otherwise, you could encounter an error message like this

      ``Unable to find image '<image-name>' locally``

#. After having the builder image, we could build all images by running ``make`` at the project root.

   If you are building for the first time, it will take about 6 hours.


Build without docker
^^^^^^^^^^^^^^^^^^^^

It's easier to build without docker, you only need to run ``cd scripts && ./buildall`` but the environment is not isolated, which could trigger some errors in the long run.


Single Image
------------

To build a single image instead of all of them, we execute ``make build-<image-name>`` in out terminal (note that this uses docker builder image mentioned above).

For example, if you want to build ceatacean image, run ``make build-cetacean-image``.


Documents
---------

Command ``make doc-html`` is used to build documents and the output html files are placed at ``build/documents/html``.

To quickly build and open the html file you could run

.. code-block:: shell

   make doc-html && xdg-open build/documents/html/index.html


Using the build outputs
=======================
The outputs of the build process (firmware, wic...) is at ``build-artifacts/_latest/``.

* cetacean image

   You could use VirtualBox or VMWare to create a machine and point the disk to ``cetacean-image-qemux86-64.wic.vdi``.

   **Note:** Cetacean uses GPT, make sure you configure your virtual machine correctly.

* Other images

   Copy the wic file to micro SD.

   .. code-block:: shell

      # Example for mushroom image
      dd if=mushroom-image-raspberrypi3.wic of=/dev/<your-sd-dev> status=progress


Upgrading a running system
==========================

``swupdate-net <url-to-swu>`` could be used to upgrade a running system.

.. code-block:: shell

   # Example:
   # If we already have
   # - an http server at build-artifacts/_latest
   # - a VM instance runs cetacean image
   # then we could upgrade the firmware by running
   #   swupdate-net http://192.168.1.1/cetacean-firmware-qemux86-64.swu
