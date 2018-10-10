import os, sys, re

class BasicRootfsChecker:
    def __init__(self, rootdir,
                 name_pattern = "^[_a-zA-Z0-9]+[-+._@a-zA-Z0-9]*$",
                 path_pattern = ".*",
                 skip_pattern_paths = set(),
                 must_exist_paths = set(),
                 must_empty_dirs = set(),
                 must_empty_files = set(),
                 allowed_empty_dirs = set(),
                 allowed_empty_files = set(),
                 allowed_dangling_symlinks = set()):
        self._rootdir = rootdir

        self._name_pattern = re.compile(name_pattern)
        self._path_pattern = re.compile(path_pattern)
        self._skip_pattern_paths = set(skip_pattern_paths)
        self._must_exist_paths = set(must_exist_paths)

        self._must_empty_dirs = set()
        self._must_empty_files = set()
        self._allowed_empty_dirs = set()
        self._allowed_empty_files = set()

        self._must_exist_paths.update(self._must_empty_dirs)
        self._must_exist_paths.update(self._must_empty_files)

        self._must_empty_dirs.update(must_empty_dirs)
        self._must_empty_files.update(must_empty_files)
        self._allowed_empty_dirs.update(allowed_empty_dirs)
        self._allowed_empty_files.update(allowed_empty_files)

        self._allowed_empty_dirs.update(self._must_empty_dirs)
        self._allowed_empty_files.update(self._must_empty_files)

        self._allowed_dangling_symlinks = set()
        self._allowed_dangling_symlinks.update(allowed_dangling_symlinks)

        # _check variables
        self._check_name_pattern = set()
        self._check_path_pattern = set()
        self._check_not_exist_paths = self._must_exist_paths

        self._check_empty_dirs = set()
        self._check_empty_files = set()
        self._check_not_empty_dirs = self._must_empty_dirs
        self._check_not_empty_files = self._must_empty_files
        self._check_dangling_symlinks = set()
        self._check_unknown_filetype = set()

    def check_empty_directory(self, path, name):
        self._check_path(path, name)
        self._check_not_empty_dirs.discard(self._r(path))
        if not self._r(path) in self._allowed_empty_dirs:
            self._check_empty_dirs.add(self._r(path))

    def check_directory(self, path, name):
        self._check_path(path, name)
        if self._r(path) in self._must_empty_dirs:
            self._check_not_empty_dirs.add(self._r(path))

    def check_file_regular(self, path, name):
        self._check_path(path, name)

        if os.path.getsize(path) == 0:
            self._check_not_empty_files.discard(self._r(path))
            if not self._r(path) in self._allowed_empty_files:
                self._check_empty_files.add(self._r(path))

    def check_file_symlink(self, path, name):
        self._check_path(path, name)

        dangling = False
        if os.path.exists(path):
            if not os.path.realpath(path).startswith(self._rootdir):
                dangling = True
        else:
            link = os.readlink(path)
            if os.path.isabs(link):
                if not os.path.exists((self._rootdir + link).replace("//", "/")):
                    dangling = True
            else:
                dangling = True

        if dangling and not self._r(path) in self._allowed_dangling_symlinks:
            self._check_dangling_symlinks.add(self._r(path))

    def check_file_other(self, path, name):
        self._check_path(path, name)
        self._check_unknown_filetype.add(self._r(path))

    def print_result(self):
        for p in sorted(self._check_name_pattern):
            bb.warn("[NAME PATTERN] {}".format(p))
        for p in sorted(self._check_path_pattern):
            bb.warn("[PATH PATTERN] {}".format(p))

        for p in sorted(self._check_empty_dirs):
            bb.warn("[EMPTY DIR] {}".format(p))
        for p in sorted(self._check_empty_files):
            bb.warn("[EMPTY FILE] {}".format(p))
        for p in sorted(self._check_not_empty_dirs):
            bb.warn("[NOT EMPTY DIR] {}".format(p))
        for p in sorted(self._check_not_empty_files):
            bb.warn("[NOT EMPTY FILE] {}".format(p))

        for p in sorted(self._check_not_exist_paths):
            bb.warn("[MISSING PATH] {}".format(p))

        for p in sorted(self._check_dangling_symlinks):
            bb.warn("[DANGLING LINK] {}".format(p))

        for p in sorted(self._check_unknown_filetype):
            bb.warn("[UNKNOWN TYPE] {}".format(p))

    def rootdir(self):
        return self._rootdir

    def _check_path(self, path, name):
        self._check_not_exist_paths.discard(self._r(path))

        if self._r(path) in self._skip_pattern_paths:
            return

        if not self._name_pattern.match(name):
            self._check_name_pattern.add(self._r(path))

        if not self._path_pattern.match(self._r(path)):
            self._check_path_pattern.add(self._r(path))

    def _r(self, path):
        return path[len(self._rootdir):]


def check(checker):
    for dirpath, dirnames, filenames in os.walk(checker.rootdir()):
        if (len(dirnames) == 0) and (len(filenames) == 0):
            checker.check_empty_directory(dirpath, os.path.basename(dirpath))
            continue

        for d in dirnames:
            checker.check_directory(os.path.join(dirpath, d), d)

        for f in filenames:
            if os.path.isfile(os.path.join(dirpath, f)):
                checker.check_file_regular(os.path.join(dirpath, f), f)
            elif os.path.islink(os.path.join(dirpath, f)):
                checker.check_file_symlink(os.path.join(dirpath, f), f)
            else:
                checker.check_file_other(os.path.join(dirpath, f), f)

default_skip_pattern_paths = frozenset({ "/usr/bin/[" })
default_must_exist_paths = frozenset({ "/dev", "/etc", "/proc", "/run", "/sys", "/usr", "/usr/bin", "/usr/lib", "/usr/sbin" })
default_must_empty_dirs = frozenset({ "/dev", "/proc", "/run", "/sys" })
