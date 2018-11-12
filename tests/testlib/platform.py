import datetime


class Platform:
    def __init__(self, name, startuptime, reboottime, upgradetime):
        self._name = name

        self._startuptime = startuptime
        self._reboottime = reboottime
        self._upgradetime = upgradetime

    @property
    def name(self):
        return self._name

    @property
    def startuptime(self):
        return self._startuptime

    @property
    def reboottime(self):
        return self._reboottime

    @property
    def upgradetime(self):
        return self._upgradetime


class PlatformCetacean(Platform):
    def __init__(self, *args, **kwargs):
        super(PlatformCetacean, self).__init__('cetacean',
                                               startuptime=datetime.time(second=10),
                                               reboottime=datetime.time(second=30),
                                               upgradetime=datetime.time(second=40))


class PlatformRodent(Platform):
    def __init__(self, *args, **kwargs):
        super(PlatformRodent, self).__init__('rodent',
                                             startuptime=datetime.time(second=30),
                                             reboottime=datetime.time(second=40),
                                             upgradetime=datetime.time(second=50))


class PlatformLizard(Platform):
    def __init__(self, *args, **kwargs):
        super(PlatformLizard, self).__init__('lizard',
                                             startuptime=datetime.time(second=30),
                                             reboottime=datetime.time(second=40),
                                             upgradetime=datetime.time(second=50))


class PlatformMushroom(Platform):
    def __init__(self, *args, **kwargs):
        super(PlatformMushroom, self).__init__('mushroom',
                                               startuptime=datetime.time(second=30),
                                               reboottime=datetime.time(second=40),
                                               upgradetime=datetime.time(second=50))


PLATFORMS = {}
for p in [PlatformCetacean(), PlatformRodent(), PlatformLizard(), PlatformMushroom()]:
    PLATFORMS[p.name] = p


def platforms(name=None):
    if name is None:
        return PLATFORMS
    return PLATFORMS[name]
