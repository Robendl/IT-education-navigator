package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.security.business.UserService;

/**
 * Hold the endpoints related to user management
 */
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

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
            @RequestParam(value = "order-by", required = false, defaultValue = "username") String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));
        return ResponseEntity.ok(userService.getUsers(search, pageable));
    }
}
