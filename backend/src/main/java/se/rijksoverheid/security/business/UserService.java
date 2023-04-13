package se.rijksoverheid.security.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.security.dto.AccountResponseDTO;
import se.rijksoverheid.security.dto.UserDTO;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * UserService classed is used for interacting with userdata.
 * Is an implementation of UserDetailsService so that Spring security's AuthenticationManager can use it.
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * Finds a user by username.
     * @param username the username identifying the user whose data is required.
     * @return          the user.
     * @throws UsernameNotFoundException    when no user with the given username can be found.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("There exists no user with username: " + username));
        return user;
    }

    /**
     * Saves a user to the database, used for registering.
     * @param userDTO   Data Transfer Object containing user info.
     * @return          Created user.
     */
    public UserDTO save(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        return Mapper.map(userRepository.save(user), UserDTO.class);
    }

    /**
     * Function used for checking if a user already exists.
     * @param username  username to be checked.
     * @return          true if username is taken, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public List<AccountResponseDTO> getAccounts(Pageable pageable){
        Page<User> userPage = userRepository.getAllUsers(pageable);
        List<AccountResponseDTO> accounts = new ArrayList<>();
        for(User user: userPage.getContent()) {
            AccountResponseDTO accountDTO = new AccountResponseDTO();
            accountDTO.setId(user.getId());
            accountDTO.setUsername(user.getUsername());
            accountDTO.setRole(user.getRole());
            accounts.add(accountDTO);
        }
        return accounts;
    }
}