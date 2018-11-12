import datetime
import logging
import time


def wait(seconds, msg, f=3):
    assert f > 0
    assert msg

    if isinstance(seconds, int):
        pass
    elif isinstance(seconds, datetime.time):
        seconds = (seconds.hour*60 + seconds.minute)*60 + seconds.second
    elif isinstance(seconds, datetime.timedelta):
        seconds = int(seconds.total_seconds())
    else:
        assert False, 'Invalid type for time'

    assert seconds >= 0

    while seconds > 0:
        logging.info('Waiting {}s - {}'.format(seconds, msg))
        time.sleep(f if seconds > f else seconds)
        seconds -= f


def echo(sshconn, msg='Hello World!'):
    stdin, stdout, stderr = sshconn.exec_command('echo {}'.format(msg))
    assert stdout.channel.recv_exit_status() == 0
    assert stdout.read().decode('utf-8').strip() == msg


def reboot(sshconn, time=None):
    stdin, stdout, stderr = sshconn.exec_command('/usr/sbin/shutdown -r +1')
    assert stdout.channel.recv_exit_status() == 0
    assert 'Shutdown scheduled for ' in stderr.read().decode('utf-8')

    stdin, stdout, stderr = sshconn.exec_command('/usr/sbin/shutdown -r now')
    wait(time if time is not None else 0, 'System is rebooting')
