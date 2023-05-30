package se.rijksoverheid.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.rijksoverheid.security.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Extension of WebSecurityConfigurerAdapter from Spring Security, used for configuring the security settings.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configures implementation of UserDetailsService (UserService) and what password encoder to use.
     * @param auth          authentication manager builder.
     * @throws Exception    if an error occurs when adding the UserService to the configuration.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * Sets up the Authentication Manager Bean to be used by Spring Security.
     * @return AuthenticationManagerBean
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configures the security settings. Sets which endpoints need which roles and specifies classes to be used in the
     * security process.
     * @param httpSecurity the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        final String ADMIN = String.valueOf(User.Role.ADMIN);
        final String DATA_MANAGER = String.valueOf(User.Role.DATA_MANAGER);
        httpSecurity.cors().and().csrf().disable()
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

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
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
