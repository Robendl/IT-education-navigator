package se.rijksoverheid.security.dto;

import lombok.Data;
import se.rijksoverheid.security.model.User;

@Data
public class UserResetPasswordResponseDTO {
    private String password;
}
