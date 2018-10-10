HISTFILE=$HOME/.zshhistfile
HISTSIZE=1000                       # Number of lines of history in memory.
SAVEHIST=1000                       # Number of lines of history in history file.
setopt      histignoredups          # Don't add duplicated commands.
setopt      histexpiredupsfirst     # Save unique hist entries longer.
setopt      histverify              # Edit recalled history before running.
setopt      extendedhistory         # Save timestamp on history entries.
setopt      extendedglob            # Enable extended glob.
setopt      autocd                  # Enable auto cd.
unsetopt    sharehistory            # Disable share history across terminals.
bindkey -e                          # Emacs mode.

alias ...=../..
alias ....=../../..
alias .....=../../../..
alias ......=../../../../..

alias ls='ls --color=auto'
alias ll='ls -alh'
alias l='ll'
