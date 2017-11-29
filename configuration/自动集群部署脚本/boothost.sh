#!/bin/bash
SERVERS="node1 node2 node4"
PASSWORD=hadoop
BASE_SERVER=192.168.33.202
for SERVER in $SERVERS
do 
scp host.sh root@$SERVER:/root
scp /etc/hosts root@$SERVER:/etc/hosts.back
ssh root@$SERVER /root/host.sh
done
