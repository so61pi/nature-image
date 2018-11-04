IMAGETAG		:= naturelinux/nature-image-builder-$(shell id -u)-$(shell id -g)
IMAGEBASETAG	:= naturelinux/nature-image-builder-base
IMGVERSION		:= 1.2

IMGCACHEPATH	:= build/docker-cache/nature-image-builder-base.tar

# We want to avoid "the input device is not a TTY" when building in
# CI/CD, but still need an interactive shell when build locally.
#
# The mentioned error could be reproduced by removing the next line
# and run "true | make".
DOCKEROPTS		:= $(shell if [ -t 0 ]; then echo -it; fi)
DOCKEROPTS		+= --rm --init --hostname localhost --env-file scripts/docker/Dockerenv.list
DOCKEROPTS		+= -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)"
DOCKERENVS		:= BB_ENV_EXTRAWHITE INHERIT SSTATE_MIRRORS SOURCE_MIRROR_URL BB_GENERATE_MIRROR_TARBALLS


.PHONY: all
all: docker-env
	docker run $(DOCKEROPTS)


.PHONY: clean
clean:
	rm -rf build/documents build/build-*


.PHONY: build-cetacean-image
build-cetacean-image: docker-env
	docker run $(DOCKEROPTS) ./build-cetacean-image


.PHONY: build-rodent-image
build-rodent-image: docker-env
	docker run $(DOCKEROPTS) ./build-rodent-image


.PHONY: build-lizard-image
build-lizard-image: docker-env
	docker run $(DOCKEROPTS) ./build-lizard-image


.PHONY: build-mushroom-image
build-mushroom-image: docker-env
	docker run $(DOCKEROPTS) ./build-mushroom-image


.PHONY: docker-env
docker-env:
	[ -e scripts/docker/Dockerenv.list ] || cp scripts/docker/Dockerenv.list.template scripts/docker/Dockerenv.list


.PHONY: docker-env-from-env
docker-env-from-env: docker-env
	$(foreach ev,$(DOCKERENVS),if [ -n "$$$(ev)" ]; then echo "$(ev)=$$$(ev)" >> scripts/docker/Dockerenv.list; fi;)


.PHONY: docker-shell
docker-shell: docker-env
	docker run $(DOCKEROPTS) bash


.PHONY: docker-imgbase-build
docker-imgbase-build:
	docker build --rm=true --file scripts/docker/Dockerfile-base -t "$(IMAGEBASETAG):$(IMGVERSION)" scripts/docker/


.PHONY: docker-imgbase-push
docker-imgbase-push:
	docker push "$(IMAGEBASETAG):$(IMGVERSION)"


.PHONY: docker-imgbase-reload
docker-imgbase-reload:
	if [ -e "$(IMGCACHEPATH)" ]; then docker load -i "$(IMGCACHEPATH)"; fi
	docker pull "$(IMAGEBASETAG):$(IMGVERSION)"
	mkdir -p "$(shell dirname $(IMGCACHEPATH))"
	docker save -o "$(IMGCACHEPATH)" "$(IMAGEBASETAG):$(IMGVERSION)"


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


.PHONY: docker-img-rm
docker-img-rm:
	docker image rm -f "$(IMAGETAG):$(IMGVERSION)"


# NOTE: The docker-clean is used to clean ALL the used containers and images in the system.
.PHONY: docker-clean
docker-clean:
	docker system prune -f


.PHONY: doc-html
doc-html: docker-env
	docker run $(DOCKEROPTS) ./builddoc
