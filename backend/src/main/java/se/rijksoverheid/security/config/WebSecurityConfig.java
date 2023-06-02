package se.rijksoverheid.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.rijksoverheid.security.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Extension of WebSecurityConfigurerAdapter from Spring Security, used for configuring the security settings.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    /**
     * Configures the security settings. Sets which endpoints need which roles and specifies classes to be used in the
     * security process.
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final String ADMIN = String.valueOf(User.Role.ADMIN);
        final String DATA_MANAGER = String.valueOf(User.Role.DATA_MANAGER);
        http.cors().and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeRequests().antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.PUT, "user/password/{id}/reset").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.PUT, "/user/password/{id}/change").authenticated()
                .antMatchers(HttpMethod.GET, "/user").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.PUT, "/user/**").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/courses/**").hasAnyAuthority(ADMIN)
                .antMatchers(HttpMethod.POST, "/courses").hasAnyAuthority(ADMIN, DATA_MANAGER)
                .antMatchers(HttpMethod.PUT, "/courses/**").hasAnyAuthority(ADMIN, DATA_MANAGER)
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/", "http://127.0.0.1:3000/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Credentials", "authorization", "content-type", "x-auth-token", "x-xsrf-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
