package eu.surething_project.core.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityException extends RuntimeException {
    private static final Logger entityLogger = LoggerFactory.getLogger(EntityException.class);

    public EntityException(ErrorMessage errMessage, String classType) {
        super(errMessage.message + " - TYPE: " + classType);
        entityLogger.error(errMessage.message + " - TYPE:" + classType);
    }

    public EntityException(ErrorMessage errMessage) {
        super(errMessage.message);
        entityLogger.error(errMessage.message);
    }

    public EntityException(ErrorMessage errMessage, Exception e) {
        super(errMessage.message, e);
        entityLogger.error(errMessage.message, e);
    }

}
