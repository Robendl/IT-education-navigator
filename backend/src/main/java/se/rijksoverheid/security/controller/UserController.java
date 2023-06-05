package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.UserChangePasswordRequestDTO;
import se.rijksoverheid.security.dto.UserPermRequestDTO;
import se.rijksoverheid.security.dto.UserResetPasswordResponseDTO;
import se.rijksoverheid.security.dto.UserResponseDTO;

import javax.validation.Valid;
import java.util.List;


/**
 * Hold the endpoints related to user management
 */
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private AuthenticationManager authenticationManager;
    private UserService userService;

    /**
     * Endpoint for retrieving users
     * @param page          page number of page to return, 0 by default.
     * @param size          size of page to return, 50 by default.
     * @return              List of users
     */
    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> getUsers(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction){
        String orderBy = "username";
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));
        return ResponseEntity.ok(userService.getUsers(search, pageable));
    }

    /**
     * Edit a user's permissions
     * @param id            ID of user to be changed.
     * @param userPermDTO   The info needed to change.
     * @return              The user that was changed
     */
    @PutMapping("/perm/{id}")
    public ResponseEntity<UserResponseDTO> editUserPermissions(
            @PathVariable long id,
            @RequestBody @Valid UserPermRequestDTO userPermDTO){
        return ResponseEntity.ok(userService.editUserPerms(id, userPermDTO));
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

    /**
     * Change a user's password
     * @param id            ID of user to be changed.
     * @return              The user that was changed
     */
    @PutMapping("/password/{id}/reset")
    public ResponseEntity<UserResetPasswordResponseDTO> resetUserPassword(
            Authentication authentication,
            @PathVariable long id) {
        if (id == userService.loadUserByUsername(authentication.getName()).getId()) {
            throw new BadRequestException("Het eigen wachtwoord mag niet gereset worden");
        }
        return ResponseEntity.ok(userService.resetPassword(id));
    }
}
