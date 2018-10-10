#
# Example
#   SYSTEMD_STATIC_PACKAGES = " ${PN} "
#   SYSTEMD_STATIC_SERVICE_${PN}        = " systemd-remount-fs.service local-fs.target; "
#   SYSTEMD_STATIC_SERVICE_${PN}_append = " tmp.mount local-fs.target tmp.link.mount "
#
# SYSTEMD_STATIC_SERVICE = " service target [link]; service target [link] "
#


# $1 = SERVICE
# $2 = TARGET
# $3 = LINK
systemd_static () {
    set -e

    local WANTSDIR="$D${systemd_system_unitdir}/$2.wants"

    if [ ! -e "$WANTSDIR" ]; then
        mkdir "$WANTSDIR"
    elif [ ! -d "$WANTSDIR" ]; then
        echo "$WANTSDIR already exists but is not a directory"
        exit 1
    fi

    ln -sr "$D${systemd_system_unitdir}/$1" "$WANTSDIR/$3"
}


python __anonymous() {
    # Recipe parse-time sanity checks
    def sanitycheck(d):
        systemd_packages = d.getVar('SYSTEMD_STATIC_PACKAGES')

        if not systemd_packages:
            bb.fatal('%s : SYSTEMD_STATIC_PACKAGES is not set' % d.getVar('FILE', False))

        for pkg in systemd_packages.split():
            if not d.getVar('SYSTEMD_STATIC_SERVICE_%s' % pkg):
                bb.fatal('%s : SYSTEMD_STATIC_SERVICE is not set for package %s' % (d.getVar('FILE', False), pkg))

        if not bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', True, False, d):
            bb.fatal('%s : read-only-rootfs is not in IMAGE_FEATURES' % d.getVar('FILE', False))

    if not bb.data.inherits_class('nativesdk', d) and not bb.data.inherits_class('native', d):
        sanitycheck(d)
}


python populate_packages_prepend() {
    def update_package(pkg):
        postinst = d.getVar('pkg_postinst_%s' % pkg) or d.getVar('pkg_postinst')
        if not postinst:
            postinst = '#!/usr/bin/sh\n'

        postinst += 'systemd_static () {\n%s}\n' % d.getVar('systemd_static')

        for line in d.getVar('SYSTEMD_STATIC_SERVICE_%s' % pkg).split(';'):
            info = line.split()
            if not info:
                continue

            if len(info) > 3:
                bb.fatal('%s : %s of package %s has more than 3 arguments' % (d.getVar('FILE', False), line, pkg))

            service = info[0] if 0 < len(info) else None
            target  = info[1] if 1 < len(info) else None
            link    = info[2] if 2 < len(info) else service

            if not service or not target:
                bb.fatal('%s : service or target is missing for package %s' % (d.getVar('FILE', False), pkg))

            if target.endswith('.wants'):
                bb.warn('%s : target %s of package %s ends with .wants' % (d.getVar('FILE', False), target, pkg))

            postinst += 'systemd_static %s %s %s\n' % (service, target, link)

        d.setVar('pkg_postinst_%s' % pkg, postinst)


    if not bb.data.inherits_class('nativesdk', d) and not bb.data.inherits_class('native', d):
        systemd_packages = d.getVar('SYSTEMD_STATIC_PACKAGES') or ''
        for pkg in systemd_packages.split():
            update_package(pkg)
}
