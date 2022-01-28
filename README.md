# SureThing Implementation

This repository contains the implementation Group 39. We used **protobuf** (Protocol Buffers) to transmit data between the entities in our system.

## Requirements
We used Maven version 3.8.4 with Java 17 to compile and run our project. We ran VirtualBox version 6.1 and our VMs ran Ubuntu 20.04.

## Folder Structure

- `data-types/` - contains the protobuf definitions
- `prover-witness/` - contains the application for the Prover and the Witness
- `verifier/` - contains the application for the Prover and the Witness
- `db/` - contains the files for the setup of the verifier's database
- `vm_settings/` - contains the setup settings for the VM machines

## Message Types

The entities can be found in the `data-types/` folder. These have been adapted for our project.

### Location Claim

The `LocationClaim` represents the claim made by the *Prover* about its location at a specific time.

The *Prover* should sign his claim and produce a `SignedLocationClaim`.

### Location Endorsement

The `LocationEndorsement` represents an endorsement made by a *Witness* of a location claim made by a *Prover*.

The *Witness* should sign his endorsement and produce a `SignedLocationEndorsement`.

### Location Proof

The `LocationProof` includes the claim made by the *Prover* and the respective endorsements made by the *Witnesses*.

The *Prover* signs this proof and produces a `SignedLocationProof`. This is then sent to the Verifier.

### Location Certificate

The `LocationVerification` represents a verification made by a *Verifier* of a location claim made by a *Prover*.
The certificate may or may not reference endorsements.

The *Verifier* should sign his endorsement and produce a `LocationCertificate`.

## VirtualBox VMs Setup
We chose the `SEED-Ubuntu20.04` VMs to setup our project. The configuration settings we used to create our VMs can be found in the `vm_settings` folder. To create our network configuration, we first run a script to configure the VM networking rules. The command below was run on all VMs and is included in the `setup_script.sh` file:

```
sudo apt update
sudo apt install ifupdown net-tools
```

### Network configuration
In our system we have 4 virtual machines and setup three sub-networks. The first includes the provers and the witnesses, the second the provers and the gateway and the third the gateway and the verifiers. The gateway serves as an access point for the verifier. 
<!-- The internal network names are as follows:
- Prover: 
  - internal-2
  - internal-3
- Witness
  - internal-3
- Verifier
  - internal-1
- Gateway
  - internal-1
  - internal-2
  - nat -->

The setup for the VM interfaces retrieved from the folder `vm_settings/interface_config` can be found below:

Prover VM interface:
```
auto lo
iface lo inet loopback
auto enp0s3
iface enp0s3 inet static
    address 192.168.1.1
    netmask 255.255.255.0
    gateway 192.168.1.254
    dns-nameservers 8.8.8.8 8.8.4.4
auto enp0s8
iface enp0s8 inet static
    address 192.168.2.1
    netmask 255.255.255.0
    dns-nameservers 8.8.8.8 8.8.4.4

```

Witness VM interface:
```
auto lo
iface lo inet loopback
auto enp0s3
iface enp0s3 inet static
    address 192.168.2.2
    netmask 255.255.255.0
    dns-nameservers 8.8.8.8 8.8.4.4
```

Verifier VM interface:
```
auto lo
iface lo inet loopback
auto enp0s3
iface enp0s3 inet static
    address 192.168.0.100
    netmask 255.255.255.0
    gateway 192.168.0.10
    dns-nameservers 8.8.8.8 8.8.4.4
```

Gateway VM interfaces:
```
auto lo
iface lo inet loopback
auto enp0s3
iface enp0s3 inet static
    address 192.168.0.10
    netmask 255.255.255.0
    dns-nameservers 8.8.8.8 8.8.4.4
auto enp0s8
iface enp0s8 inet static
    address 192.168.1.254
    netmask 255.255.255.0
    dns-nameservers 8.8.8.8 8.8.4.4
auto enp0s9
iface enp0s9 inet dhcp
```

### Firewall Configuration

We included several scripts for the setup of the firewall. These can be found in the `vm_settings/firewall_setup` folder. We will briefly cover the noteworthy configurations we have made to the firewalls of each entity:

- For the `FORWARD` and `INPUT` rules our default policy is to drop packets in all VMs
- Both the prover and the witness have the same rules but differ in the ports they use. We accept input connections if they were previously established or if they connect to the port of the server running on the VM (for the prover VM, this is port 8080 and for the verifier VM this is port 8081). For the `OUTPUT` our default policy is to accept the connections, since a prover can communicate with any witnesses surrounding it.
- For the Verifier, the standard `OUTPUT` policy is to drop packets. We add another rule to accept output packets of established connections. The Verifier does not start any connections, it only receives and responds to incoming requests. We only accept input packets with the destination of the verifier's server. Furthermore, we add a loopback rule to accept connections from the database.

## Database
We used a Database for the Verifier to store the Location data that is approved. The database configuration can be found in the `db/` folder. We included an SQL configuration file to configure the database. We also provided a script called `createDB.sh` that can be used to create the database at the verifier VM.

## Generate Keys and Keystores
We include a script called `createCertificates.sh` to generate the keystores, certificates and necessary keys. This can be found in the `pki/` folder. The script only requires setting the password for the exported keystores, the Country Name and the Locality Name. 