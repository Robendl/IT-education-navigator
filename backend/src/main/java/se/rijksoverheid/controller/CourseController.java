package se.rijksoverheid.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.filter.CourseFilter;

import javax.validation.Valid;
import java.util.List;

/**
 * Holds endpoints to which the course data can be accessed/altered from the outside world.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;

    /**
     * Endpoint for getting all courses.
     * @param authentication    Authentication object containing the user's credentials.
     * @param search            Search string to filter courses by.
     * @param archived          Boolean to filter courses by.
     * @param levels            List of levels to filter courses by.
     * @param regions           List of regions to filter courses by.
     * @param provinceIds       List of province ids to filter courses by.
     * @param courseTypes       List of course types to filter courses by.
     * @param orderBy           String to order courses by.
     * @param direction         Direction to order courses by.
     * @return                  List of courses.
     */
    @Transactional
    @GetMapping("")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(
            Authentication authentication,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "false") boolean archived,
            @RequestParam(required = false, defaultValue = "") List<String> levels,
            @RequestParam(required = false, defaultValue = "") List<String> regions,
            @RequestParam(value = "province-ids", required = false, defaultValue = "") List<Long> provinceIds,
            @RequestParam(value = "course-types", required = false, defaultValue = "") List<String> courseTypes,
            @RequestParam(value = "order-by", required = false, defaultValue = "name") String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, orderBy);
        CourseFilter filter = new CourseFilter(search, archived, levels, regions, provinceIds, courseTypes);
        return ResponseEntity.ok(courseService.getCourses(filter, sort, authentication));
    }

    /**
     * Endpoint for getting a course by id.
     * @param id                id of the course to be retrieved.
     * @param authentication    Authentication object containing the user's credentials.
     * @return                  The course with the given id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable long id, Authentication authentication) {
        return ResponseEntity.ok(courseService.getCourseById(id, authentication));
    }

    /**
     * Endpoint for saving a course in the database.
     * @param courseDTO     Data Transfer Object holding the data to be used for creating course entity.
     * @return              The saved Course.
     */
    @PostMapping("")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody @Validated CourseRequestDTO courseDTO) {
        return ResponseEntity.ok(courseService.save(courseDTO));
    }

    /**
     * Endpoint used for editing course.
     * @param id            id of the course to be edited.
     * @param courseDTO     Data Transfer Object containing new data for course.
     * @return              The saved course or bad request when invalid provinceId is provided or not found when
     *                      no course with the given id can be found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> editCourse(
            @PathVariable long id,
            @RequestBody @Valid CourseRequestDTO courseDTO) {
        return ResponseEntity.ok(courseService.edit(id, courseDTO));
    }

    /**
     * Endpoint used for deleting a course.
     * @param id    id of course to be deleted.
     * @return      No content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeCourse(@PathVariable long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
