package se.rijksoverheid.security.dto;

import lombok.Data;
import se.rijksoverheid.security.model.User;

import javax.validation.constraints.NotNull;

/**
 * DTO for changing user permissions/role
 */
@Data
public class UserPermRequestDTO {
    @NotNull
    private User.Role role;
}
