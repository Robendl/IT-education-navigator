package se.rijksoverheid.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@AllArgsConstructor
@Service
public class CourseService {
    private CourseRepository courseRepository;
    private ProvinceRepository provinceRepository;

    @Transactional
    public List<CourseResponseDTO> getCourses(String search, boolean archived, int page, int size, String orderBy, Sort.Direction direction ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));
        Page<Course> coursePage = courseRepository.searchAllFields(search, archived, pageable);
        List<CourseResponseDTO> courses = new ArrayList<>();
        for(Course course: coursePage.getContent()) {
            ProvinceDTO provinceDTO = Mapper.map(course.getProvince(), ProvinceDTO.class);
            CourseResponseDTO courseDTO = Mapper.map(course, CourseResponseDTO.class);
            courseDTO.setProvince(provinceDTO);
            courses.add(courseDTO);
        }
        return courses;
    }

    @Transactional
    public Course save(CourseRequestDTO courseDTO) {
        Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
        Course course = Mapper.map(courseDTO, Course.class);
        course.setProvince(province);
        return courseRepository.save(course);
    }

    @Transactional
    public Course edit(Long courseId, CourseRequestDTO courseDTO) {
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        Mapper.map(courseDTO, course);
        if(courseDTO.getProvinceId() != course.getProvince().getId()) {
            Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
            course.setProvince(province);
        }
        return courseRepository.save(course);
    }
}
