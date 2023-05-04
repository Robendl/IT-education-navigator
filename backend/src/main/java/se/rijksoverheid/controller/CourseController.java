package se.rijksoverheid.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.model.Course;

import java.util.List;

/**
 * Holds endpoints to which the course data can be accessed/altered from the outside world.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseController {
    private CourseService courseService;

    /**
     * Endpoint for retrieving courses
     * @param search        every string field is checked for this search string when provided.
     * @param archived      determines whether to return unarchived or archived course, unarchived by default.
     * @param page          page number of page to return, 0 by default.
     * @param size          size of page to return, 500 by default.
     * @param orderBy       what field to order by, name by default.
     * @param direction     direction to order by, can be ASC or DESC, ASC by default.
     * @return              List of courses.
     */
    @Transactional
    @GetMapping("")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "false") boolean archived,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "500") int size,
            @RequestParam(value = "order-by", required = false, defaultValue = "name") String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));
        return ResponseEntity.ok(courseService.getCourses(search, archived, pageable));
    }

    /**
     * Endpoint for saving a course in the database.
     * @param courseDTO     Data Transfer Object holding the data to be used for creating course entity.
     * @return              The saved Course.
     */
    @PostMapping("")
    public ResponseEntity<Course> createCourse(@RequestBody @Validated CourseRequestDTO courseDTO) {
        try {
            return ResponseEntity.ok(courseService.save(courseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint used for editing course.
     * @param id            id of the course to be edited.
     * @param courseDTO     Data Transfer Object containing new data for course.
     * @return              The saved course or bad request when invalid provinceId is provided or not found when
     *                      no course with the given id can be found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> editCourse(
            @PathVariable long id,
            @RequestBody @Valid CourseRequestDTO courseDTO) {
        try {
            return ResponseEntity.ok(courseService.edit(id, courseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
