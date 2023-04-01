package se.rijksoverheid.security.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.security.dto.UserDTO;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("There exists no user with username: " + username));
        return user;
    }

    public UserDTO save(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return Mapper.map(userRepository.save(user), UserDTO.class);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
