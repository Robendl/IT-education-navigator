package se.rijksoverheid.security.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WebSecurityConfigTest {
    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    void setUp() {
        webSecurityConfig = new WebSecurityConfig();
    }

    @Test
    void testAuthenticationProvider() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AuthenticationProvider authenticationProvider = webSecurityConfig.authenticationProvider(userDetailsService, passwordEncoder);
        assertNotNull(authenticationProvider);
        assertEquals(DaoAuthenticationProvider.class, authenticationProvider.getClass());
    }

    @Test
    void testAuthenticationManagerBean() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AuthenticationManager authenticationManager = webSecurityConfig.authenticationManagerBean(userDetailsService, passwordEncoder);
        assertNotNull(authenticationManager);
        assertEquals(ProviderManager.class, authenticationManager.getClass());
    }

    @Test
    void testCookieCsrfTokenRepository() {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = webSecurityConfig.cookieCsrfTokenRepository();
        assertEquals("/" ,cookieCsrfTokenRepository.getCookiePath());
    }

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource corsConfigurationSource = webSecurityConfig.corsConfigurationSource();
        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource) corsConfigurationSource;
        CorsConfiguration configuration = source.getCorsConfigurations().get("/**");
        assertEquals(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"), configuration.getAllowedOrigins());
        assertEquals(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"), configuration.getAllowedMethods());
        assertEquals(Arrays.asList("Access-Control-Allow-Credentials", "authorization", "content-type", "x-auth-token", "x-xsrf-token"),
                configuration.getAllowedHeaders());
        assertEquals(List.of("x-auth-token"), configuration.getExposedHeaders());
        assertEquals(true, configuration.getAllowCredentials());
    }

    @Test
    void testJwtParser() {
        JwtParser jwtParser = webSecurityConfig.jwtParser();
        assertNotNull(jwtParser);
    }

    @Test
    void testJwtBuilder() {
        JwtBuilder jwtBuilder = webSecurityConfig.jwtBuilder();
        assertNotNull(jwtBuilder);
    }
}