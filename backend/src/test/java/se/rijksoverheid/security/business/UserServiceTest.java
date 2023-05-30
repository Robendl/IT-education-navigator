package se.rijksoverheid.security.business;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.rijksoverheid.mapper.Mapper;
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
        UserRequestDTO mockUserRequest = mock(UserRequestDTO.class);
        when(mockUserRequest.getPassword()).thenReturn("encryptedPassword");
        when(mockPasswordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            mockMapper.when(() -> Mapper.map(mockUser, UserRequestDTO.class)).thenReturn(mockUserRequest);
            assertEquals(mockUserRequest, userService.save(mockUserRequest));
        }
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
        assertTrue(userService.isValidEmailAddress(email));
    }

    @Test
    void testIsValidEmailAddress_False() {
        String email = "name";
        assertFalse(userService.isValidEmailAddress(email));
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
}
