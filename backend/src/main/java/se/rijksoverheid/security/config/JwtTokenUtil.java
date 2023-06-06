package se.rijksoverheid.security.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used for generating and checking JwtTokens.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    public static final long JWT_TOKEN_VALIDITY = (long) 3 * 60 * 60;
    private final JwtParser jwtParser;
    private final JwtBuilder jwtBuilder;

    /**
     * Retrieves username from token.
     * @param token token
     * @return      username
     */
    public String getUsernameFromToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Checks if token is expired.
     * @param token     token
     * @return          true if token is expired, false if not.
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = jwtParser.parseClaimsJws(token).getBody().getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * Generates a new JwtToken
     * @param userDetails   user info to base JwtToken on.
     * @return              token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return jwtBuilder.setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).compact();
    }
}
