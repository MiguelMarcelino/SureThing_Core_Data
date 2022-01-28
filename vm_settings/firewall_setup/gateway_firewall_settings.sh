#!/bin/bash

# Gateway
iptables -F
iptables -X
iptables -t nat -F
iptables -t nat -X
iptables -t mangle -F
iptables -t mangle -X
iptables -t raw -F
iptables -t raw -X

# Forwarding Rules
iptables -P FORWARD ACCEPT
iptables -F FORWARD
iptables -t nat -F
iptables -t nat -A POSTROUTING  -o enp0s9 -j MASQUERADE
