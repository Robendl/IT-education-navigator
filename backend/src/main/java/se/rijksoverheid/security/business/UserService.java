package se.rijksoverheid.security.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.security.dto.*;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * UserService classed is used for interacting with userdata.
 * Is an implementation of UserDetailsService so that Spring security's AuthenticationManager can use it.
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int NEW_RANDOM_PASSWORD_LENGTH = 12;

    /**
     * Finds a user by username.
     * @param username  the username identifying the user whose data is required.
     * @return          the user.
     * @throws UsernameNotFoundException    when no user with the given username can be found.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("There exists no user with username: " + username));
    }

    /**
     * Saves a user to the database, used for registering.
     * @param userDTO   Data Transfer Object containing user info.
     * @return          Created user.
     */
    public UserRequestDTO save(UserRequestDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(User.Role.DATA_CONSUMER);
        return Mapper.map(userRepository.save(user), UserRequestDTO.class);
    }

    /**
     * Function used for checking if a user already exists.
     * @param username  username to be checked.
     * @return          true if username is taken, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a given string is a valid email address
     * @param email string to be checked
     * @return      true if string is a valid email address, false otherwise
     */
    public boolean isValidEmailAddress(String email) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    /**
     * Function used for getting a list of all users and converting them to a DTO.
     * @param pageable  page for frontend
     * @return          UserResponseDTO.
     */
    @Transactional
    public List<UserResponseDTO> getUsers(String search,Pageable pageable){
        Page<User> users;
        if(search.isEmpty()){
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findAllUserByUsername(search, pageable);
        }

        List<UserResponseDTO> UserResponseDTO = new ArrayList<>();
        for(User user: users.getContent()) {
            UserResponseDTO UserResDTO = new UserResponseDTO();
            UserResDTO.setId(user.getId());
            UserResDTO.setUsername(user.getUsername());
            UserResDTO.setRole(user.getRole());
            UserResponseDTO.add(UserResDTO);
        }
        return UserResponseDTO;
    }

    /**
     * Change a user's permissions.
     * @param userId                    Id of user to change permissions for.
     * @param userPermDTO               DTO for all data to be changed.
     * @return                          The user which was changed.
     * @throws EntityNotFoundException  No user with id was found.
     * @throws Exception                Changed user to non-existing role.
     */
    @Transactional
    public UserResponseDTO editUserPerms(long userId, UserPermRequestDTO userPermDTO) throws EntityNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Mapper.map(userPermDTO, user);
        userRepository.save(user);
        UserResponseDTO UserDTO = new UserResponseDTO();
        Mapper.map(user, UserDTO);
        return UserDTO;
    }

    /**
     * Change a user's password.
     * @param id                        ID of user to change password for.
     * @param userDTO                   DTO for all data to be changed.
     * @return                          The user that was changed.
     * @throws EntityNotFoundException  No user with id was found.
     */
    public UserResponseDTO changePassword(long id, UserChangePasswordRequestDTO userDTO) throws EntityNotFoundException {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        userRepository.save(user);
        return Mapper.map(user, UserResponseDTO.class);
    }

    /**
     * Reset a user's password.
     * @param id                        ID of user to change password for.
     * @return                          The user that was changed.
     * @throws EntityNotFoundException  No user with id was found.
     */
    public UserResetPasswordResponseDTO resetPassword(long id) throws EntityNotFoundException {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        String newPassword = alphaNumericString(NEW_RANDOM_PASSWORD_LENGTH);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        UserResetPasswordResponseDTO UserDTO = new UserResetPasswordResponseDTO();
        UserDTO.setPassword(newPassword);
        return UserDTO;
    }

    /**
     * Helper function that generates a random alphanumeric string
     * @param length    Desired length of the string
     * @return          Random alphanumeric string
     */
    private static String alphaNumericString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}