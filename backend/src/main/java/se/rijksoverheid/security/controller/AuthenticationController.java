package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.security.business.UserDetailsServiceImpl;
import se.rijksoverheid.security.dto.JwtTokenDTO;
import se.rijksoverheid.security.config.JwtTokenUtil;
import se.rijksoverheid.security.dto.UserDTO;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserDTO userDTO) {
        System.out.println("hello\n");
        if(userDetailsService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        return ResponseEntity.ok(userDetailsService.save(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Validated UserDTO userDTO) throws Exception {
        authenticate(userDTO.getUsername(), userDTO.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtTokenDTO(token));
    }
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
