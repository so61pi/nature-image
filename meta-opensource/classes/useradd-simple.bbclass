inherit useradd_base

#
# Simplified from useradd.bbclass
#
# - Always use pkg_preinst
# - Only support read-only-rootfs
# - Assume that base-files package already provides
#     /etc/login.defs
#     /etc/passwd
#     /etc/shadow
#     /etc/group
#

DEPENDS_append_class-target = " base-files "
PACKAGE_WRITE_DEPS_append = " shadow-native "


useradd_preinst () {
set -e

# Installing into a sysroot
SYSROOT="$D"
OPT="--root $SYSROOT"

# user/group lookups should match useradd/groupadd --root
export PSEUDO_PASSWD="$SYSROOT"

# check for /etc/login.defs
if [ ! -e $D${sysconfdir}/login.defs ]; then
    echo "${sysconfdir}/login.defs does not exist"
    exit 1
fi

# Installing onto a target
# Add groups and users defined only for this package
GROUPADD_PARAM="${GROUPADD_PARAM}"
USERADD_PARAM="${USERADD_PARAM}"
GROUPMEMS_PARAM="${GROUPMEMS_PARAM}"

# Perform group additions first, since user additions may depend
# on these groups existing
if test "x`echo $GROUPADD_PARAM | tr -d '[:space:]'`" != "x"; then
    echo "Running groupadd commands..."
    # Invoke multiple instances of groupadd for parameter lists
    # separated by ';'
    opts=`echo "$GROUPADD_PARAM" | cut -d ';' -f 1 | sed -e 's#[ \t]*$##'`
    remaining=`echo "$GROUPADD_PARAM" | cut -d ';' -f 2- | sed -e 's#[ \t]*$##'`
    while test "x$opts" != "x"; do
        perform_groupadd "$SYSROOT" "$OPT $opts"
        if test "x$opts" = "x$remaining"; then
            break
        fi
        opts=`echo "$remaining" | cut -d ';' -f 1 | sed -e 's#[ \t]*$##'`
        remaining=`echo "$remaining" | cut -d ';' -f 2- | sed -e 's#[ \t]*$##'`
    done
fi 

if test "x`echo $USERADD_PARAM | tr -d '[:space:]'`" != "x"; then
    echo "Running useradd commands..."
    # Invoke multiple instances of useradd for parameter lists
    # separated by ';'
    opts=`echo "$USERADD_PARAM" | cut -d ';' -f 1 | sed -e 's#[ \t]*$##'`
    remaining=`echo "$USERADD_PARAM" | cut -d ';' -f 2- | sed -e 's#[ \t]*$##'`
    while test "x$opts" != "x"; do
        perform_useradd "$SYSROOT" "$OPT $opts"
        if test "x$opts" = "x$remaining"; then
            break
        fi
        opts=`echo "$remaining" | cut -d ';' -f 1 | sed -e 's#[ \t]*$##'`
        remaining=`echo "$remaining" | cut -d ';' -f 2- | sed -e 's#[ \t]*$##'`
    done
fi

if test "x`echo $GROUPMEMS_PARAM | tr -d '[:space:]'`" != "x"; then
    echo "Running groupmems commands..."
    # Invoke multiple instances of groupmems for parameter lists
    # separated by ';'
    opts=`echo "$GROUPMEMS_PARAM" | cut -d ';' -f 1 | sed -e 's#[ \t]*$##'`
    remaining=`echo "$GROUPMEMS_PARAM" | cut -d ';' -f 2- | sed -e 's#[ \t]*$##'`
    while test "x$opts" != "x"; do
        perform_groupmems "$SYSROOT" "$OPT $opts"
        if test "x$opts" = "x$remaining"; then
            break
        fi
        opts=`echo "$remaining" | cut -d ';' -f 1 | sed -e 's#[ \t]*$##'`
        remaining=`echo "$remaining" | cut -d ';' -f 2- | sed -e 's#[ \t]*$##'`
    done
fi
}


python __anonymous() {
    # Recipe parse-time sanity checks
    def sanitycheck(d):
        useradd_packages = d.getVar('USERADD_PACKAGES')

        if not useradd_packages:
            bb.fatal('%s : USERADD_PACKAGES is not set' % d.getVar('FILE', False))

        for pkg in useradd_packages.split():
            if not d.getVar('USERADD_PARAM_%s' % pkg) and not d.getVar('GROUPADD_PARAM_%s' % pkg) and not d.getVar('GROUPMEMS_PARAM_%s' % pkg):
                bb.fatal('%s : USERADD_PARAM or GROUPADD_PARAM or GROUPMEMS_PARAM are not set for package %s' % (d.getVar('FILE', False), pkg))

        if not bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', True, False, d):
            bb.fatal('%s : read-only-rootfs is not in IMAGE_FEATURES' % d.getVar('FILE', False))

    if not bb.data.inherits_class('nativesdk', d) and not bb.data.inherits_class('native', d):
        sanitycheck(d)
}


# Adds the preinst script into generated packages
python populate_packages_prepend () {
    def update_package(pkg):
        """
        useradd preinst is appended here because pkg_preinst may be
        required to execute on the target. Not doing so may cause
        useradd preinst to be invoked twice, causing unwanted warnings.
        """
        preinst = d.getVar('pkg_preinst_%s' % pkg) or d.getVar('pkg_preinst')
        if not preinst:
            preinst = '#!/usr/bin/sh\n'
        preinst += 'bbnote () {\n\techo "NOTE: $*"\n}\n'
        preinst += 'bbwarn () {\n\techo "WARNING: $*"\n}\n'
        preinst += 'bbfatal () {\n\techo "ERROR: $*"\n\texit 1\n}\n'
        preinst += 'perform_groupadd () {\n%s}\n' % d.getVar('perform_groupadd')
        preinst += 'perform_useradd () {\n%s}\n' % d.getVar('perform_useradd')
        preinst += 'perform_groupmems () {\n%s}\n' % d.getVar('perform_groupmems')
        preinst += d.getVar('useradd_preinst')
        d.setVar('pkg_preinst_%s' % pkg, preinst)

        # RDEPENDS setup
        rdepends = d.getVar('RDEPENDS_%s' % pkg) or ''
        rdepends += ' ' + d.getVar('MLPREFIX', False) + 'base-files'
        d.setVar('RDEPENDS_%s' % pkg, rdepends)


    # Add the user/group preinstall scripts and RDEPENDS requirements
    # to packages specified by USERADD_PACKAGES
    if not bb.data.inherits_class('nativesdk', d) and not bb.data.inherits_class('native', d):
        useradd_packages = d.getVar('USERADD_PACKAGES') or ''
        for pkg in useradd_packages.split():
            update_package(pkg)
}
