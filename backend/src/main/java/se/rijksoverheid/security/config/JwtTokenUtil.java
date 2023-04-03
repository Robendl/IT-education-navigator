package se.rijksoverheid.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used for generating and checking JwtTokens.
 */
@Component
public class JwtTokenUtil {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private static final SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Retrieves username from token.
     * @param token token
     * @return      username
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Checks if token is expired.
     * @param token     token
     * @return          true if token is expired, false if not.
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Generates a new JwtToken
     * @param userDetails   user info to base JwtToken on.
     * @return              token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(secret, SignatureAlgorithm.HS512).compact();
    }

    /**
     * Validates token
     * @param token         token
     * @param userDetails   user to be checked for.
     * @return              true if token is valid, false if not.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
