package se.rijksoverheid.security.config;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class JwtTokenUtilTest {

    @Mock
    private JwtParser mockJwtParser;
    @Mock
    private JwtBuilder mockJwtBuilder;
    private JwtTokenUtil jwtTokenUtil;
    private JwtTokenUtil spyJwtTokenUtil;
    @Mock
    Jws<Claims> mockJws;
    @Mock
    Claims mockClaims;
    @Mock
    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtil = new JwtTokenUtil(mockJwtParser, mockJwtBuilder);
        spyJwtTokenUtil = spy(new JwtTokenUtil(mockJwtParser, mockJwtBuilder));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = "token";
        String username = "username";
        when(mockJws.getBody()).thenReturn(mockClaims);
        when(mockClaims.getSubject()).thenReturn(username);
        when(mockJwtParser.parseClaimsJws(token)).thenReturn(mockJws);
        assertEquals(username, jwtTokenUtil.getUsernameFromToken(token));
    }

    @Test
    void testGetUsernameFromToken_InvalidToken() {
        String invalidToken = "invalidToken";
        when(mockJwtParser.parseClaimsJws(invalidToken)).thenThrow(JwtException.class);
        assertThrows(JwtException.class, () -> jwtTokenUtil.getUsernameFromToken(invalidToken));
    }

    @Test
    void testGenerateToken() {
        String username = "username";
        String token = "token";
        when(userDetails.getUsername()).thenReturn(username);
        when(mockJwtBuilder.setClaims(any(Map.class))).thenReturn(mockJwtBuilder);
        when(mockJwtBuilder.setSubject(username)).thenReturn(mockJwtBuilder);
        when(mockJwtBuilder.setIssuedAt(Mockito.any())).thenReturn(mockJwtBuilder);
        when(mockJwtBuilder.setExpiration(Mockito.any())).thenReturn(mockJwtBuilder);
        when(mockJwtBuilder.compact()).thenReturn(token);
        assertEquals(token, jwtTokenUtil.generateToken(userDetails));
    }
}