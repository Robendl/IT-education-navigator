package se.rijksoverheid.security.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class used for interacting with User data.
 * Implements UserDetails so that it can be used in Spring Security context.
 */
@Entity
@Getter
@Setter
@Table(name = "users", schema = "rijksoverheid")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Enum for the different roles a user can have.
     */
    public enum Role {
        ADMIN,
        DATA_MANAGER,
        DATA_CONSUMER,
        LIM_DATA_CONSUMER
    }

    /**
     * Returns the authorities of the user, which is the role.
     * @return  the authorities of the user
     */
    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
