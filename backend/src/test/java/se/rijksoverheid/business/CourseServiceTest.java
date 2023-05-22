package se.rijksoverheid.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import se.rijksoverheid.dto.CoursePageDTO;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.dto.ProvinceDTO;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    private CourseRepository mockCourseRepository;
    private ProvinceRepository mockProvinceRepository;
    private Course mockCourse;
    private Province mockProvince;
    private List<Course> courseList;
    private CoursePageDTO mockCoursePage;
    private Page<Course> pageCourse;
    private CourseService courseService;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        mockCourseRepository = mock(CourseRepository.class);
        mockProvinceRepository = mock(ProvinceRepository.class);
        mockCourse = mock(Course.class);
        mockProvince = mock(Province.class);
        courseService = new CourseService(mockCourseRepository, mockProvinceRepository);
        courseList = new ArrayList<>();
        courseList.add(mockCourse);
        mockCoursePage = mock(CoursePageDTO.class);
        pageCourse = new PageImpl<>(courseList);
        authentication = mock(Authentication.class);
    }

    @Test
    void testGetAllCourses() {
        String search = "search";
        boolean archived = true;
        List<String> levels = List.of("level");
        List<String> regions = List.of("region");
        List<Long> provinceIds = List.of(2L);
        List<String> courseTypes = List.of("courseType");
        Pageable mockPageable = mock(Pageable.class);
        when(mockCourseRepository.searchAndFilterAndOrderCourses(search, archived, levels, regions, provinceIds, courseTypes, mockPageable)).thenReturn(pageCourse);

        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            // set up desired Mapper behaviour
            ProvinceDTO mockProvinceDTO = mock(ProvinceDTO.class);
            mockMapper.when(() -> Mapper.map(mockProvince, ProvinceDTO.class)).thenReturn(mockProvinceDTO);
            CourseResponseDTO mockCourseDTO = mock(CourseResponseDTO.class);
            mockMapper.when(() -> Mapper.map(mockCourse, CourseResponseDTO.class)).thenReturn(mockCourseDTO);
            CoursePageDTO courses = courseService.getCourses(search, archived, levels, regions, provinceIds, courseTypes, mockPageable, authentication);
//            assertEquals(courses, mockCoursePage);
        }
    }

    @Test
    void testSave_Successful() {
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        long provinceId = 1;
        when(mockCourseRequest.getProvinceId()).thenReturn(provinceId);
        when(mockProvinceRepository.findById(provinceId)).thenReturn(Optional.of(mockProvince));
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            mockMapper.when(() -> Mapper.map(mockCourseRequest, Course.class)).thenReturn(mockCourse);
            doNothing().when(mockCourse).setProvince(mockProvince);
            when(mockCourseRepository.save(mockCourse)).thenReturn(mockCourse);
            assertEquals(mockCourse, courseService.save(mockCourseRequest));
        }
    }

    @Test
    void testSave_IncorrectProvinceId() {
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        long provinceId = 1;
        when(mockCourseRequest.getProvinceId()).thenReturn(provinceId);
        when(mockProvinceRepository.findById(provinceId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> courseService.save(mockCourseRequest));
    }

    @Test
    void testEditCourse() {
        long courseId = 1;
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        long provinceId = 1;
        when(mockCourseRequest.getProvinceId()).thenReturn(provinceId);
        when(mockCourseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(mockCourse.getProvince()).thenReturn(mockProvince);
        when(mockProvinceRepository.findById(provinceId)).thenReturn(Optional.of(mockProvince));
        doNothing().when(mockCourse).setProvince(mockProvince);
        when(mockCourseRepository.save(mockCourse)).thenReturn(mockCourse);
        assertEquals(mockCourse, courseService.edit(courseId, mockCourseRequest));

//        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
//            doAnswer(invocation -> {
//                mockCourse = mockCourse2;
//                return null;
//            }).when(mockMapper); Mapper.map(mockCourseRequest, mockCourse);
//            doNothing().when(() -> Mapper.map(mockCourseRequest,mockCourse));
//            assertEquals(mockCourse, courseService.edit(courseId, mockCourseRequest));
//        }
    }

    @Test
    void testEditCourse_DifferentProvince() {
        long courseId = 1;
        long provinceId = 1, newProvinceId = 2;
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        Province newProvince = mock(Province.class);

        when(mockCourseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));

        //simulate different id's
        when(mockCourseRequest.getProvinceId()).thenReturn(newProvinceId);
        when(mockCourse.getProvince()).thenReturn(mockProvince);
        when(mockProvince.getId()).thenReturn(provinceId);

        when(mockProvinceRepository.findById(newProvinceId)).thenReturn(Optional.of(newProvince));
        when(mockCourseRepository.save(mockCourse)).thenReturn(mockCourse);

        assertEquals(mockCourse, courseService.edit(courseId, mockCourseRequest));
    }

    @Test
    void testDeleteById() {
        long courseId = 1;
        doAnswer(invocation -> {
            assertEquals(courseId,(long)invocation.getArgument(0));
            return null;
        }).when(mockCourseRepository).deleteById(courseId);
        mockCourseRepository.deleteById(courseId);
    }
}
