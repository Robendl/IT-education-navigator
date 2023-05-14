package se.rijksoverheid.controller;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.model.Course;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {


    @Mock
    private CourseService courseService;

    @InjectMocks CourseController courseController;


    @BeforeEach
    void setup() {
    }

    @Test
    public void testGetCourses() {
        String search = "search";
        boolean archived = false;
        int page = 0,size = 500;
        String orderBy = "name";
        String level = "level";
        String region = "region";
        long provinceId = 2;
        Sort.Direction direction = Sort.Direction.ASC;
        Course mockCourse = mock(Course.class);
        List<Course> courses = new ArrayList<>();
        courses.add(mockCourse);
        Page<Course> coursePage = new PageImpl<>(courses);

        when(courseService.getCourses(
                eq(search),
                eq(archived),
                eq(level),
                eq(region),
                eq(provinceId),
                any(PageRequest.class)))
                .thenReturn(coursePage);
        assertEquals(coursePage, courseController.getCourses(
                search,
                archived,
                level,
                region,
                provinceId,
                page,
                size,
                orderBy,
                direction).getBody());
    }

    @Test
    void testCreateCourse() {
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        Course mockCourse = mock(Course.class);
        when(courseService.save(mockCourseRequest)).thenReturn(mockCourse);
        assertEquals(mockCourse,courseController.createCourse(mockCourseRequest).getBody());
    }

    @Test
    void testEditCourse() {
        long id = 1;
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        Course mockCourse = mock(Course.class);
        when(courseService.edit(anyLong(),any(CourseRequestDTO.class))).thenReturn(mockCourse);
        assertEquals(mockCourse,courseController.editCourse(id,mockCourseRequest).getBody());
    }

    @Test
    void testEditCourse_IdNotFound() {
        long courseId = 2;
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        when(courseService.edit(courseId,mockCourseRequest)).thenThrow(EntityNotFoundException.class);
        assertEquals(ResponseEntity.notFound().build().getStatusCode(),courseController.editCourse(courseId,mockCourseRequest).getStatusCode());
    }

    @Test
    void testRemoveCourse() {
        long courseId = 1;
        doAnswer(invocation -> {
            assertEquals(courseId,(long)invocation.getArgument(0));
            return null;
        }).when(courseService).deleteById(courseId);
        courseService.deleteById(courseId);
    }
}