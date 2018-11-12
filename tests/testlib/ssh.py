import paramiko


def connection(hostname, port, username, password):
    '''Create a SSH connection to the target'''
    client = paramiko.client.SSHClient()
    client.set_missing_host_key_policy(paramiko.client.AutoAddPolicy())
    client.connect(hostname=hostname, port=port, username=username, password=password)
    return client


def sutconnection(sut):
    return connection(sut['hostname'], sut['sshport'],
                      sut['username'], sut['password'])
