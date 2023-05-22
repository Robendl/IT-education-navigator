package se.rijksoverheid.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository used for interacting with Province data.
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    /**
     * Finds a province by id.
     * @param id    id of province.
     * @return      optional province.
     */
    Optional<Province> findById(long id);

    List<Province> findAll();
}
