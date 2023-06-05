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
import se.rijksoverheid.model.Course;

import javax.servlet.http.HttpServletRequest;
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
     * Endpoint for retrieving courses
     * @param search        every string field is checked for this search string when provided.
     * @param archived      determines whether to return unarchived or archived course, unarchived by default.
     * @param orderBy       what field to order by, name by default.
     * @param direction     direction to order by, can be ASC or DESC, ASC by default.
     * @return              List of courses.
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
        CourseFilter filter = getCourseFilter(search, archived, levels, regions, provinceIds, courseTypes);
        return ResponseEntity.ok(courseService.getCourses(filter, sort, authentication));
    }

    protected CourseFilter getCourseFilter(String search, boolean archived, List<String> levels, List<String> regions,
                                 List<Long> provinceIds, List<String> courseTypes) {
        CourseFilter filter = new CourseFilter();
        filter.setSearch(search);
        filter.setArchived(archived);
        filter.setLevels(levels);
        filter.setRegions(regions);
        filter.setProvinceIds(provinceIds);
        filter.setCourseTypes(courseTypes);
        return filter;
    }

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
    public ResponseEntity<Course> createCourse(@RequestBody @Validated CourseRequestDTO courseDTO) {
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
    public ResponseEntity<Course> editCourse(
            @PathVariable long id,
            @RequestBody @Valid CourseRequestDTO courseDTO,
            HttpServletRequest request) {
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
