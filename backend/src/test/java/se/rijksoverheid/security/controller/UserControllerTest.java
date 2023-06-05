package se.rijksoverheid.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.UserChangePasswordRequestDTO;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserResetPasswordResponseDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;
import se.rijksoverheid.security.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService mockUserService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private UserPermRequestDTO mockUserPermRequest;
    @Mock
    private UserResponseDTO mockUserResponse;
    @Mock
    private UserResetPasswordResponseDTO mockUserResetPasswordResponse;
    @InjectMocks
    private UserController userController;

    @Test
    void testGetUsers() {
        String search = "";
        int page = 0,size = 500;
        Sort.Direction direction = Sort.Direction.ASC;

        List<UserResponseDTO> users = new ArrayList<>();
        users.add(mockUserResponse);

        when(mockUserService.getUsers(anyString(), any(Pageable.class))).thenReturn(users);

        assertEquals(users, userController.getUsers(search, page, size, direction).getBody());
    }

    @Test
    void testEditUserPermissions() {
        long id = 1;
        when(mockUserService.editUserPerms(id,mockUserPermRequest)).thenReturn(mockUserResponse);
        assertDoesNotThrow(() -> {
            userController.editUserPermissions(id, mockUserPermRequest);
        });
    }

    @Test
    void testEditNonExistingUserPermissions() {
        long id = 1;
        when(mockUserService.editUserPerms(id, mockUserPermRequest)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, ()-> userController.editUserPermissions(id, mockUserPermRequest));
    }

    @Test
    void testResetUserPasswordSuccess() {
        long idAdmin = 1;
        long idUser = 2;
        String username = "test@email.com";
        User mockUser = mock(User.class);

        when(authentication.getName()).thenReturn(username);
        when(mockUser.getId()).thenReturn(idAdmin);
        when(mockUserService.loadUserByUsername(username)).thenReturn(mockUser);
        when(mockUserService.resetPassword(idUser)).thenReturn(mockUserResetPasswordResponse);

        assertEquals(ResponseEntity.ok(mockUserResetPasswordResponse), userController.resetUserPassword(authentication, idUser));
    }

    @Test
    void testResetUserPasswordUserNotFound() {
        long idAdmin = 1;
        long idUser = 2;
        String username = "test@email.com";
        User mockUser = mock(User.class);

        when(authentication.getName()).thenReturn(username);
        when(mockUser.getId()).thenReturn(idAdmin);
        when(mockUserService.loadUserByUsername(username)).thenReturn(mockUser);
        when(mockUserService.resetPassword(idUser)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, ()-> userController.resetUserPassword(authentication, idUser));
    }

    @Test
    void testResetUserPasswordNotResetOwnPassword() {
        long idAdmin = 1;
        long idUser = 1;
        String username = "test@email.com";
        User mockUser = mock(User.class);

        when(authentication.getName()).thenReturn(username);
        when(mockUser.getId()).thenReturn(idAdmin);
        when(mockUserService.loadUserByUsername(username)).thenReturn(mockUser);

        assertThrows(BadRequestException.class, ()-> userController.resetUserPassword(authentication, idUser));
    }
}
