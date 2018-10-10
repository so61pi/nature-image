#
# Simplified from systemd.bbclass
#
# - Always use pkg_postinst
# - Only support read-only-rootfs
# - Only support enable
#

PACKAGE_WRITE_DEPS_append = " systemd-systemctl-native "


systemd_postinst() {
set -e

OPT="--root=$D"

systemctl $OPT enable ${SYSTEMD_SERVICE}
}


python __anonymous() {
    # Recipe parse-time sanity checks
    def sanitycheck(d):
        systemd_packages = d.getVar('SYSTEMD_PACKAGES')

        if not systemd_packages:
            bb.fatal('%s : SYSTEMD_PACKAGES is not set' % d.getVar('FILE', False))

        for pkg in systemd_packages.split():
            if not d.getVar('SYSTEMD_SERVICE_%s' % pkg):
                bb.fatal('%s : SYSTEMD_SERVICE is not set for package %s' % (d.getVar('FILE', False), pkg))

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
        postinst += d.getVar('systemd_postinst')
        d.setVar('pkg_postinst_%s' % pkg, postinst)


    if not bb.data.inherits_class('nativesdk', d) and not bb.data.inherits_class('native', d):
        systemd_packages = d.getVar('SYSTEMD_PACKAGES') or ''
        for pkg in systemd_packages.split():
            update_package(pkg)
}
