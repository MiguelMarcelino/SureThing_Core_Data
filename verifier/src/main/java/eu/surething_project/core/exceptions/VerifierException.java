package eu.surething_project.core.exceptions;

import java.util.logging.Logger;

public class VerifierException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	private static final Logger entityLogger = Logger.getLogger(VerifierException.class.getName());

    public VerifierException(ErrorMessage errMessage, String classType) {
        super(errMessage.message + " - TYPE: " + classType);
        entityLogger.severe(errMessage.message + " - TYPE:" + classType);
    }

    public VerifierException(ErrorMessage errMessage) {
        super(errMessage.message);
        entityLogger.severe(errMessage.message);
    }

    public VerifierException(ErrorMessage errMessage, Exception e) {
        super(errMessage.message, e);
        entityLogger.severe(errMessage.message);
    }

}
