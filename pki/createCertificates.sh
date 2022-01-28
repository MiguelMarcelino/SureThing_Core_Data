#!/bin/bash
# Docs
# - https://www.openssl.org/docs/manmaster/man1/openssl-pkcs12.html
# - https://www.openssl.org/docs/manmaster/man1/openssl-passphrase-options.html
# - https://docs.oracle.com/javase/7/docs/technotes/tools/solaris/keytool.html

# Data
# - Country Name (Two-letter code): PT
# - Locality Name: Lisbon
# - Common Name: root --> Just for root CA

##################
# Create folder structure
mkdir root
mkdir prover
mkdir witness
mkdir verifier

##################

cd root

# Generate Certificate for CA (root CA)
openssl genrsa -aes256 -out rootCA.key 4096

# Create self-signed Certificate for CA (with 90 day validity)
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 90 -out rootCA.crt -config root.cnf

cd ..

##################

# Verifier
cd verifier
## Generate Keystore
keytool -genkeypair -alias verifier -keyalg RSA -keysize 2048 -storetype JCEKS -keystore keystore_verifier.jks
## Generate certificate signing request (CSR)
keytool -certreq -alias verifier -file verifier.csr -keystore keystore_verifier.jks
## Sign with root CA certificate
openssl x509 -req -in verifier.csr -CA ../root/rootCA.crt -CAkey ../root/rootCA.key -CAcreateserial -out verifier.crt -days 90 -sha256 -extfile verifier.ext
## Export private key
### Start by converting keystore to p12 format
keytool -importkeystore \
    -srckeystore keystore_verifier.jks \
    -destkeystore keystore_verifier.p12 \
    -deststoretype PKCS12 \
    -srcalias verifier \
    -deststorepass 12345678 \
    -destkeypass 12345678
### Export key with openssl (google does not allow encrypted keys)
# openssl pkcs12 -in keystore_verifier.p12 -passin pass:12345678 -nocerts -out key_verifier.pem

### Not encrypted
openssl pkcs12 -in keystore_verifier.p12 -nodes -nocerts -out key_verifier.pem

cd ..

##################

# Prover
cd prover
## Generate Keystore
keytool -genkeypair -alias prover -keyalg RSA -keysize 2048 -storetype JCEKS -keystore keystore_prover.jks
## Generate certificate signing request (CSR)
keytool -certreq -alias prover -file prover.csr -keystore keystore_prover.jks
## Sign with root CA certificate
openssl x509 -req -in prover.csr -CA ../root/rootCA.crt -CAkey ../root/rootCA.key -CAcreateserial -out prover.crt -days 90 -sha256 -extfile prover.ext
## Export private key
### Start by converting keystore to p12 format
keytool -importkeystore \
    -srckeystore keystore_prover.jks \
    -destkeystore keystore_prover.p12 \
    -deststoretype PKCS12 \
    -srcalias prover \
    -deststorepass 12345678 \
    -destkeypass 12345678
### Export key with openssl (google does not allow encrypted keys)
# openssl pkcs12 -in keystore_prover.p12 -passin pass:12345678 -nocerts -out key_prover.pem

### Not encrypted
openssl pkcs12 -in keystore_prover.p12 -nodes -nocerts -out key_prover.pem

cd ..

##################

# Witness
cd witness
## Generate Keystore
keytool -genkeypair -alias witness -keyalg RSA -keysize 2048 -storetype JCEKS -keystore keystore_witness.jks
## Generate certificate signing request (CSR)
keytool -certreq -alias witness -file witness.csr -keystore keystore_witness.jks
## Sign with root CA certificate
openssl x509 -req -in witness.csr -CA ../root/rootCA.crt -CAkey ../root/rootCA.key -CAcreateserial -out witness.crt -days 90 -sha256 -extfile witness.ext
## Export private key
### Start by converting keystore to p12 format
keytool -importkeystore \
    -srckeystore keystore_witness.jks \
    -destkeystore keystore_witness.p12 \
    -deststoretype PKCS12 \
    -srcalias witness \
    -deststorepass 12345678 \
    -destkeypass 12345678
### Export key with openssl (google does not allow encrypted keys)
# openssl pkcs12 -in keystore_witness.p12 -passin pass:12345678 -nocerts -out key_witness.pem

### Not encrypted
openssl pkcs12 -in keystore_witness.p12 -nodes -nocerts -out key_witness.pem

cd ..
