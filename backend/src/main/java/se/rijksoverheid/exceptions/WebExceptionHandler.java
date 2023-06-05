package se.rijksoverheid.exceptions;

import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.rijksoverheid.exceptions.webexceptions.EntityConflictException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;

@ControllerAdvice
@Slf4j
public class WebExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthException(Exception e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabledException() {
        log.info("disabled account");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This account has been disabled.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(Exception e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email-address and/or password.");
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<String> handleEntityConflictException(Exception e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
    }
}
