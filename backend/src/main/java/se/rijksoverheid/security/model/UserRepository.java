package se.rijksoverheid.security.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository class used for interacting with users in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Retrieve user by username.
     * @param username  username of user
     * @return          user
     */
    Optional<User> findUserByUsername(String username);


    List<User> findAllUserByUsername(String search, Sort sort);

    /**
     * Checks if username already exists in the database.
     * @param username  username
     * @return          true if it exists, false if not.
     */
    boolean existsByUsername(String username);

    Optional<User> findById(long id);

    /**
     * Method used for deleting user.
     * @param id    id of user to be deleted.
     */
    void deleteById(long id);
}
