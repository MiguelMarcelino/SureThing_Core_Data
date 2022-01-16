#!/bin/sh
PROJ_DIR="/home/miguel/Desktop/SIRS/SureThing_Core_Data"
PROTO_SRC_DIR="$PROJ_DIR/data-types/proto"

PROVER_WITNESS_DIR="$PROJ_DIR/prover-witness/src/main/java"
if [ -d "$PROVER_WITNESS_DIR/eu/surething_project/core/grpc" ]; then
    rm -vrf "$PROVER_WITNESS_DIR/eu/surething_project/core/grpc/*"
else
    mkdir "$PROVER_WITNESS_DIR/eu/surething_project/core/grpc"  
fi

VERIFIER_DIR="$PROJ_DIR/verifier/src/main/java/"
if [ -d "$VERIFIER_DIR/eu/surething_project/core/grpc" ]; then
    rm -vrf "$VERIFIER_DIR/eu/surething_project/core/grpc/*"
else
    mkdir "$VERIFIER_DIR/eu/surething_project/core/grpc"  
fi

protoc -I=$PROTO_SRC_DIR --java_out="$PROVER_WITNESS_DIR" $PROTO_SRC_DIR/*.proto
protoc -I=$PROTO_SRC_DIR --java_out="$VERIFIER_DIR" $PROTO_SRC_DIR/*.proto

