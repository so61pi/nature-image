do_install_append() {
    rm -rf ${D}${datadir}/bash-completion ${D}${sysconfdir}/bash_completion.d
    rm -rf ${D}${datadir}/zsh/site-functions ${D}${datadir}/zsh/vendor-completions
}
