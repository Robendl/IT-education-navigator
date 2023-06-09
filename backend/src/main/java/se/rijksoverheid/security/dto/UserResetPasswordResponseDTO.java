package se.rijksoverheid.security.dto;

import lombok.Data;

/**
 * Data Transfer Object used for resetting a user's password
 */
@Data
public class UserResetPasswordResponseDTO {
    private String password;
}
