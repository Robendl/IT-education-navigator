package se.rijksoverheid.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.UserRequestDTO;
import se.rijksoverheid.security.model.User;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void testRegisterUser_Success() {
        String email = "test@test.com";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(email);
        when(mockUserService.existsByUsername(email)).thenReturn(false);
        when(mockUserService.save(any(UserRequestDTO.class))).thenReturn(mockUserRequest);
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).build(),authenticationController.registerUser(mockUserRequest));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        String email = "test@test.com";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(email);
        when(mockUserService.existsByUsername(email)).thenReturn(true);
        assertEquals("Emailadres wordt al gebruikt",authenticationController.registerUser(mockUserRequest).getBody());
    }

    @Test
    void testRegisterUser_InvalidEmail() {
        String email = "test";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(email);
        when(mockUserService.existsByUsername(email)).thenReturn(false);
        assertEquals("Geen geldig emailadres ingevoerd",authenticationController.registerUser(mockUserRequest).getBody());
    }

    @Test
    void testCreateAuthenticationToken() {
        String username = "email@email.com";
        HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        User.Role mockRole = mock(User.Role.class);
        when(mockUserRequest.getUsername()).thenReturn(username);

        try {
            assertEquals(mockRole, authenticationController.createAuthenticationToken(mockUserRequest, mockHttpServletResponse));

        } catch (Exception e) {

        }
    }
}
