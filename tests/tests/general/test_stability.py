import datetime
import logging
import pytest


@pytest.mark.stability
@pytest.mark.service
def test_system_service_status_is_good(sshconn):
    '''System service status is good'''
    stdin, stdout, stderr = sshconn.exec_command('systemctl status --no-pager')
    assert stdout.channel.recv_exit_status() == 0

    lines = stdout.readlines()
    assert lines
    assert lines[1].strip() == 'State: running'
    assert lines[2].strip() == 'Jobs: 0 queued'
    assert lines[3].strip() == 'Failed: 0 units'


@pytest.mark.stability
@pytest.mark.fs
def test_essential_fs_are_mounted(sshconn):
    '''Essential filesystems are mounted'''
    stdin, stdout, stderr = sshconn.exec_command('mount')
    assert stdout.channel.recv_exit_status() == 0

    lines = stdout.readlines()
    assert lines

    patterns = ['devtmpfs on /dev type devtmpfs',
                'sysfs on /sys type sysfs',
                'proc on /proc type proc']
    for pattern in patterns:
        count = sum(1 for line in lines if pattern in line)
        assert count == 1


@pytest.mark.stability
@pytest.mark.perf
@pytest.mark.startup
def test_system_startup_time(platform, sshconn):
    '''System startup time is reasonable'''
    stdin, stdout, stderr = sshconn.exec_command('systemd-analyze')
    assert stdout.channel.recv_exit_status() == 0

    lines = stdout.readlines()
    assert len(lines) == 1

    # Startup finished in 2.820s (kernel) + 488ms (userspace) = 3.309s
    time = lines[0].strip().split()[-1]
    assert time
    assert datetime.datetime.strptime(time, '%S.%fs').time() < platform.startuptime
