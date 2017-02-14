beaker-manager
===============================

A Custom Jupyter Widget Library

Installation
------------

To install use pip:

    $ pip install beakermanager
    $ jupyter nbextension enable --py --sys-prefix beakermanager


For a development installation (requires npm),

    $ git clone https://github.com/Beaker/beaker-manager.git
    $ cd beaker-manager
    $ pip install -e .
    $ jupyter nbextension install --py --symlink --sys-prefix beakermanager
    $ jupyter nbextension enable --py --sys-prefix beakermanager
