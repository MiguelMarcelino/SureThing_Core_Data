package eu.surething_project.core.exceptions;

public enum ErrorMessage {
    DEFAULT_EXCEPTION_MSG("There was an error in the application"),
    LOCATION_CERTIFICATE_COMM_ERROR("There was an error when receiving a LocationCertificate in prover"),
    LOCATION_ENDORSEMENT_SEND_ERROR("There was an error when sending a LocationEndorsement from prover to verifier"),
    LOCATION_ENDORSEMENT_CONN_ERROR("Error when receiving LocationEndorsement"),
    LOCATION_CLAIM_SEND_ERROR("There was an error when sending a LocationClaim from prover to witness"),
    LOCATION_PROOF_SEND_ERROR("There was an error when sending a LocationProof from prover to verifier"),
    INVALID_PROTOBUF_DATA("Invalid Protocol Buffer data"),

    // Key errors
    ERROR_SIGNING_DATA("Error signing data"),
    ERROR_ENCRYPTING_DATA("Error encrypting data"),
    ERROR_GETTING_KEYSTORE_KEY("Error getting keystore key"),

    GRPC_SERVER_ERROR("There was an error starting the GRPC server"),

    NONCE_MATCH_ERROR("The returned nonce does not match");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
