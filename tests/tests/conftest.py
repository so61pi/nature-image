from datetime import datetime
from py.xml import html
import contextlib
import logging
import pytest
import subprocess

import testlib


################################################################################
#
# Format HTML report.
#
################################################################################
@pytest.mark.optionalhook
def pytest_html_results_table_header(cells):
    cells.insert(2, html.th('Description'))
    cells.insert(1, html.th('Time', class_='sortable time', col='time'))
    cells.pop()


@pytest.mark.optionalhook
def pytest_html_results_table_row(report, cells):
    cells.insert(2, html.td(report.description))
    cells.insert(1, html.td(datetime.utcnow(), class_='col-time'))
    cells.pop()


@pytest.mark.hookwrapper
def pytest_runtest_makereport(item, call):
    outcome = yield
    report = outcome.get_result()
    report.description = str(item.function.__doc__)



################################################################################
#
# pytest args.
#
################################################################################
def pytest_addoption(parser):
    parser.addoption('--x-platform-name',
                     action='store',
                     default=None,
                     required=True,
                     help='Name of platform under test')

    # sut
    parser.addoption('--x-sut-hostname',
                     action='store',
                     default=None,
                     required=True,
                     help='Hostname or IP address of SUT')
    parser.addoption('--x-sut-sshport',
                     action='store',
                     default=22,
                     help='SUT SSH port')
    parser.addoption('--x-sut-username',
                     action='store',
                     default='root',
                     help='SUT username')
    parser.addoption('--x-sut-password',
                     action='store',
                     default='root',
                     help='SUT password')

    # fwhost
    parser.addoption('--x-fwhost-on',
                     action='store_true',
                     default=False,
                     help='Enable internal firmware host')
    parser.addoption('--x-fwhost-port',
                     action='store',
                     default=None,
                     help='Internal firmware host port')
    parser.addoption('--x-fwhost-directory',
                     action='store',
                     default=None,
                     help='Internal firmware host directory')

    # fwlink
    parser.addoption('--x-fwlink',
                     action='store',
                     default=None,
                     required=True,
                     help='Link to firmware')

    # server
    parser.addoption('--x-server-ping',
                     action='store',
                     default=None,
                     required=True,
                     help='IP address of ping server')

    # qemu
    parser.addoption('--x-qemu-on',
                     action='store_true',
                     default=False,
                     help='Enable QEMU')
    parser.addoption('--x-qemu-diskpath',
                     action='store',
                     default=None,
                     help='QEMU disk')
    parser.addoption('--x-qemu-sshport',
                     action='store',
                     default=50022,
                     help='QEMU ssh port')
    parser.addoption('--x-qemu-ramsize',
                     action='store',
                     default='512M',
                     help='QEMU ram size')


def pytest_generate_tests(metafunc):
    if 'platform_name' in metafunc.fixturenames:
        metafunc.parametrize('platform_name', [metafunc.config.option.x_platform_name], scope='session')

    # sut
    if 'sut' in metafunc.fixturenames:
        sut = {'hostname' : metafunc.config.option.x_sut_hostname,
               'sshport' : metafunc.config.option.x_sut_sshport,
               'username' : metafunc.config.option.x_sut_username,
               'password' : metafunc.config.option.x_sut_password}
        metafunc.parametrize('sut', [sut], scope='session')

    # fwhost
    if 'fwhost_on' in metafunc.fixturenames:
        metafunc.parametrize('fwhost_on', [metafunc.config.option.x_fwhost_on], scope='session')
    if 'fwhost_port' in metafunc.fixturenames:
        metafunc.parametrize('fwhost_port', [metafunc.config.option.x_fwhost_port], scope='session')
    if 'fwhost_directory' in metafunc.fixturenames:
        metafunc.parametrize('fwhost_directory', [metafunc.config.option.x_fwhost_directory], scope='session')

    # fwlink
    if 'fwlink' in metafunc.fixturenames:
        metafunc.parametrize('fwlink', [metafunc.config.option.x_fwlink], scope='session')

    # servers
    if 'servers' in metafunc.fixturenames:
        servers = {'ping' : metafunc.config.option.x_server_ping}
        metafunc.parametrize('servers', [servers], scope='session')

    # qemu
    if 'qemu_on' in metafunc.fixturenames:
        metafunc.parametrize('qemu_on', [metafunc.config.option.x_qemu_on], scope='session')
    if 'qemu_diskpath' in metafunc.fixturenames:
        metafunc.parametrize('qemu_diskpath', [metafunc.config.option.x_qemu_diskpath], scope='session')
    if 'qemu_sshport' in metafunc.fixturenames:
        metafunc.parametrize('qemu_sshport', [metafunc.config.option.x_qemu_sshport], scope='session')
    if 'qemu_ramsize' in metafunc.fixturenames:
        metafunc.parametrize('qemu_ramsize', [metafunc.config.option.x_qemu_ramsize], scope='session')


################################################################################
#
# fixtures.
#
################################################################################
@contextlib.contextmanager
def runbg(args, cwd=None, logpath='/dev/null'):
    with open(logpath, 'w+') as f, subprocess.Popen(args, stdin=subprocess.DEVNULL,
                                                    stdout=f, stderr=f, cwd=cwd) as p:
        logging.debug(args)
        yield p
        p.terminate()


@pytest.fixture(scope='session', autouse=True)
def qemu(qemu_on, qemu_diskpath, qemu_ramsize, qemu_sshport):
    '''
    This fixture starts QEMU at the beginning of the test session.
    '''
    if not qemu_on:
        logging.info('QEMU will not run')
        yield None
    else:
        with runbg(['qemu-system-x86_64',
                    '-nographic',
                    '-enable-kvm',
                    '-cpu', 'host',
                    '-bios', '/usr/share/qemu/OVMF.fd',
                    '-drive', 'file={},if=virtio,format=qcow2'.format(qemu_diskpath),
                    '-m', qemu_ramsize,
                    '-redir', 'tcp:{}::22'.format(qemu_sshport)],
                   logpath='/tmp/qemu.log') as p:
            testlib.util.wait(20, 'QEMU is starting')
            yield p


@pytest.fixture(scope='session', autouse=True)
def fwhost(fwhost_on, fwhost_port, fwhost_directory):
    '''
    Starts an HTTP server.
    '''
    if not fwhost_on:
        logging.info('Internal firmware host is not activated')
        yield None
    else:
        with runbg(['python3', '-m', 'http.server', fwhost_port],
                   cwd=fwhost_directory, logpath='/tmp/httpserver.log') as p:
            testlib.util.wait(3, 'http.server is starting')
            yield p


@pytest.fixture
def platform(platform_name):
    '''
    Get the current platform under test data.
    '''
    return testlib.platform.platforms(platform_name)


@pytest.fixture
def sshconn(sut):
    '''
    Create an SSH connection.
    '''
    with testlib.ssh.sutconnection(sut) as client:
        yield client
