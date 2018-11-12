import pytest

import testlib


@pytest.mark.login
def test_default_login(sut):
    '''Login and echo a message'''
    with testlib.ssh.sutconnection(sut) as client:
        testlib.util.echo(client)
