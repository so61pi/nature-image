import logging
import pytest

import testlib


@pytest.mark.firmware
def test_firmware_upgrading(platform, sut, fwlink):
    '''Firmware upgrading'''
    logging.info('Reboot system...')
    with testlib.ssh.sutconnection(sut) as client:
        testlib.util.reboot(client, time=platform.reboottime)

    logging.info('Upgrade firmware')
    with testlib.ssh.sutconnection(sut) as client:
        stdin, stdout, stderr = client.exec_command('PATH=/usr/sbin:/usr/bin:/sbin:/bin swupdate-net {}'.format(fwlink))
        assert stdout.channel.recv_exit_status() == 0

    testlib.util.wait(platform.reboottime, 'System is rebooting')

    with testlib.ssh.sutconnection(sut) as client:
        testlib.util.echo(client)
