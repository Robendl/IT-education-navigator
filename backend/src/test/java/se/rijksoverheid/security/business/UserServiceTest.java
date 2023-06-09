package se.rijksoverheid.security.business;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.security.dto.UserChangePasswordRequestDTO;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserRequestDTO;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository mockUserRepository;
    @Mock
    PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void testLoadUserByUsername() {
        String username = "username";
        User mockUser = mock(User.class);
        when(mockUserRepository.findUserByUsername(username)).thenReturn(Optional.of(mockUser));
        assertEquals(mockUser,userService.loadUserByUsername(username));
    }

    @Test
    void testSave() {
        User mockUser = mock(User.class);
        String username = "test@email.com";
        String passwordPlaintext = "plaintextPassword";
        String passwordEncrypted = "encryptedPassword";
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(mockUserRequest.getPassword()).thenReturn(passwordPlaintext);
        when(mockUserRequest.getUsername()).thenReturn(username);
        when(mockPasswordEncoder.encode(passwordPlaintext)).thenReturn(passwordEncrypted);
        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);

        userService.save(mockUserRequest);

        verify(mockUserRepository).save(userArgumentCaptor.capture());
        assertEquals(User.Role.LIM_DATA_CONSUMER, userArgumentCaptor.getValue().getRole());

        assertEquals(username, userArgumentCaptor.getValue().getUsername());
        assertEquals(passwordEncrypted, userArgumentCaptor.getValue().getPassword());
    }

    @Test
    void testExistByUsername() {
        String username = "username";
        when(mockUserRepository.existsByUsername(username)).thenReturn(false);
        assertFalse(userService.existsByUsername(username));
    }

    @Test
    void testIsValidEmailAddress() {
        String email = "name@domain.com";
        assertDoesNotThrow(() -> userService.checkEmailAddress(email));
    }

    @Test
    void testIsValidEmailAddress_False() {
        String email = "name";
        assertThrows(BadRequestException.class, () -> userService.checkEmailAddress(email));
    }

    @Test
    void testGetUsers_EmptySearch() {
        String search = "";
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        userList.add(mockUser2);
        Sort mockSort = mock(Sort.class);

       when(mockUserRepository.findAll(mockSort)).thenReturn(userList);

        assertEquals(2,userService.getUsers(search,mockSort).size());
        verify(mockUserRepository).findAll(mockSort);
    }

    @Test
    void testGetUsers_FilledSearch() {
        String search = "user";
        User mockUser1 = mock(User.class);
        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        Sort mockSort = mock(Sort.class);

        when(mockUserRepository.findAllUserByUsername(search, mockSort)).thenReturn(userList);
        assertEquals(1,userService.getUsers(search,mockSort).size());
        verify(mockUserRepository).findAllUserByUsername(search, mockSort);
    }

    @Test
    void testEditUserPerms() {
        String username = "admin@email.com";
        String password = "password";
        UserPermRequestDTO mockUserRequest = mock(UserPermRequestDTO.class);
        when(mockUserRequest.getRole()).thenReturn(User.Role.ADMIN);

        User user = new User();
        long userId = user.getId();
        user.setRole(User.Role.DATA_MANAGER);
        user.setUsername(username);
        user.setPassword(password);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));

        assertEquals(User.Role.ADMIN, userService.editUserPerms(userId, mockUserRequest).getRole());
    }

    @Test
    void testEditUserPermsUserNotFound() {
        long userId = 1;
        UserPermRequestDTO mockUserRequest = mock(UserPermRequestDTO.class);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.editUserPerms(userId,mockUserRequest));
    }

    @Test
    void testChangePassword() {
        String username = "test@email.com";
        String passwordOldEncrypted = "encryptedOldPassword";
        String passwordNewEncrypted = "encryptedNewPassword";
        String passwordNewPlainText = "plaintextNewPassword";
        UserChangePasswordRequestDTO mockUserRequest = mock(UserChangePasswordRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(username);
        when(mockUserRequest.getNewPassword()).thenReturn(passwordNewPlainText);

        User user = new User();
        user.setRole(User.Role.DATA_MANAGER);
        user.setUsername(username);
        user.setPassword(passwordOldEncrypted);

        when(mockUserRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(mockPasswordEncoder.encode(passwordNewPlainText)).thenReturn(passwordNewEncrypted);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        userService.changePassword(mockUserRequest);

        verify(mockUserRepository).save(userArgumentCaptor.capture());
        assertEquals(User.Role.DATA_MANAGER, userArgumentCaptor.getValue().getRole());
        assertEquals(username, userArgumentCaptor.getValue().getUsername());
        assertEquals(passwordNewEncrypted, userArgumentCaptor.getValue().getPassword());
    }

    @Test
    void testChangePassword_userNotFound() {
        String username = "test@email.com";
        UserChangePasswordRequestDTO mockUserRequest = mock(UserChangePasswordRequestDTO.class);
        when(mockUserRequest.getUsername()).thenReturn(username);
        when(mockUserRepository.findUserByUsername(username)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()-> userService.changePassword(mockUserRequest));
    }

    @Test
    void testResetPassword() {
        String REGEX = "^[A-Z0-9]*$";
        String username = "admin@email.com";
        String passwordOld = "passwordOld";
        String passwordNew = "passwordNew";
        int size = 12;

        User user = new User();
        long userId = user.getId();
        user.setRole(User.Role.DATA_MANAGER);
        user.setUsername(username);
        user.setPassword(passwordOld);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockPasswordEncoder.encode(anyString())).thenReturn(passwordNew);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> randomPasswordArgumentCaptor = ArgumentCaptor.forClass(String.class);

        userService.resetPassword(userId);

        verify(mockPasswordEncoder).encode(randomPasswordArgumentCaptor.capture());
        String randomString1 = randomPasswordArgumentCaptor.getValue();
        assertTrue(Pattern.compile(REGEX).matcher(randomString1).matches());
        assertEquals(randomString1.length(), size);

        verify(mockUserRepository).save(userArgumentCaptor.capture());
        User userChanged = userArgumentCaptor.getValue();
        assertEquals(User.Role.DATA_MANAGER, userChanged.getRole());
        assertEquals(username, userChanged.getUsername());
        assertEquals(passwordNew, userChanged.getPassword());
    }

    @Test
    void testResetPassword_userNotFound() {
        long userId = 1;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()-> userService.resetPassword(userId));
    }

    @Test
    void testDeleteById() {
        long userId = 1;
        userService.deleteById(userId);
        verify(mockUserRepository,times(1)).deleteById(userId);
    }
}
