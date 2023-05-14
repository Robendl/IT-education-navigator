package se.rijksoverheid.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
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
    private Page<Course> coursePage;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        mockCourseRepository = mock(CourseRepository.class);
        mockProvinceRepository = mock(ProvinceRepository.class);
        mockCourse = mock(Course.class);
        mockProvince = mock(Province.class);
        courseService = new CourseService(mockCourseRepository, mockProvinceRepository);
        courseList = new ArrayList<>();
        courseList.add(mockCourse);
        coursePage = new PageImpl<>(courseList);
    }

    @Test
    void testGetAllCourses() {
        String search = "search";
        boolean archived = true;
        String level =  "level";
        String region = "region";
        long provinceId = 2;
        Pageable mockPageable = mock(Pageable.class);
        when(mockCourseRepository.searchAndFilterAndOrderCourses(search, archived, level, region, provinceId, mockPageable)).thenReturn(coursePage);

        Page<Course> courses = courseService.getCourses(search, archived, level, region, provinceId, mockPageable);
        assertEquals(courses, coursePage);
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
