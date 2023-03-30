package se.rijksoverheid.security.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
}
