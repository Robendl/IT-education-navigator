package se.rijksoverheid.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object used for registering and logging in.
 */
@Data
public class UserRequestDTO {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;
}
