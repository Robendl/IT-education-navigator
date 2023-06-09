package se.rijksoverheid.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.EntityConflictException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WebExceptionHandlerTest {
    @InjectMocks WebExceptionHandler handler;

    @Test
    void testHandleBadRequestException() {
        String errorMessage = "test message";
        BadRequestException exception = new BadRequestException(errorMessage);
        ResponseEntity<String> response = handler.handleBadRequestException(exception);
        assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage), response);
    }

    @Test
    void testHandleEntityConflictException() {
        String errorMessage = "entity conflict";
        EntityConflictException exception = new EntityConflictException(errorMessage);
        ResponseEntity<String> response = handler.handleEntityConflictException(exception);
        assertEquals(ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage), response);
    }

    @Test
    void testHandleNotFoundException() {
        String errorMessage = "not found";
        NotFoundException exception = new NotFoundException(errorMessage);
        ResponseEntity<String> response = handler.handleNotFoundException(exception);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage), response);
    }
}
