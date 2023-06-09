package se.rijksoverheid.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import se.rijksoverheid.dto.*;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.filter.CourseFilter;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository mockCourseRepository;
    @Mock
    private ProvinceRepository mockProvinceRepository;
    @InjectMocks
    private CourseService courseService;
    @Mock
    private Course mockCourse;
    @Mock
    private CourseRequestDTO mockCourseRequestDTO;
    @Mock
    private CourseResponseDTO mockCourseResponseDTO;
    @Mock
    private Province mockProvince;
    private List<Course> courseList;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        courseList = new ArrayList<>();
        courseList.add(mockCourse);
    }

    @Test
    void testGetAllCourses() {
        String search = "search";
        boolean archived = true;
        List<String> levels = List.of("level");
        List<String> regions = List.of("region");
        List<Long> provinceIds = List.of(2L);
        List<String> courseTypes = List.of("courseType");
        CourseFilter filter = mock(CourseFilter.class);
        when(filter.getSearch()).thenReturn(search);
        when(filter.isArchived()).thenReturn(archived);
        when(filter.getLevels()).thenReturn(levels);
        when(filter.getRegions()).thenReturn(regions);
        when(filter.getProvinceIds()).thenReturn(provinceIds);
        when(filter.getCourseTypes()).thenReturn(courseTypes);
        Sort sort = mock(Sort.class);
        when(mockCourseRepository.searchAndFilterAndOrderCourses(
                search,
                archived,
                levels,
                regions,
                provinceIds,
                courseTypes,
                sort
        )).thenReturn(courseList);

        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            ProvinceDTO mockProvinceDTO = mock(ProvinceDTO.class);
            mockMapper.when(() -> Mapper.map(mockProvince, ProvinceDTO.class)).thenReturn(mockProvinceDTO);
            LimitedCourseResponseDTO mockCourseDTO = mock(LimitedCourseResponseDTO.class);
            mockMapper.when(() -> Mapper.map(mockCourse, LimitedCourseResponseDTO.class)).thenReturn(mockCourseDTO);
            List<CourseResponseDTO> courses = courseService.getCourses(filter, sort, authentication);
            List<CourseResponseDTO> check = List.of(mockCourseDTO);
            assertEquals(courses, check);
        }
    }

    @Test
    void testGetCourseById_Limited() {
        long id = 1;
        Collection authorities = mock(Collection.class);
        when(authentication.getAuthorities()).thenReturn(authorities);
        when(authorities.contains(any())).thenReturn(false);
        when(mockCourseRepository.findById(id)).thenReturn(Optional.of(mockCourse));
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            mockMapper.when(() -> Mapper.map(mockCourse, LimitedCourseResponseDTO.class)).thenReturn(mockCourseResponseDTO);
            assertEquals(mockCourseResponseDTO, courseService.getCourseById(id, authentication));
            mockMapper.verify(() -> Mapper.map(mockCourse, LimitedCourseResponseDTO.class));
        }
    }

    @Test
    void testGetCourseById_Full() {
        long id = 1;
        Collection authorities = mock(Collection.class);
        when(authentication.getAuthorities()).thenReturn(authorities);
        when(authorities.contains(any())).thenReturn(true);
        when(mockCourseRepository.findById(id)).thenReturn(Optional.of(mockCourse));
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            mockMapper.when(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class)).thenReturn(mockCourseResponseDTO);
            assertEquals(mockCourseResponseDTO, courseService.getCourseById(id, authentication));
            mockMapper.verify(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class));
        }
    }

    @Test
    void testGetCourseById_NotFound() {
        long id = 1;
        when(mockCourseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> courseService.getCourseById(id, authentication));
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
            mockMapper.when(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class)).thenReturn(mockCourseResponseDTO);
            assertEquals(mockCourseResponseDTO, courseService.save(mockCourseRequest));
        }
    }

    @Test
    void testSave_IncorrectProvinceId() {
        CourseRequestDTO mockCourseRequest = mock(CourseRequestDTO.class);
        long provinceId = 1;
        when(mockCourseRequest.getProvinceId()).thenReturn(provinceId);
        when(mockProvinceRepository.findById(provinceId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> courseService.save(mockCourseRequest));
    }

    @Test
    void testEditCourse_Successful() {
        long courseId = 1;
        long provinceId = 1;
        when(mockCourseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            when(mockCourseRequestDTO.getProvinceId()).thenReturn(provinceId);
            when(mockCourse.getProvince()).thenReturn(mockProvince);
            when(mockProvince.getId()).thenReturn(provinceId);
            when(mockCourseRepository.save(mockCourse)).thenReturn(mockCourse);
            mockMapper.when(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class)).thenReturn(mockCourseResponseDTO);
            assertEquals(mockCourseResponseDTO, courseService.edit(courseId, mockCourseRequestDTO));
            verify(mockCourseRepository).save(mockCourse);
            mockMapper.verify(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class));
        }
    }

    @Test
    void testEditCourse_IncorrectCourseId() {
        long courseId = 1;
        when(mockCourseRepository.findById(courseId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> courseService.edit(courseId, mockCourseRequestDTO));
    }

    @Test
    void testEditCourse_DifferentProvince() {
        long courseId = 1;
        long originalProvinceId = 1;
        long newProvinceId = 2;
        Province mockNewProvince = mock(Province.class);
        when(mockCourseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        try (MockedStatic<Mapper> mockMapper = Mockito.mockStatic(Mapper.class)) {
            when(mockCourseRequestDTO.getProvinceId()).thenReturn(newProvinceId);
            when(mockCourse.getProvince()).thenReturn(mockProvince);
            when(mockProvince.getId()).thenReturn(originalProvinceId);
            when(mockProvinceRepository.findById(newProvinceId)).thenReturn(Optional.of(mockNewProvince));
            when(mockCourseRepository.save(mockCourse)).thenReturn(mockCourse);
            mockMapper.when(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class)).thenReturn(mockCourseResponseDTO);
            assertEquals(mockCourseResponseDTO, courseService.edit(courseId, mockCourseRequestDTO));
            verify(mockCourseRepository).save(mockCourse);
            verify(mockCourse).setProvince(mockNewProvince);
            mockMapper.verify(() -> Mapper.map(mockCourse, FullCourseResponseDTO.class));
        }
    }

    @Test
    void testEditCourse_DifferentProvinceIncorrectProvinceId() {
        long courseId = 1;
        long originalProvinceId = 1;
        long newProvinceId = 2;
        when(mockCourseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(mockCourseRequestDTO.getProvinceId()).thenReturn(newProvinceId);
        when(mockCourse.getProvince()).thenReturn(mockProvince);
        when(mockProvince.getId()).thenReturn(originalProvinceId);
        when(mockProvinceRepository.findById(newProvinceId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> courseService.edit(courseId, mockCourseRequestDTO));
    }

    @Test
    void testDeleteById() {
        long courseId = 1;
        courseService.deleteById(courseId);
        verify(mockCourseRepository,times(1)).deleteById(courseId);
    }
}
