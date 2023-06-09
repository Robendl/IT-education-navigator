package se.rijksoverheid.security.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserRequestDTOTest {

    @Test
    void testSetUsername() {
        String usernameBefore = "UserName";
        String usernameAfter = "username";
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername(usernameBefore);
        assertEquals(usernameAfter, userRequestDTO.getUsername());
    }
}
