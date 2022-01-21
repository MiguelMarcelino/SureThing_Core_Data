package eu.surething_project.core.exceptions;

import java.util.logging.Logger;

public class EntityException extends RuntimeException {
    private static final Logger entityLogger = Logger.getLogger(EntityException.class.getName());

    public EntityException(ErrorMessage errMessage, String classType) {
        super(errMessage.message + " - TYPE: " + classType);
        entityLogger.severe(errMessage.message + " - TYPE:" + classType);
    }

    public EntityException(ErrorMessage errMessage) {
        super(errMessage.message);
        entityLogger.severe(errMessage.message);
    }

    public EntityException(ErrorMessage errMessage, Exception e) {
        super(errMessage.message, e);
        entityLogger.severe(errMessage.message);
    }

}
