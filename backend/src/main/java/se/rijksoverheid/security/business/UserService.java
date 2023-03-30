package se.rijksoverheid.security.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.security.dto.UserDTO;
import se.rijksoverheid.security.model.User;
import se.rijksoverheid.security.model.UserRepository;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("There exists no user with username: " + username));
        user.setAuthorities(new SimpleGrantedAuthority(user.getAuthorityString()));
        return user;
    }

    public User save(UserDTO userDTO) {
        User user = Mapper.map(userDTO, User.class);
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
