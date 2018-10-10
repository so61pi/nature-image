def binpath(d, names, prefix=''):
    paths = ''
    for name in names.split():
        for dirvar in ["sbindir", "bindir"]:
            paths += ' {0}{1}/{2} '.format(prefix, d.getVar(dirvar), name)
    return paths
