package se.rijksoverheid.security.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JwtAuthenticationEntryPointTest {
    @Test
    void testCommence() throws IOException {
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationException mockAuthException = mock(AuthenticationException.class);
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
        jwtAuthenticationEntryPoint.commence(mockRequest, mockResponse, mockAuthException);
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}