package se.rijksoverheid.security.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private long id;
    private String username;
    private String role;
}
