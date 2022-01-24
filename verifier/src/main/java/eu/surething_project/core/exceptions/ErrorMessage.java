package eu.surething_project.core.exceptions;

public enum ErrorMessage {
    DEFAULT_EXCEPTION_MSG("There was an error in the application"),
    LOCATION_CERTIFICATE_COMM_ERROR("There was an error when receiving a LocationCertificate in prover"),
    LOCATION_ENDORSEMENT_SEND_ERROR("There was an error when sending a LocationEndorsement from prover to verifier"),
    LOCATION_ENDORSEMENT_CONN_ERROR("Error when receiving LocationEndorsement"),
    LOCATION_CLAIM_SEND_ERROR("There was an error when sending a LocationClaim from prover to witness"),
    LOCATION_PROOF_SEND_ERROR("There was an error when sending a LocationProof from prover to verifier"),
    INVALID_PROTOBUF_DATA("Invalid Protocol Buffer data"),

    // Properties errors
    ERROR_READING_PROPERTIES("Error reading properties file"),

    // Security errors
    ERROR_SIGNING_DATA("Error signing data"),
    ERROR_ENCRYPTING_DATA("Error encrypting data"),
    ERROR_GETTING_KEYSTORE_KEY("Error getting keystore key"),
    ERROR_GETTING_CERTIFICATE("Error getting requested certificate"),
    NONCE_MATCH_ERROR("The returned nonce does not match"),
    ERROR_VERIFYING_DATA("Error verifying data"),

    // gRPC errors
    GRPC_SERVER_ERROR("There was an error starting the GRPC server"),
    GRPC_CONNECTION_ERROR("There was an error when trying to establish a connection"),

    // Parsing errors
    INVALID_ARGS_LENGTH("Invalid number of Arguments"),
    INVALID_ARGS_DATA("Invalid argument data was supplied"),

    // File Reading errors
    ERROR_READING_ENTITIES_FILE("Error Reading Entities file"),
    ERROR_CLOSING_ENTITIES_FILE("Error closing Entities file"),
    ERROR_READING_CERTIFICATE("Error reading certificate file"),
    ERROR_CREATING_CERTIFICATE("Error creating Certificate file"),
    ERROR_GETTING_KEY_FILE("Error getting key file"),

    // Database errors
    DATABASE_CONNECTION_EXCEPTION("Error creating Database connection"),
    ERROR_CLOSING_DB_CONNECTION("Error closing Database connection"),
    ERROR_ACCESSING_SQL_TABLE("Error accessing SQL table"),
    ERROR_CREATING_PROOFS_TABLE("Error creating Proofs table"),
    ERROR_CREATING_ENDORSEMENTS_TABLE("Error creating Endorsements table"),
    ERROR_CREATING_CLAIMS_TABLE("Error creating Claims table"),
    ERROR_CLOSING_DB_STATEMENT("Error closing Database statement"),
    ERROR_CLOSING_RESULTSET("Error closing ResultSet");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
