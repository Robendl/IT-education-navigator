package se.rijksoverheid.security.config;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import se.rijksoverheid.mapper.Mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenUtilTest {

    @Mock
    private JwtParser jwtParser;
    @Mock
    private JwtBuilder jwtBuilder;

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    Jws<Claims> jws;
    @Mock
    Claims claims;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtil = new JwtTokenUtil(jwtParser, jwtBuilder);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = "token";
        String username = "username";
        when(jws.getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn(username);
        when(jwtParser.parseClaimsJws(token)).thenReturn(jws);
        assertEquals(username, jwtTokenUtil.getUsernameFromToken(token));
    }

    @Test
    public void testGetUsernameFromToken_InvalidToken() {
        String invalidToken = "invalidToken";
        when(jwtParser.parseClaimsJws(invalidToken)).thenThrow(JwtException.class);
        assertThrows(JwtException.class, () -> jwtTokenUtil.getUsernameFromToken(invalidToken));
    }

    @Test
    void generateToken() {
    }

    @Test
    void validateToken() {
    }
}