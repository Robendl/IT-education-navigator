package se.rijksoverheid.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;

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

    @PostMapping("")
    public ResponseEntity<Course> createCourse(@RequestBody @Validated CourseRequestDTO courseDTO) {
        try {
            return ResponseEntity.ok(courseService.save(courseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeCourse(@PathVariable long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
