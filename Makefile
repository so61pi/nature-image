IMAGETAG		:= naturelinux/nature-image-builder-$(shell id -u)-$(shell id -g)
IMAGEBASETAG	:= naturelinux/nature-image-builder-base
IMGVERSION		:= 1.3

IMGCACHEPATH	:= build/docker-cache/nature-image-builder-base.tar

# We want to avoid "the input device is not a TTY" when building in
# CI/CD, but still need an interactive shell when build locally.
#
# The mentioned error could be reproduced by removing the next line
# and run "true | make".
DOCKEROPTS		:= $(shell if [ -t 0 ]; then echo -it; fi)
DOCKEROPTS		+= --rm --init --privileged --hostname localhost --env-file scripts/docker/Dockerenv.list
DOCKEROPTS		+= -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)"
DOCKERENVS		:= BB_ENV_EXTRAWHITE INHERIT SSTATE_MIRRORS SOURCE_MIRROR_URL BB_GENERATE_MIRROR_TARBALLS


#-------------------------------
## all                          : Build all images.
.PHONY: all
all: docker-env
	docker run $(DOCKEROPTS)

#-------------------------------
## clean                        : Remove files in build/documents and build/build-*.
.PHONY: clean
clean:
	rm -rf build/documents build/build-*


##
#-------------------------------
## build-cetacean-image         : Build cetacean image.
.PHONY: build-cetacean-image
build-cetacean-image: docker-env
	docker run $(DOCKEROPTS) ./build-cetacean-image


#-------------------------------
## build-rodent-image           : Build rodent image.
.PHONY: build-rodent-image
build-rodent-image: docker-env
	docker run $(DOCKEROPTS) ./build-rodent-image


#-------------------------------
## build-lizard-image           : Build lizard image.
.PHONY: build-lizard-image
build-lizard-image: docker-env
	docker run $(DOCKEROPTS) ./build-lizard-image


#-------------------------------
## build-mushroom-image         : Build mushroom image.
.PHONY: build-mushroom-image
build-mushroom-image: docker-env
	docker run $(DOCKEROPTS) ./build-mushroom-image


##
#-------------------------------
## tests-run                    : Run tests.
.PHONY: tests-run
tests-run: docker-env
	docker run $(DOCKEROPTS) ./tests-run


##
#-------------------------------
## docker-env                   : Create default scripts/docker/Dockerenv.list file.
.PHONY: docker-env
docker-env:
	[ -e scripts/docker/Dockerenv.list ] || cp scripts/docker/Dockerenv.list.template scripts/docker/Dockerenv.list


#-------------------------------
## docker-env-from-env          : Create scripts/docker/Dockerenv.list and with BB_ENV_EXTRAWHITE 
##                                INHERIT SSTATE_MIRRORS SOURCE_MIRROR_URL BB_GENERATE_MIRROR_TARBALLS.
.PHONY: docker-env-from-env
docker-env-from-env: docker-env
	$(foreach ev,$(DOCKERENVS),if [ -n "$$$(ev)" ]; then echo "$(ev)=$$$(ev)" >> scripts/docker/Dockerenv.list; fi;)


#-------------------------------
## docker-shell                 : Open a terminal into builder docker image.
.PHONY: docker-shell
docker-shell: docker-env
	docker run $(DOCKEROPTS) bash


##
#-------------------------------
## docker-imgbase-build         : Build the base builder docker image, usually used when updating Dockerfile-base.
.PHONY: docker-imgbase-build
docker-imgbase-build:
	docker build --rm=true --file scripts/docker/Dockerfile-base -t "$(IMAGEBASETAG):$(IMGVERSION)" scripts/docker/


#-------------------------------
## docker-imgbase-push          : Push the built builder docker image.
.PHONY: docker-imgbase-push
docker-imgbase-push:
	docker push "$(IMAGEBASETAG):$(IMGVERSION)"


#-------------------------------
## docker-imgbase-reload        : Load, pull then save the base builder docker image.
##                                Mostly used by CI/CD to avoid re-pulling the image everytime.
.PHONY: docker-imgbase-reload
docker-imgbase-reload:
	if [ -e "$(IMGCACHEPATH)" ]; then docker load -i "$(IMGCACHEPATH)"; fi
	docker pull "$(IMAGEBASETAG):$(IMGVERSION)"
	mkdir -p "$(shell dirname $(IMGCACHEPATH))"
	docker save -o "$(IMGCACHEPATH)" "$(IMAGEBASETAG):$(IMGVERSION)"


##
#-------------------------------
## docker-img-build             : Build builder docker image.
.PHONY: docker-img-build
docker-img-build:
	[ -e scripts/docker/Dockerfile ] || cp scripts/docker/Dockerfile.template scripts/docker/Dockerfile
	docker build --rm=true \
	--build-arg imgbase="$(IMAGEBASETAG):$(IMGVERSION)" \
	--build-arg groupid=$(shell id -g) \
	--build-arg userid=$(shell id -u) \
	--build-arg gitusername="$(shell git config user.name)" \
	--build-arg gituseremail="$(shell git config user.email)" \
	-t "$(IMAGETAG):$(IMGVERSION)" scripts/docker/


#-------------------------------
## docker-img-rm                : Remove local builder docker image.
.PHONY: docker-img-rm
docker-img-rm:
	docker image rm -f "$(IMAGETAG):$(IMGVERSION)"


#-------------------------------
## docker-clean                 : Clean all unused images and containers in the system.
.PHONY: docker-clean
docker-clean:
	docker system prune -f


##
#-------------------------------
## doc-html                     : Build documents.
.PHONY: doc-html
doc-html: docker-env
	docker run $(DOCKEROPTS) ./builddoc


##
#-------------------------------
## help                         : Display this help.
.PHONY: help
help: Makefile
	@sed -n 's/^##//p' $<
