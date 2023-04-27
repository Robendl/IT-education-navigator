package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.security.business.UserService;
import se.rijksoverheid.security.dto.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;


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
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction
    ){
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
            @RequestBody @Valid UserPermRequestDTO userPermDTO
    ){
        try {
            return ResponseEntity.ok(userService.changeUserPerms(id, userPermDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Change a user's password
     * @param id                        ID of user to be changed.
     * @param userChangePasswordDTO     The info needed to change.
     * @return                          The user that was changed
     * @throws Exception
     */
    @PutMapping("/password/{id}")
    public ResponseEntity<?> changeUserPassword(
            @PathVariable long id,
            @RequestBody @Valid UserChangePasswordRequestDTO userChangePasswordDTO
    ) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userChangePasswordDTO.getUsername(), userChangePasswordDTO.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername(userChangePasswordDTO.getUsername());
        userRequestDTO.setPassword(userChangePasswordDTO.getNewPassword());
        try {
            return ResponseEntity.ok(userService.changePassword(id, userRequestDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
