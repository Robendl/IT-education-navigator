package se.rijksoverheid.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.EntityConflictException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.config.JwtTokenUtil;
import se.rijksoverheid.security.dto.UserChangePasswordRequestDTO;
import se.rijksoverheid.security.dto.UserRequestDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;
import se.rijksoverheid.security.model.User;

import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService mockUserService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtTokenUtil jwtTokenUtil;
    @Mock
    private UserChangePasswordRequestDTO mockUserChangePasswordRequest;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void testRegisterUser_Success() {
        String email = "test@test.com";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(email);
        when(mockUserService.existsByUsername(email)).thenReturn(false);
        when(mockUserService.save(mockUserRequest)).thenReturn(mockUserRequest);
        assertEquals(ResponseEntity.status(HttpStatus.CREATED).build(),authenticationController.registerUser(mockUserRequest));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        String email = "test@test.com";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(email);
        when(mockUserService.existsByUsername(email)).thenReturn(true);
        assertThrows(EntityConflictException.class, ()-> authenticationController.registerUser(mockUserRequest));
    }

    @Test
    void testRegisterUser_InvalidEmail() {
        String email = "test";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(email);
        when(mockUserService.existsByUsername(email)).thenReturn(false);
        doThrow(new BadRequestException("")).when(mockUserService).checkEmailAddress(email);
        assertThrows(BadRequestException.class, () -> authenticationController.registerUser(mockUserRequest));
    }

    @Test
    void testCreateAuthenticationToken() {
        String username = "email@email.com";
        HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        User.Role mockRole = mock(User.Role.class);
        User mockUser = mock(User.class);
        when(mockUser.getRole()).thenReturn(mockRole);
        when(mockUserService.loadUserByUsername(username)).thenReturn(mockUser);
        when(mockUserRequest.getUsername()).thenReturn(username);
        assertEquals(mockRole, Objects.requireNonNull(authenticationController.createAuthenticationToken(mockUserRequest, mockHttpServletResponse).getBody()).getRole());
    }

    @Test
    void testChangeUserPasswordSuccess() {
        UserResponseDTO mockUserResponse = mock(UserResponseDTO.class);
        when(mockUserService.changePassword(mockUserChangePasswordRequest)).thenReturn(mockUserResponse);
        assertEquals(ResponseEntity.ok(mockUserResponse), authenticationController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testChangeUserPasswordUserDisabled() {
        when(mockUserService.changePassword(mockUserChangePasswordRequest)).thenThrow(DisabledException.class);
        assertThrows(Exception.class, ()-> authenticationController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testChangeUserPasswordBadCredentials() {
        when(mockUserService.changePassword(mockUserChangePasswordRequest)).thenThrow(BadCredentialsException.class);
        assertThrows(Exception.class, ()-> authenticationController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testChangeUserPasswordUsernameNotFound() {
        when(mockUserService.changePassword(mockUserChangePasswordRequest)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, ()-> authenticationController.changeUserPassword(mockUserChangePasswordRequest));
    }
}
