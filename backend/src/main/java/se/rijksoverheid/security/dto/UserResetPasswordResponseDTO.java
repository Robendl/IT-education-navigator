package se.rijksoverheid.security.dto;

import lombok.Data;
import se.rijksoverheid.security.model.User;

@Data
public class UserResetPasswordResponseDTO {
    private long id;
    private String username;
    private String password;
    private User.Role role;
}
