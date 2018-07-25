FROM ubuntu:16.04

RUN DEBIAN_FRONTEND=noninteractive; apt-get update && apt-get upgrade -y
RUN DEBIAN_FRONTEND=noninteractive; apt-get update && apt-get upgrade -y && apt-get install -y --fix-missing gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat cpio python python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping libsdl1.2-dev xterm locales vim tmux tree fish man-db less
RUN DEBIAN_FRONTEND=noninteractive; apt-get clean -y

RUN dpkg-reconfigure locales && locale-gen en_US.UTF-8 && update-locale LC_ALL=en_US.UTF-8 LANG=en_US.UTF-8

ARG groupid
ARG userid
RUN [ $(getent group ${groupid}) ] || addgroup --quiet --gid ${groupid} builder
RUN [ $(getent passwd ${userid}) ] || adduser --quiet --uid ${userid} --gid ${groupid} --disabled-password --gecos '' builder
RUN usermod -a --groups ${groupid} $(id -un ${userid}) || true

RUN chsh --shell /usr/bin/fish builder

ARG gitusername
ARG gituseremail
RUN git config --system user.name "${gitusername}"
RUN git config --system user.email "${gituseremail}"

USER ${userid}:${groupid}
WORKDIR /code/scripts
CMD ./buildall
