package se.rijksoverheid.security.business;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.rijksoverheid.exceptions.webexceptions.BadRequestException;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.security.dto.*;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * UserService classed is used for interacting with userdata.
 * Is an implementation of UserDetailsService so that Spring security's AuthenticationManager can use it.
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int NEW_RANDOM_PASSWORD_LENGTH = 12;


    /**
     * Finds a user by username.
     * @param username  the username identifying the user whose data is required.
     * @return          the user.
     * @throws NotFoundException    when no user with the given username can be found.
     */
    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username).
                orElseThrow(() -> new NotFoundException("There exists no user with username: " + username));
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
        user.setRole(User.Role.LIM_DATA_CONSUMER);
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
     * Method used for checking if a user already exists.
     * @param email    email to be checked.
     */
    public void checkEmailAddress(String email) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(!Pattern.compile(regexPattern).matcher(email).matches()) {
            throw new BadRequestException("Email-address is not valid");
        }
    }

    /**
     * Retrieve list of all users
     * @param search    possible search string, if empty all users are returned
     * @param sort      sort order
     * @return          List of all/found users
     */
    @Transactional
    public List<UserResponseDTO> getUsers(String search, Sort sort){
        List<User> users;
        if(search.isEmpty()){
            users = userRepository.findAll(sort);
        } else {
            users = userRepository.findAllUserByUsername(search, sort);
        }

        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();
        for(User user: users) {
            UserResponseDTO userResDTO = new UserResponseDTO();
            userResDTO.setId(user.getId());
            userResDTO.setUsername(user.getUsername());
            userResDTO.setRole(user.getRole());
            userResponseDTOs.add(userResDTO);
        }
        return userResponseDTOs;
    }

    /**
     * Edit permissions of a user.
     * @param userId        ID of user to edit.
     * @param userPermDTO   DTO containing new role.
     * @return              The user that was changed.
     */
    @Transactional
    public UserResponseDTO editUserPerms(long userId, UserPermRequestDTO userPermDTO) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId + " could not be found."));
        user.setRole(userPermDTO.getRole());
        userRepository.save(user);
        UserResponseDTO userDTO = new UserResponseDTO();
        Mapper.map(user, userDTO);
        return userDTO;
    }


    /**
     * Change password of a user.
     * @param userDTO   DTO containing username and new password.
     * @return          The user that was changed.
     */
    public UserResponseDTO changePassword(UserChangePasswordRequestDTO userDTO) {
        User user = loadUserByUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        userRepository.save(user);
        return Mapper.map(user, UserResponseDTO.class);
    }

    /**
     * Reset a user's password.
     * @param id                        ID of user to change password for.
     * @return                          The user that was changed.
     */
    public UserResetPasswordResponseDTO resetPassword(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User with id " + id + " could not be found."));
        String newPassword = alphaNumericString(NEW_RANDOM_PASSWORD_LENGTH);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        UserResetPasswordResponseDTO userDTO = new UserResetPasswordResponseDTO();
        userDTO.setPassword(newPassword);
        return userDTO;
    }

    /**
     * Helper function that generates a random alphanumeric string
     * @param length    Desired length of the string
     * @return          Random alphanumeric string
     */
    private static String alphaNumericString(int length) {
        final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(CHARS.length());
            char randomChar = CHARS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    /**
     * Deletes a user by Id.
     * @param id    id of user to be deleted.
     */
    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}