import os


def prjroot():
    return os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
