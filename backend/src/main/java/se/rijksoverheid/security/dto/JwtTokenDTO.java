package se.rijksoverheid.security.dto;

import lombok.Data;

@Data
public class JwtTokenDTO {
    private final String token;
}