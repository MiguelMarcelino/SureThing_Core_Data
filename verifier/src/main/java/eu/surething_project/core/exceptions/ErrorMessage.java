package eu.surething_project.core.exceptions;

public enum ErrorMessage {
    DEFAULT_EXCEPTION_MSG("There was an error in the application"),
    LOCATION_ENDORSEMENT_CONN_ERROR("Error when receiving LocationEndorsement"),
    GRPC_SERVER_ERROR("There was an error starting the GRPC server");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
