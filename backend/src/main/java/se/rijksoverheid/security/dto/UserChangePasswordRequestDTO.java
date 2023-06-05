package se.rijksoverheid.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object used for changing a user's password
 */
@Data
public class UserChangePasswordRequestDTO {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    private String newPassword;
    @NotNull
    @NotBlank
    private String confirmNewPassword;
}

