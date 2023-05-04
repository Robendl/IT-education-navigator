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
        Pageable mockPageable = mock(Pageable.class);
        when(mockCourse.getProvince()).thenReturn(mockProvince);
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            // set up desired Mapper behaviour
            ProvinceDTO mockProvinceDTO = mock(ProvinceDTO.class);
            mockMapper.when(() -> Mapper.map(mockProvince, ProvinceDTO.class)).thenReturn(mockProvinceDTO);
            CourseResponseDTO mockCourseDTO = mock(CourseResponseDTO.class);
            mockMapper.when(() -> Mapper.map(mockCourse, CourseResponseDTO.class)).thenReturn(mockCourseDTO);

            when(mockCourseRepository.searchAllFields(search, archived, mockPageable)).thenReturn(coursePage);

            List<CourseResponseDTO> courses = courseService.getCourses(search, archived, mockPageable);
            verify(mockCourseDTO).setProvince(mockProvinceDTO);
            assertEquals(courses.size(), courseList.size());
            assertEquals(courses.get(0), mockCourseDTO);
        }
    }

    @Test
    void testSaveSuccessful() {
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
    void testSaveIncorrectProvinceId() {
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        long provinceId = 1;
        when(mockCourseRequest.getProvinceId()).thenReturn(provinceId);
        when(mockProvinceRepository.findById(provinceId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> courseService.save(mockCourseRequest));
    }
}
