package se.rijksoverheid.controller;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.model.Course;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

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
        String search = "";
        boolean archived = false;
        int page = 0,size = 500;
        String orderBy = "name";
        Sort.Direction direction = Sort.Direction.ASC;
        CourseResponseDTO mockCourseDTO = mock(CourseResponseDTO.class);
        List<CourseResponseDTO> courses = new ArrayList<>();
        courses.add(mockCourseDTO);

        when(courseService.getCourses(anyString(),anyBoolean(),any(Pageable.class))).thenReturn(courses);

        assertEquals(courses, courseController.getCourses(search, archived, page, size, orderBy, direction).getBody());

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