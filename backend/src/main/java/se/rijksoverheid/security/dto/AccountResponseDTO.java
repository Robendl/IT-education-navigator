package se.rijksoverheid.security.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {
    private long id;
    private String username;
    private String role;
}
