package se.rijksoverheid.exceptions.webexceptions;

/**
 * Exception thrown when a requested entity is not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) {
        super(msg);
    }
}
