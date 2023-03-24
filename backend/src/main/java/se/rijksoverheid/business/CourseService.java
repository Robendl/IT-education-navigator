package se.rijksoverheid.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.rijksoverheid.dto.CourseDTO;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

@AllArgsConstructor
@Service
public class CourseService {
    private CourseRepository courseRepository;
    private ProvinceRepository provinceRepository;
    private static final ModelMapper modelMapper = new ModelMapper();

    public Course save(CourseDTO courseDTO) {
        Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
        Course course = modelMapper.map(courseDTO, Course.class);
        course.setProvince(province);
        return courseRepository.save(course);
    }

    public Course edit(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        modelMapper.map(courseDTO, course);
        if(courseDTO.getProvinceId() != course.getProvince().getId()) {
            Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(EntityNotFoundException::new);
            course.setProvince(province);
        }
        return courseRepository.save(course);
    }
}
