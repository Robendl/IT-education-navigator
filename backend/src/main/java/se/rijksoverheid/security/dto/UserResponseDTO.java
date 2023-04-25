package se.rijksoverheid.security.dto;

import lombok.Data;
import se.rijksoverheid.security.model.User;

@Data
public class UserResponseDTO {
    private long id;
    private String username;
    private User.Role role;
}
