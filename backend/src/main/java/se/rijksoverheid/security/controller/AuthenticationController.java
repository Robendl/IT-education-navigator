package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.JwtTokenDTO;
import se.rijksoverheid.security.config.JwtTokenUtil;
import se.rijksoverheid.security.dto.UserDTO;
import se.rijksoverheid.security.model.User;

/**
 * Holds the endpoints related to authentication
 */
@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;

    /**
     * Register a new user
     * @param userDTO   Data Transfer Object conatining user info.
     * @return          Created user.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserDTO userDTO) {

        if(userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        return ResponseEntity.ok(userService.save(userDTO));
    }

    /**
     * Login endpoint
     * @param userDTO   Data Transfer Object containing login info
     * @return          Data Transfer Object conating JwtToken and role of user
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Validated UserDTO userDTO) throws Exception {
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
        return ResponseEntity.ok(new JwtTokenDTO(token, user.getRole()));
    }
}
