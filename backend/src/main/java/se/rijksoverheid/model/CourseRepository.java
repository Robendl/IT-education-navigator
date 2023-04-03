package se.rijksoverheid.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository used for interacting with course data from the database.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    /**
     * Method used for deleting course.
     * @param id    id of course to be deleted
     */
    void deleteById(long id);

    /**
     * Method used for saving course.
     * @param course must not be {@literal null}.   course to be saved
     * @return saved course
     */
    Course save( Course course);

    /**
     * Method used for retrieving courses from the databse, a search can be performed and desired results can be further
     * specified through archived and pageable.
     * @param search        search string to be used for searching all string fields.
     * @param archived      return archived or unarchived courses.
     * @param pageable      page specification.
     * @return              Page of courses.
     */
    @Query(value = "SELECT * FROM rijksoverheid.courses c WHERE CONCAT_WS(' ', c.*) LIKE %:search% AND c.archived = :archived",
            countQuery = "SELECT COUNT(*) FROM rijksoverheid.courses c WHERE CONCAT_WS(' ', c.*) ILIKE %:search% AND c.archived = :archived",
            nativeQuery = true)
    Page<Course> searchAllFields(@Param("search") String search, @Param("archived") boolean archived, Pageable pageable);
}
