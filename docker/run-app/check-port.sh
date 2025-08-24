#!/bin/bash

sudo lsof -i -P -n | grep -E "8080|8088"
# docker-pr  8615            root    5u  IPv4 684982      0t0  TCP 172.20.0.1:39462->172.20.0.2:8080 (ESTABLISHED)
sudo iptables -L -n | grep -E "8080|8088"
# ACCEPT     tcp  --  0.0.0.0/0            172.20.0.2           tcp dpt:8080
sudo iptables -A INPUT -p tcp --dport 8088 -j ACCEPT