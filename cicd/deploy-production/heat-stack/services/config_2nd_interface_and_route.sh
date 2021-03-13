#!/bin/bash

sudo cat >/etc/sysconfig/network-scripts/ifcfg-eth1 <<EOF
BOOTPROTO=static
DEVICE=eth1
HWADDR=$mac_addr
ONBOOT=yes
TYPE=Ethernet
IPADDR=$ip_addr
NETMASK=255.255.255.0
NM_CONTROLLER=no
EOF

sudo cat >/etc/sysconfig/network-scripts/route-eth1<<EOF
203.190.170.229/32 via 171.255.192.254 dev eth1
EOF

sudo ifdown ifcfg-eth1 && sudo ifup ifcfg-eth1
sudo systemctl restart network
