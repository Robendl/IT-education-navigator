package se.rijksoverheid.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rijksoverheid.dto.CourseDTO;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

@AllArgsConstructor
@Service
public class CourseService {
    private CourseRepository courseRepository;
    private ProvinceRepository provinceRepository;

    @Transactional
    public Course save(CourseDTO courseDTO) {
        Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
        Course course = Mapper.map(courseDTO, Course.class);
//        course.setProvince(province);
        return courseRepository.save(course);
    }

    @Transactional
    public Course edit(Long courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        Mapper.map(courseDTO, course);
        if(courseDTO.getProvinceId() != course.getProvince().getId()) {
            Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
            course.setProvince(province);
        }
        return courseRepository.save(course);
    }
}
