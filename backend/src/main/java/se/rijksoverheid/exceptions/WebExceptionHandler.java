package se.rijksoverheid.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.EntityConflictException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;

/**
 * Exception handler for web exceptions.
 */
@ControllerAdvice
@Slf4j
public class WebExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<String> handleEntityConflictException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
