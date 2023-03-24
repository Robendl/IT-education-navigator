package se.rijksoverheid.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseDTO;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
    private CourseRepository courseRepository;
    private CourseService courseService;

    @GetMapping("")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @PostMapping("")
    public ResponseEntity<Course> createCourse(@RequestBody @Validated CourseDTO courseDTO) {
        try {
            return ResponseEntity.ok(courseService.save(courseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> editCourse(
            @PathVariable long id,
            @RequestBody @Valid CourseDTO courseDTO) {
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
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
