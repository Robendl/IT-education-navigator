package se.rijksoverheid.exceptions.webexceptions;

/**
 * Exception thrown when a request is malformed.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) {
        super(msg);
    }
}
