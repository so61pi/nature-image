PACKAGES_prepend = " ${PN}-bash-completion ${PN}-zsh-completion "

FILES_${PN}-bash-completion_append = " ${datadir}/bash-completion ${sysconfdir}/bash_completion.d "
FILES_${PN}-zsh-completion_append  = " ${datadir}/zsh/site-functions ${datadir}/zsh/vendor-completions "
