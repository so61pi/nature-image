IMAGETAG		:= naturelinux/nature-image-builder
IMAGEBASETAG	:= naturelinux/nature-image-builder-base
IMGVERSION		:= 1.0


.PHONY: all
all: docker-run


.PHONY: docker-imgbase-build
docker-imgbase-build:
	docker build --rm=true --file Dockerfile-base -t "$(IMAGEBASETAG):$(IMGVERSION)" .


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
	-t "$(IMAGETAG):$(IMGVERSION)" .


.PHONY: docker-img-rm
docker-img-rm:
	docker image rm -f "$(IMAGETAG):$(IMGVERSION)"


.PHONY: docker-run
docker-run:
	docker run -it --rm --init --env-file Dockerenv.list -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)"


.PHONY: docker-shell
docker-shell:
	docker run -it --rm --init --env-file Dockerenv.list -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)" fish


# NOTE: The docker-clean is used to clean ALL the used containers and images in the system.
.PHONY: docker-clean
docker-clean:
	docker image prune -f
	docker container prune -f
