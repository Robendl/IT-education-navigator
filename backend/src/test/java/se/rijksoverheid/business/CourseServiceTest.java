package se.rijksoverheid.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import se.rijksoverheid.dto.CourseResponseDTO;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private CourseService courseService;

    private Course setCourse() {
        Course course = new Course();
        course.setId(0);
        course.setArchived(false);
        course.setName("name0");
        course.setInstitution("in0");
        course.setLocation("loc");
        Province province = new Province();
        course.setProvince(province);
        course.setLevel("lev0");
        course.setCourseType("type0");
        course.setHousekeepingRelated(false);
        course.setTimeOccupation("timeOc0");
        course.setRegion("reg0");
        course.setCollaboration(false);
        course.setResponsibleTaskForce("resF0");
        course.setProfessor("prof0");
        course.setContact("con0");
        course.setWeb("web0");
        course.setExplanation("ex0");
        return course;
    }

    @BeforeEach
    void setMockOutput() {
        List<Course> courseList = new ArrayList<>();
        Course course = setCourse();
        courseList.add(course);
        Page<Course> coursePage = new PageImpl<>(courseList);
        when(courseRepository.searchAllFields(anyString(),anyBoolean(),any(Pageable.class))).thenReturn(coursePage);
    }

    @Test
    void testGetAllCourses() {
        List<CourseResponseDTO> courseList = courseService.getCourses("", false, PageRequest.of(0,500));
        assertEquals(1, courseList.size());
        assertEquals(0, courseList.get(0).getId(), 0);
        assertEquals(false, courseList.get(0).getArchived());
        assertEquals("in0", courseList.get(0).getInstitution());
    }
}
