IMAGETAG	:= image-yocto-builder


.PHONY: all
all: docker-run


.PHONY: docker-build
docker-build:
	docker build --rm=true --build-arg groupid=$(shell id -g) --build-arg userid=$(shell id -u) --build-arg gitusername="$(shell git config user.name)" --build-arg gituseremail="$(shell git config user.email)" -t $(IMAGETAG) .


.PHONY: docker-run
docker-run:
	docker run -it --rm --init --env-file Dockerenv.list -v $(shell pwd):/code:rw $(IMAGETAG)


.PHONY: docker-shell
docker-shell:
	docker run -it --rm --init --env-file Dockerenv.list -v $(shell pwd):/code:rw $(IMAGETAG) fish


# NOTE: The docker-clean is used to clean ALL the used containers and images in the system.
.PHONY: docker-clean
docker-clean:
	docker image prune -f
	docker container prune -f


.PHONY: docker-rm
docker-rm:
	docker image rm -f $(IMAGETAG)
