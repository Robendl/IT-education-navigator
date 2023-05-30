package se.rijksoverheid.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;

@ControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("TEEEEEEEST");
    }
}
