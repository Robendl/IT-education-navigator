package se.rijksoverheid.controller;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.Authentication;
import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.filter.CourseFilter;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.model.Course;


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
    void testGetCourses() {
        String search = "search";
        boolean archived = false;
        String orderBy = "name";
        List<String> levels = List.of("level");
        List<String> regions = List.of("region");
        List<Long> provinceIds = List.of(2L);
        List<String> courseTypes = List.of("courseType");
        Sort.Direction direction = Sort.Direction.ASC;
        CourseResponseDTO mockCourse = mock(CourseResponseDTO.class);
        List<CourseResponseDTO> courses = new ArrayList<>();
        courses.add(mockCourse);
        CourseFilter filter = mock(CourseFilter.class);
        Sort sort = mock(Sort.class);
        Authentication authentication = mock(Authentication.class);

        try (MockedStatic<Sort> mockSort = Mockito.mockStatic(Sort.class)) {
            mockSort.when(() -> Sort.by(direction, orderBy)).thenReturn(sort);
            CourseController spyCourseController = spy(courseController);
            doReturn(filter).when(spyCourseController).getCourseFilter(
                    search,
                    archived,
                    levels,
                    regions,
                    provinceIds,
                    courseTypes
            );

            when(courseService.getCourses(
                    filter,
                    sort,
                    authentication)
            ).thenReturn(courses);

            assertEquals(courses, spyCourseController.getCourses(
                    authentication,
                    search,
                    archived,
                    levels,
                    regions,
                    provinceIds,
                    courseTypes,
                    orderBy,
                    direction).getBody()
            );
        }
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
        when(courseService.edit(courseId,mockCourseRequest)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> courseController.editCourse(courseId,mockCourseRequest));
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