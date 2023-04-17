package se.rijksoverheid.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO for changing user permissions/role
 */
@Data
public class UserPermRequestDTO {
    @NotNull
    @NotBlank
    private String role;
}
