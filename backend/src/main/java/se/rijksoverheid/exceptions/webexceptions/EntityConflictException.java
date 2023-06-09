package se.rijksoverheid.exceptions.webexceptions;

/**
 * Exception thrown when user wants to create an entity that already exists.
 */
public class EntityConflictException extends RuntimeException {
    public EntityConflictException(String msg) {
        super(msg);
    }
}
