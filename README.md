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

## Location schemes

The SureThing framework supports a set of alternative location schemes.
This is represented with the protobuf `oneof` construct.

SureThing locations are extensible but only with new releases of the core library.
Once a new location alternative is added, it can be recognized by new applications, but existing applications can safely ignore them.
This is because of protobuf design that allows the safe addition of new fields.

## Time schemes

The SureThing framework also supports a set of alternative time schemes.
Again, this is represented with the protobuf `oneof` construct.

SureThing time descriptiosn are extensible but only with new releases of the core library.

This designed choice was done to accomodate change, but at a slow pace.
This adds programmer convenience with the marshalled types.

## Identity schemes

The SureThing entities -- Prover, Witness, Verifier -- are represented by a `string` identifier that can be assumed to be **unique** in the application scope.
The identity can be a number, a textual name, a [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier), etc.
Despite this enumeration there are no predefined types for these identities and it will be up to the application to make sense of the identifier and its internal structure.
For this reason, the identifiers do not carry an identity type discriminant.
It is assumed that the identifier type can be recognized by syntax of the text it contains.

Example numeric identifier: `2207`  
Example textual identifier: `Alice Sure`  
Example UUID: `123e4567-e89b-12d3-a456-426614174000`.

If we do not want to provide an identifier, then the empty string value should be used "".

When using a name, it does not have to be the actual name of the entity/person, it can a pseudonym.

## Any evidence

This was not the same approach as taken with time and location (`oneof`), as evidence types are expected to be more dynamic and application-specific, and, in these cases, we could not way for an alternative to be added to the core framework definitions.

The SureThing framework supports any kind of evidence.
The way this information is represented is by two fields: the *type* string and the any field.
The type string is the fully-qualified protobuf name of the evidence type, e.g., "eu.surething_project.core.wi_fi.WiFiNetworksEvidence", this allows the application to check if it recognizes the type of evidence, and, if so, unpack it and use it.

## Representing lack of information (null)

For a string field, the lack of information is represented as the empty string "".

For a time field, the lack of information is represented as the Empty message (`google.protobuf.Empty`).

For a evidence type field, which is a string, we represent the absence of evidence using the "".

Reference: <https://itnext.io/protobuf-and-null-support-1908a15311b6>
