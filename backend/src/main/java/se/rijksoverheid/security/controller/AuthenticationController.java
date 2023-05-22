package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.config.JwtTokenUtil;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserRequestDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;
import se.rijksoverheid.security.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Holds the endpoints related to authentication
 */
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;

    /**
     * Register a new user
     * @param userDTO   Data Transfer Object containing user info.
     * @return          Created HTTP Status, or Bad Request HTTP Status if username is already in use.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserRequestDTO userDTO) {
        if(userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Emailadres wordt al gebruikt");
        }
        if(!userService.isValidEmailAddress(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Geen geldig emailadres ingevoerd");
        }
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Login endpoint
     * @param userDTO   Data Transfer Object containing login info
     * @return          Data Transfer Object containing JwtToken and role of user
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Validated UserRequestDTO userDTO,
                                                       HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        User user = userService.loadUserByUsername(userDTO.getUsername());
        String token = jwtTokenUtil.generateToken(user);
        response.addHeader("Set-Cookie", "jwt=" + token + "; Path=/; Secure; HttpOnly; SameSite=strict");

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setRole(user.getRole());
        return ResponseEntity.ok(userResponse);
    }
}
