#!/bin/bash

# Verifier - 8082

# Clean Rules
iptables -F
iptables -X
iptables -t nat -F
iptables -t nat -X
iptables -t mangle -F
iptables -t mangle -X
iptables -t raw -F
iptables -t raw -X

# Standard Policy: Drop
iptables -P FORWARD DROP
iptables -P INPUT DROP
iptables -P OUTPUT DROP

# Accept previously established connections
iptables -A OUTPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

#Filter Table 
iptables -A INPUT -p tcp --dport 8082 -j ACCEPT
iptables -A INPUT -i lo -j ACCEPT
iptables -A OUTPUT -o lo -j ACCEPT


