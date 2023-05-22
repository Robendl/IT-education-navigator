package se.rijksoverheid.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

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
    Course save(Course course);

    /**
     * Method used for retrieving courses from the databse, a search can be performed and desired results can be further
     * specified through archived and pageable.
     * @param search        search string to be used for searching all string fields.
     * @param archived      return archived or unarchived courses.
     * @param pageable      page specification.
     * @return              Page of courses.
     */
    @Query("select c from Course c where " +
            "LOWER(CONCAT_WS(c.name, c.province.name, c.contact, c.courseType, c.explanation, c.institution, c.level, " +
            "c.location, c.professor, c.region, c.responsibleTaskForce, c.timeOccupation, c.web)) like LOWER(CONCAT('%', :search, '%')) " +
            "and c.archived = :archived " +
            "and (COALESCE(:levels, NULL) IS NULL or (c.level in :levels)) " +
            "and (COALESCE(:regions, NULL) IS NULL or (c.region in :regions)) " +
            "and (COALESCE(:provinceIds, NULL) IS NULL or (c.province.id in :provinceIds)) ")
    Page<Course> searchAndFilterAndOrderCourses(@Param("search") String search,
                                                @Param("archived") boolean archived,
                                                @Param("levels") Collection<String> levels,
                                                @Param("regions") Collection<String> regions,
                                                @Param("provinceIds") Collection<Long> provinceIds,
                                                Pageable pageable);
}
