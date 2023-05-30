package se.rijksoverheid.exceptions.webexceptions;

public class BadRequestException extends Exception {
    public BadRequestException(String msg) {
        super(msg);
    }
}
