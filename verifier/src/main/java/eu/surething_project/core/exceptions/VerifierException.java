package eu.surething_project.core.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifierException extends RuntimeException {
    private static final Logger entityLogger = LoggerFactory.getLogger(VerifierException.class);

    public VerifierException(ErrorMessage errMessage, String classType) {
        super(errMessage.message + " - TYPE: " + classType);
        entityLogger.error(errMessage.message + " - TYPE:" + classType);
    }

    public VerifierException(ErrorMessage errMessage) {
        super(errMessage.message);
        entityLogger.error(errMessage.message);
    }

    public VerifierException(ErrorMessage errMessage, Exception e) {
        super(errMessage.message, e);
        entityLogger.error(errMessage.message, e);
    }

}
