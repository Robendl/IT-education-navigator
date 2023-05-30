package se.rijksoverheid.exceptions.webexceptions;

public class EntityConflictException extends RuntimeException {
    public EntityConflictException(String msg) {
        super(msg);
    }
}
