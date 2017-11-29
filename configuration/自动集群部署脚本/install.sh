#!/bin/bash
BASE_SERVER=192.168.33.202
rm -f /etc/yum.repos.d/*.repo
touch /etc/yum.repos.d/CentOS-URL.repo
cat >> /etc/yum.repos.d/CentOS-URL.repo <<EOF
[base-source]
name=CentOS-$releasever - Base Sources
baseurl=http://192.168.33.202/rom/
gpgcheck=0
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
EOF
yum list |grep wget
yum install -y wget
wget $BASE_SERVER/soft/jdk-7u45-linux-x64.tar.gz
tar -zxvf jdk-7u45-linux-x64.tar.gz -C /usr/local/
cat >> /etc/profile <<EOF
export JAVA_HOME=/usr/local/jdk1.7.0_45
export PATH=\$PATH:\$JAVA_HOME/bin
EOF
