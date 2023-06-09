package se.rijksoverheid.security.dto;

import lombok.Data;
import se.rijksoverheid.security.model.User;

/**
 * Data Transfer Object used for registering and logging in.
 */
@Data
public class UserResponseDTO {
    private long id;
    private String username;
    private User.Role role;
}
