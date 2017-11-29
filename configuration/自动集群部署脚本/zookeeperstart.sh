#!/bin/bash
for i in 2 3 4
do 
ssh node$i "source /etc/profile;/root/apps/zookeeper-3.4.5/bin/zkServer.sh start"
done
