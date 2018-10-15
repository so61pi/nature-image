IMAGETAG		:= naturelinux/nature-image-builder-$(shell id -u)-$(shell id -g)
IMAGEBASETAG	:= naturelinux/nature-image-builder-base
IMGVERSION		:= 1.0


.PHONY: all
all: docker-run


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


.PHONY: docker-run
docker-run:
	docker run --rm --init --env-file scripts/docker/Dockerenv.list -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)"


.PHONY: docker-shell
docker-shell:
	docker run -it --rm --init --env-file scripts/docker/Dockerenv.list -v $(shell pwd):/code:rw "$(IMAGETAG):$(IMGVERSION)" fish


# NOTE: The docker-clean is used to clean ALL the used containers and images in the system.
.PHONY: docker-clean
docker-clean:
	docker system prune -f
