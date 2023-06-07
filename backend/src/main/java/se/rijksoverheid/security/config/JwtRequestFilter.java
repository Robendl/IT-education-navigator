package se.rijksoverheid.security.config;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.rijksoverheid.exceptions.webexceptions.JwtAuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This implementation of OncePerRequestFilter specifies a filter that is performed on every request that requires
 * authentication. It filters out request with missing or invalid JwtTokens.
 */
@Component
@Slf4j
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private UserDetailsService userService;
    private JwtTokenUtil jwtTokenUtil;
    private Environment env;

    /**
     * Handles filtering of request based on valid JwtTokens.
     * @param request           incoming request.
     * @param response          response that can be sent.
     * @param chain             filter chain in use.
     * @throws ServletException when something goes wrong within the servlets.
     * @throws IOException      when incorrect input is given.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if(request.getRequestURI().startsWith(env.getProperty("server.servlet.context-path") + "/auth")) {
            chain.doFilter(request, response);
            return;
        }
        String jwtToken = null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt")) {
                    jwtToken = cookie.getValue();
                }
            }
        }
        try {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            chain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            throw new JwtAuthenticationException("Exception occurred while validating JWT: ", e);
        }
    }
}