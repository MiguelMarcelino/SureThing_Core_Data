# SureThing_Core_Data

This repository contains the central datatypes defined for the SureThing framework.  
The underlying representation format is **protobuf** (Protocol Buffers).

- `data-types/` - contains the protobuf definitions
- `java-marshaller/` - contains Java classes that can represent the types
- `python-marshaller/` - contains Python code to represent the types

## Entities

We present an overview of the main message types defined in `data-types/`.

### Location Claim

The `LocationClaim` represents the claim made by the *Prover* about its location at a specific time.

The *Prover* should sign his claim and produce a `SignedLocationClaim`.

### Location Endorsement

The `LocationEndorsement` represents an endorsement made by a *Witness* of a location claim made by a *Prover*.

The *Witness* should sign his endorsement and produce a `SignedLocationEndorsement`.

### Location Certificate

The `LocationVerification` represents a verification made by a *Verifier* of a location claim made by a *Prover*.
The certificate may or may not reference endorsements.

The *Verifier* should sign his endorsement and produce a `LocationCertificate`.
