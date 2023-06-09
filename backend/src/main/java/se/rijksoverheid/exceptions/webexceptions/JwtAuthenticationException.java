package se.rijksoverheid.exceptions.webexceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when a JWT token is invalid.
 */
public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
