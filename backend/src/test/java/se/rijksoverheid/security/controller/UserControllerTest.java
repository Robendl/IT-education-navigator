package se.rijksoverheid.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.UserChangePasswordRequestDTO;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserResetPasswordResponseDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private UserService mockUserService;
    private UserController userController;


    @BeforeEach
    void setUp() {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        mockUserService = mock(UserService.class);
        userController = new UserController(mockAuthenticationManager, mockUserService);
    }

    @Test
    void testGetUsers() {
        String search = "";
        int page = 0,size = 500;
        Sort.Direction direction = Sort.Direction.ASC;

        UserResponseDTO mockUserResponse = mock(UserResponseDTO.class);
        List<UserResponseDTO> users = new ArrayList<>();
        users.add(mockUserResponse);

        when(mockUserService.getUsers(anyString(), any(Pageable.class))).thenReturn(users);

        assertEquals(users, userController.getUsers(search, page, size, direction).getBody());
    }

    @Test
    void testEditUserPermissions() {
        long id = 1;
        UserPermRequestDTO mockUserPermRequest = mock(UserPermRequestDTO.class);
        UserResponseDTO mockUserResponse = mock(UserResponseDTO.class);
        when(mockUserService.editUserPerms(id,mockUserPermRequest)).thenReturn(mockUserResponse);
        assertDoesNotThrow(() -> {
            userController.editUserPermissions(id, mockUserPermRequest);
        });
    }

    @Test
    void testEditNonExistingUserPermissions() {
        long id = 1;
        UserPermRequestDTO mockUserPermRequest = mock(UserPermRequestDTO.class);
        when(mockUserService.editUserPerms(id, mockUserPermRequest)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, ()-> userController.editUserPermissions(id, mockUserPermRequest));
    }

    @Test
    void testChangeUserPasswordSuccess() throws Exception {
        UserChangePasswordRequestDTO mockUserChangePasswordRequest = mock(UserChangePasswordRequestDTO.class);
        UserResponseDTO mockUserResponse = mock(UserResponseDTO.class);
        when(mockUserService.changePassword(any(UserChangePasswordRequestDTO.class))).thenReturn(mockUserResponse);
        assertEquals(ResponseEntity.ok(mockUserResponse), userController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testChangeUserPasswordUserDisabled() {
        UserChangePasswordRequestDTO mockUserChangePasswordRequest = mock(UserChangePasswordRequestDTO.class);
        when(mockUserService.changePassword(any(UserChangePasswordRequestDTO.class))).thenThrow(DisabledException.class);
        assertThrows(Exception.class, ()-> userController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testChangeUserPasswordBadCredentials() {
        UserChangePasswordRequestDTO mockUserChangePasswordRequest = mock(UserChangePasswordRequestDTO.class);
        when(mockUserService.changePassword(any(UserChangePasswordRequestDTO.class))).thenThrow(BadCredentialsException.class);
        assertThrows(Exception.class, ()-> userController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testChangeUserPasswordUsernameNotFound() throws Exception {
        UserChangePasswordRequestDTO mockUserChangePasswordRequest = mock(UserChangePasswordRequestDTO.class);
        when(mockUserService.changePassword(any(UserChangePasswordRequestDTO.class))).thenThrow(UsernameNotFoundException.class);
        assertEquals(ResponseEntity.notFound().build(), userController.changeUserPassword(mockUserChangePasswordRequest));
    }

    @Test
    void testResetUserPasswordSuccess() {
        long id = 1;
        UserResetPasswordResponseDTO mockUserResetPasswordResponse = mock(UserResetPasswordResponseDTO.class);
        when(mockUserService.resetPassword(anyLong())).thenReturn(mockUserResetPasswordResponse);
        assertEquals(ResponseEntity.ok(mockUserResetPasswordResponse), userController.resetUserPassword(id));
    }

    @Test
    void testResetUserPasswordUserNotFound() {
        long id = 1;
        when(mockUserService.resetPassword(anyLong())).thenThrow(EntityNotFoundException.class);
        assertEquals(ResponseEntity.notFound().build(), userController.resetUserPassword(id));
    }
}
