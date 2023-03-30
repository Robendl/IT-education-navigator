package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.UserDTO;
import se.rijksoverheid.security.model.User;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody @Validated UserDTO userDTO) {
        if(userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        return ResponseEntity.ok(userService.save(userDTO));
    }

}
