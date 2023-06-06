package se.rijksoverheid.security.config;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void testIsTokenExpired_NotExpiredToken() {
        String token = "validToken";
        when(mockJws.getBody()).thenReturn(mockClaims);
        when(mockClaims.getExpiration()).thenReturn(Date.from(new Date().toInstant().plusSeconds(1000)));
        when(mockJwtParser.parseClaimsJws(token)).thenReturn(mockJws);

        assertFalse(spyJwtTokenUtil.isTokenExpired(token));
    }

    @Test
    void testIsTokenExpired_InvalidToken() {
        String invalidToken = "invalidToken";
        doThrow(JwtException.class).when(mockJwtParser).parseClaimsJws(invalidToken);
        assertTrue(jwtTokenUtil.isTokenExpired(invalidToken));
    }

    @Test
    void testIsTokenExpired_ExpiredToken() {
        String token = "expiredToken";
        when(mockJws.getBody()).thenReturn(mockClaims);
        when(mockClaims.getExpiration()).thenReturn(Date.from(new Date().toInstant().minusSeconds(1000)));
        when(mockJwtParser.parseClaimsJws(token)).thenReturn(mockJws);

        assertTrue(spyJwtTokenUtil.isTokenExpired(token));
    }
}