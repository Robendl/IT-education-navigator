package se.rijksoverheid.business;

import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rijksoverheid.dto.CourseRequestDTO;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;

import java.util.List;
import java.util.Optional;

/**
 * Responsible for all business logic regarding the courses.
 */
@AllArgsConstructor
@Service
public class CourseService {
    private CourseRepository courseRepository;
    private ProvinceRepository provinceRepository;

    /**
     * Retrieves a list of courses.
     * @param search        search string that will be used for looking in all string fields
     * @param archived      determines whether to retrieve unarchived or archived courses
     * @param pageable      Pageable object that contains information on which page of what size to retrieve in what order
     * @return              List of courses
     */

    public Page<Course> getCourses(String search, boolean archived, List<String> levels, List<String> regions, List<Long> provinceIds, Pageable pageable ) {
        return courseRepository.searchAndFilterAndOrderCourses(search, archived, levels, regions, provinceIds, pageable);
    }

    public Optional<Course> getCourseById(long id) {
        return courseRepository.findById(id);
    }

    /**
     * Saves a course to the database
     * @param courseDTO                     Data Transfer Object containing information on course to be created.
     * @throws IllegalArgumentException     when no province with the given provinceId can be found.
     * @return                              The saved course.
     */
    @Transactional
    public Course save(CourseRequestDTO courseDTO) throws IllegalArgumentException {
        Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
        Course course = Mapper.map(courseDTO, Course.class);
        course.setProvince(province);
        return courseRepository.save(course);
    }

    /**
     * Edits a course, can also be used for archiving.
     * @param courseId                      id of course to be edited.
     * @param courseDTO                     Data Transfer Object containing new data to be saved.
     * @throws EntityNotFoundException      when the no course with courseId can be found.
     * @throws IllegalArgumentException     when no province with the given provinceId can be found.
     * @return                              The new Course object
     */
    @Transactional
    public Course edit(Long courseId, CourseRequestDTO courseDTO) throws EntityNotFoundException, IllegalArgumentException {
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        Mapper.map(courseDTO, course);
        if(courseDTO.getProvinceId() != course.getProvince().getId()) {
            Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(IllegalArgumentException::new);
            course.setProvince(province);
        }
        return courseRepository.save(course);
    }

    /**
     * Deletes a course by Id.
     * @param id    id of course to be deleted.
     */
    public void deleteById(long id) {
        courseRepository.deleteById(id);
    }
}
