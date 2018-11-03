IMAGETAG		:= naturelinux/nature-image-builder-$(shell id -u)-$(shell id -g)
IMAGEBASETAG	:= naturelinux/nature-image-builder-base
IMGVERSION		:= 1.0

DOCKEROPTS		:= --rm --init --env-file scripts/docker/Dockerenv.list -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)"


.PHONY: all
all: docker-env
	docker run $(DOCKEROPTS)


.PHONY: clean
clean:
	rm -rf build/build-*


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


.PHONY: docker-shell
docker-shell: docker-env
	docker run -it $(DOCKEROPTS) fish


.PHONY: docker-imgbase-build
docker-imgbase-build:
	docker build --rm=true --file scripts/docker/Dockerfile-base -t "$(IMAGEBASETAG):$(IMGVERSION)" scripts/docker/


.PHONY: docker-imgbase-push
docker-imgbase-push:
	docker push "$(IMAGEBASETAG):$(IMGVERSION)"


.PHONY: docker-img-build
docker-img-build:
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
