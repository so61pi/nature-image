import pytest


@pytest.mark.network
def test_ping(sshconn, servers):
    '''Ping successfully without losing packets'''
    stdin, stdout, stderr = sshconn.exec_command('ping -c 4 {}'.format(servers['ping']))
    assert stdout.channel.recv_exit_status() == 0
    assert '4 packets transmitted, 4 received, 0% packet loss' in stdout.read().decode('utf-8')
