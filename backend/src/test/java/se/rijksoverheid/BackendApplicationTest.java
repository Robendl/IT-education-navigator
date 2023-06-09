package se.rijksoverheid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BackendApplicationTest {
    @InjectMocks
    BackendApplication backendApplication;
    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = backendApplication.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
}
