package eu.surething_project.core.exceptions;

public enum ErrorMessage {
    DEFAULT_EXCEPTION_MSG("There was an error in the application"),
    LOCATION_CERTIFICATE_COMM_ERROR("There was an error when receiving a LocationCertificate in prover"),
    LOCATION_ENDORSEMENT_SEND_ERROR("There was an error when sending a LocationEndorsement from prover to verifier"),
    LOCATION_CLAIM_SEND_ERROR("There was an error when sending a LocationClaim from prover to witness"),
    INVALID_PROTOBUF_DATA("Invalid Protocol Buffer data");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
