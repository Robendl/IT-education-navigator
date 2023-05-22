package se.rijksoverheid.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUsers() {
        String search = "";
        int page = 0,size = 500;
        Sort.Direction direction = Sort.Direction.ASC;

        UserResponseDTO mockUserResponse = mock(UserResponseDTO.class);
        List<UserResponseDTO> users = new ArrayList<>();
        users.add(mockUserResponse);

        when(mockUserService.getUsers(anyString(),any(Pageable.class))).thenReturn(users);

        assertEquals(users, userController.getUsers(search, page, size, direction).getBody());
    }

    @Test
    void testEditUserPermissions() {
        long id = 1;
        UserPermRequestDTO mockUserPermRequest = mock(UserPermRequestDTO.class);
        UserResponseDTO mockUserResponse = mock(UserResponseDTO.class);
        when(mockUserService.editUserPerms(anyLong(),any(UserPermRequestDTO.class))).thenReturn(mockUserResponse);
        assertDoesNotThrow(() -> {
            userController.editUserPermissions(id,mockUserPermRequest);
        });
    }
}
