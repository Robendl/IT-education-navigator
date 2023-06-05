package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.exceptions.webexceptions.EntityConflictException;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.config.JwtTokenUtil;
import se.rijksoverheid.security.dto.UserChangePasswordRequestDTO;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserRequestDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;
import se.rijksoverheid.security.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    public ResponseEntity<String> registerUser(@RequestBody @Validated UserRequestDTO userDTO) {
        if(userService.existsByUsername(userDTO.getUsername())) {
            throw new EntityConflictException("Email-address is already in use");
        }
        userService.checkEmailAddress(userDTO.getUsername());
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Login endpoint
     * @param userDTO   Data Transfer Object containing login info
     * @return          Data Transfer Object containing JwtToken and role of user
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> createAuthenticationToken(@RequestBody @Validated UserRequestDTO userDTO,
                                                       HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        User user = userService.loadUserByUsername(userDTO.getUsername());
        String token = jwtTokenUtil.generateToken(user);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/rijksoverheid/api");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
//        response.addHeader("Set-Cookie", "jwt=" + token + "; Path=/; Secure; HttpOnly; SameSite=strict");
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setRole(user.getRole());
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Change a user's password
     * @param userChangePasswordDTO     The info needed to change.
     * @return                          The user that was changed
     */
    @PutMapping("/password")
    public ResponseEntity<UserResponseDTO> changeUserPassword(
            @RequestBody @Valid UserChangePasswordRequestDTO userChangePasswordDTO
    ) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        userChangePasswordDTO.getUsername(),
                        userChangePasswordDTO.getPassword()
                )
        );
        return ResponseEntity.ok(userService.changePassword(userChangePasswordDTO));
    }
}
