package se.rijksoverheid.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.security.business.UserService;

/**
 * Hold the endpoints related to account management
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountManagementController {

    private UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAccounts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(value = "order-by", required = false, defaultValue = "username") String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));
        System.out.println(userService.getAccounts(pageable));
        return ResponseEntity.ok(userService.getAccounts(pageable));
    }
}
