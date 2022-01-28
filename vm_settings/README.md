# Setup for the VM network
This folder contains general setup information for our VMs. The `interface_config` includes the interface information for our VMs
As we are using Ubuntu 20, we needed to execute a setup script, which is the file called setup_script.sh.
The file `vms.txt` includes basic configuration information for the VMs and some of the used comands. 


## Network configuration
In our system we have 4 virtual machines and setup three sub-networks. The first includes the provers and the witnesses, the second the provers and the gateway and the third the gateway and the verifiers. The gateway serves as an access point for the verifier. 
The setup for the VM interfaces can be found in the `interface_config` folder.

## Firewall Configuration

We included several scripts for the setup of the firewall. These can be found in the `firewall_setup` folder. We will briefly cover the noteworthy configurations we have made to the firewalls of each entity:

- For the `FORWARD` and `INPUT` rules our default policy is to drop packets in all VMs
- Both the prover and the witness have the same rules but differ in the ports they use. We accept input connections if they were previously established or if they connect to the port of the server running on the VM (for the prover VM, this is port 8080 and for the verifier VM this is port 8081). For the `OUTPUT` our default policy is to accept the connections, since a prover can communicate with any witnesses surrounding it.
- For the Verifier, the standard `OUTPUT` policy is to drop packets. We add another rule to accept output packets of established connections. The Verifier does not start any connections, it only receives and responds to incoming requests. We only accept input packets with the destination of the verifier's server. Furthermore, we add a loopback rule to accept connections from the database.
