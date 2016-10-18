#!/bin/bash
# This script starts up 4 clients to connect to a game server
# on the current machine - edit the parameters to specify
# the host and port number.
for i in 0 1 2 3;
do
    echo Running client $i
    java TestGame --connect $HOSTNAME --port 22222 &
    sleep 1
done
