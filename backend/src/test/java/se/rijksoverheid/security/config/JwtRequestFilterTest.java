package se.rijksoverheid.security.config;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import se.rijksoverheid.exceptions.webexceptions.JwtAuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {
    @Mock
    UserDetailsService mockUserDetailsService;
    @Mock
    JwtTokenUtil mockJwtTokenUtil;
    @Mock
    Environment env;
    @InjectMocks
    JwtRequestFilter jwtRequestFilter;
    static String baseUrl = "baseUrl";
    @Mock
    HttpServletRequest mockRequest;
    @Mock
    HttpServletResponse mockResponse;
    @Mock
    FilterChain mockFilterChain;
    @Mock
    UserDetails mockUserDetails;
    @Mock
    SimpleGrantedAuthority mockSimpleGrantedAuthority;

    @Test
    void testDoFilterInternal_SuccessfulAuthentication() throws ServletException, IOException {
        when(env.getProperty("server.servlet.context-path")).thenReturn(baseUrl);
        when(mockRequest.getRequestURI()).thenReturn("not/auth");
        Cookie mockCookie = mock(Cookie.class);
        String token = "token";
        String username = "username";
        when(mockCookie.getName()).thenReturn("jwt");
        when(mockCookie.getValue()).thenReturn(token);
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{mockCookie});
        when(mockJwtTokenUtil.getUsernameFromToken(token)).thenReturn(username);
        when(mockUserDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMock = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext mockSecurityContext = mock(SecurityContext.class);
            when(mockSecurityContext.getAuthentication()).thenReturn(null);
            securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);
            ArgumentCaptor<UsernamePasswordAuthenticationToken> authToken = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

            jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
            verify(mockSecurityContext).setAuthentication(authToken.capture());
            assertEquals(mockUserDetails, authToken.getValue().getPrincipal());
            verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        }
    }

    @Test
    void testDoFilterInternal_OnAuthEndpoint() throws ServletException, IOException {
        when(env.getProperty("server.servlet.context-path")).thenReturn(baseUrl);
        when(mockRequest.getRequestURI()).thenReturn(baseUrl + "/auth");
        assertDoesNotThrow(() -> jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain));
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    void testDoFilterInternal_NoJwtCookie() throws ServletException, IOException {
        when(env.getProperty("server.servlet.context-path")).thenReturn(baseUrl);
        when(mockRequest.getRequestURI()).thenReturn("not/auth");
        when(mockRequest.getCookies()).thenReturn(null);
        when(mockJwtTokenUtil.getUsernameFromToken(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(JwtAuthenticationException.class,() -> jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain));
    }

    @Test
    void testDoFilterInternal_InvalidJwt() {
        String token = "token";
        when(env.getProperty("server.servlet.context-path")).thenReturn(baseUrl);
        when(mockRequest.getRequestURI()).thenReturn("not/auth");
        Cookie mockCookie = mock(Cookie.class);
        when(mockCookie.getName()).thenReturn("jwt");
        when(mockCookie.getValue()).thenReturn(token);
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{mockCookie});
        when(mockJwtTokenUtil.getUsernameFromToken(token)).thenThrow(JwtException.class);
        assertThrows(JwtAuthenticationException.class,() -> jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain));
    }
}