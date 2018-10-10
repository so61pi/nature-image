ARG imgbase
FROM ${imgbase}

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
CMD /code/scripts/buildall
