package se.rijksoverheid.controller;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import se.rijksoverheid.business.CourseService;
import se.rijksoverheid.dto.CourseResponseDTO;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {


    @Mock
    private CourseService courseService;

    @InjectMocks CourseController courseController;


    @BeforeEach
    void setMockOutput() {
        List<CourseResponseDTO> courseDTOList = new ArrayList<>();
        CourseResponseDTO courseDTO = new CourseResponseDTO();
        courseDTO.setId(0);
        courseDTO.setArchived(false);
        courseDTO.setName("course 0");
        courseDTO.setInstitution("Institute 0");
        courseDTOList.add(courseDTO);
        when(courseService.getCourses(anyString(),anyBoolean(),any(Pageable.class))).thenReturn(courseDTOList);
    }

    @Test
    public void getCourses() throws Exception {
        ResponseEntity<List<CourseResponseDTO>> responseEntity = courseController.getCourses("",false,0,500,"name", Sort.Direction.ASC);
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(0, responseEntity.getBody().get(0).getId(),0);
        assertEquals(false, responseEntity.getBody().get(0).getArchived());
        assertEquals("Institute 0", responseEntity.getBody().get(0).getInstitution());
    }

//    @Test
//    void createCourse() {
//    }
//
//    @Test
//    void editCourse() {
//    }
//
//    @Test
//    void removeCourse() {
//    }
}