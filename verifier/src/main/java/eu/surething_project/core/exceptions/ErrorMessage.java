package eu.surething_project.core.exceptions;

public enum ErrorMessage {
    DEFAULT_EXCEPTION_MSG("There was an error in the application");

    public final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
