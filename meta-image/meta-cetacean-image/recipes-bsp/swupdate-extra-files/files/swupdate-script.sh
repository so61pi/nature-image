#!/bin/sh


do_preinst() {
    :
    # swupdate-helper
    # already in prepare in sw-description
}


do_postinst() {
    cp -f /var/lib/swupdate-helper/sibling-bootconf /var/lib/swupdate-helper/current-bootconf
}


do_invalid() {
    echo "Invalid action"
    exit 1
}


case "$1" in
    "preinst")
        do_preinst
    ;;

    "postinst")
        do_postinst
    ;;

    *)
        do_invalid
    ;;
esac
