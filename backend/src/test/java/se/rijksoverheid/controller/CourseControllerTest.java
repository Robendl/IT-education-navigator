package se.rijksoverheid.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.dto.LimitedCourseResponseDTO;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.filter.CourseFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        LimitedCourseResponseDTO mockCourse = mock(LimitedCourseResponseDTO.class);
        List<CourseResponseDTO> courses = new ArrayList<>();
        courses.add(mockCourse);
        Authentication authentication = mock(Authentication.class);
        ArgumentCaptor<CourseFilter> filterCaptor = ArgumentCaptor.forClass(CourseFilter.class);
        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        when(courseService.getCourses(
                filterCaptor.capture(),
                sortCaptor.capture(),
                any())
        ).thenReturn(courses);
        assertEquals(courses, courseController.getCourses(
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
        assertEquals(orderBy, Objects.requireNonNull(sortCaptor.getValue().getOrderFor(orderBy)).getProperty());
        assertEquals(direction, Objects.requireNonNull(sortCaptor.getValue().getOrderFor(orderBy)).getDirection());
        assertEquals(search, filterCaptor.getValue().getSearch());
        assertEquals(archived, filterCaptor.getValue().isArchived());
        assertEquals(levels, filterCaptor.getValue().getLevels());
        assertEquals(regions, filterCaptor.getValue().getRegions());
        assertEquals(provinceIds, filterCaptor.getValue().getProvinceIds());
        assertEquals(courseTypes, filterCaptor.getValue().getCourseTypes());
    }

    @Test
    void testGetCourseById() {
        Authentication authentication = mock(Authentication.class);
        long id = 1;
        CourseResponseDTO mockCourse = mock(CourseResponseDTO.class);
        when(courseService.getCourseById(id, authentication)).thenReturn(mockCourse);
        assertEquals(mockCourse, courseController.getCourseById(id, authentication).getBody());
    }

    @Test
    void testCreateCourse() {
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        CourseResponseDTO mockCourse = mock(CourseResponseDTO.class);
        when(courseService.save(mockCourseRequest)).thenReturn(mockCourse);
        assertEquals(mockCourse,courseController.createCourse(mockCourseRequest).getBody());
    }

    @Test
    void testEditCourse() {
        long id = 1;
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        CourseResponseDTO mockCourse = mock(CourseResponseDTO.class);
        when(courseService.edit(id,mockCourseRequest)).thenReturn(mockCourse);
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
        ResponseEntity<Object> response = courseController.removeCourse(courseId);
        verify(courseService,times(1)).deleteById(courseId);
        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(), response);
    }
}