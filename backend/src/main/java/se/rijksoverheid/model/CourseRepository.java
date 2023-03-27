package se.rijksoverheid.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
//    Page<Course> findCourseByArchivedEquals(Boolean archived);

    @Query(value = "SELECT * FROM rijksoverheid.courses c WHERE CONCAT_WS(' ', c.*) LIKE %:search% AND c.archived = :archived",
            countQuery = "SELECT COUNT(*) FROM rijksoverheid.courses c WHERE CONCAT_WS(' ', c.*) ILIKE %:search% AND c.archived = :archived",
            nativeQuery = true)
    Page<Course> searchAllFields(@Param("search") String search, @Param("archived") boolean archived, Pageable pageable);
}
