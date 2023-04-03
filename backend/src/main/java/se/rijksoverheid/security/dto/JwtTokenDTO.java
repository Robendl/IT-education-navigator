package se.rijksoverheid.security.dto;

import lombok.Data;

/**
 * Data Transfer Object user for returning token and user role on succesfull login.
 */
@Data
public class JwtTokenDTO {
    private final String token;
    private final String role;
}