package se.rijksoverheid.security.business;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserRequestDTO;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Pageable mockPageable = mock(Pageable.class);
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        userList.add(mockUser2);
        Page<User> users = new PageImpl<>(userList);

        when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(users);

        assertEquals(2,userService.getUsers(search,mockPageable).size());
    }

    @Test
    void testGetUsers_FilledSearch() {
        String search = "user";
        Pageable mockPageable = mock(Pageable.class);
        User mockUser1 = mock(User.class);
        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        Page<User> users = new PageImpl<>(userList);

        when(mockUserRepository.findAllUserByUsername(search, mockPageable)).thenReturn(users);

        assertEquals(1,userService.getUsers(search,mockPageable).size());
    }

    @Test
    void testEditUserPerms() {
        long userId = 1;
        User mockUser = mock(User.class);
        UserPermRequestDTO mockUserRequest = mock(UserPermRequestDTO.class);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        assertDoesNotThrow(() -> {
            userService.editUserPerms(userId,mockUserRequest);
        });
    }

    @Test
    void testEditUserPermsUserNotFound() {
        long userId = 1;
        UserPermRequestDTO mockUserRequest = mock(UserPermRequestDTO.class);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.editUserPerms(userId,mockUserRequest));
    }
}
