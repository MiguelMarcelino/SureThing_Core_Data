#!/bin/bash
#  Cortesy of https://gist.github.com/fntlnz/cf14feb5a46b2eda428e000157447309

cd certificates

# Data
# - Password: 12345678
# - Country Name (Two-letter code): PT
# - Locality Name: Lisbon
# - Common Name: root --> Just for root CA

##################

# Generate Certificate for CA (root CA)
openssl genrsa -aes256 -out rootCA.key 4096 # -subj "/C=PT/ST=CA/O=root_org"

# Create self-signed Certificate for CA (with 90 day validity)
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 90 -out rootCA.crt

##################

# Verifier
# Generate Keystore
keytool -genkeypair -alias verifier -keyalg RSA -keysize 2048 -storetype JCEKS -keystore keystore_verifier.jks
# Generate certificate signing request (CSR)
keytool -certreq -alias verifier -file verifier.csr -keystore keystore_verifier.jks
# Sign with root CA certificate
openssl x509 -req -in verifier.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial -out verifier.crt -days 90 -sha256


##################

# Prover
# Generate Keystore
keytool -genkeypair -alias prover -keyalg RSA -keysize 2048 -storetype JCEKS -keystore keystore_prover.jks
# Generate certificate signing request (CSR)
keytool -certreq -alias prover -file prover.csr -keystore keystore_prover.jks
# Sign with root CA certificate
openssl x509 -req -in prover.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial -out prover.crt -days 90 -sha256

##################

# Witness
# Generate Keystore
keytool -genkeypair -alias witness -keyalg RSA -keysize 2048 -storetype JCEKS -keystore keystore_witness.jks
# Generate certificate signing request (CSR)
keytool -certreq -alias witness -file witness.csr -keystore keystore_witness.jks
# Sign with root CA certificate
openssl x509 -req -in witness.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial -out witness.crt -days 90 -sha256