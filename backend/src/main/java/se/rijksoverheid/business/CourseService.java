package se.rijksoverheid.business;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.rijksoverheid.dto.*;
import se.rijksoverheid.exceptions.webexceptions.NotFoundException;
import se.rijksoverheid.filter.CourseFilter;
import se.rijksoverheid.mapper.Mapper;
import se.rijksoverheid.model.Course;
import se.rijksoverheid.model.CourseRepository;
import se.rijksoverheid.model.Province;
import se.rijksoverheid.model.ProvinceRepository;
import se.rijksoverheid.security.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * @return              List of courses
     */

    public List<CourseResponseDTO> getCourses(CourseFilter filter, Sort sort, Authentication authentication) {
        List<Course> courses = courseRepository.searchAndFilterAndOrderCourses(
                filter.getSearch(),
                filter.isArchived(),
                filter.getLevels(),
                filter.getRegions(),
                filter.getProvinceIds(),
                filter.getCourseTypes(),
                sort
        );
        List<CourseResponseDTO> courseDTOs = new ArrayList<>();
        for(Course course: courses) {
            ProvinceDTO provinceDTO = Mapper.map(course.getProvince(), ProvinceDTO.class);
            CourseResponseDTO courseDTO = convertCourseAppropriately(course, authentication);
            courseDTO.setProvince(provinceDTO);
            courseDTOs.add(courseDTO);
        }
        return courseDTOs;
    }

    public CourseResponseDTO getCourseById(long id, Authentication authentication) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course with id " + id + "could not be found."));
        return convertCourseAppropriately(course, authentication);
    }

    private CourseResponseDTO convertCourseAppropriately(Course course, Authentication authentication) {
        Collection<?> authorities = authentication.getAuthorities();
        if(authorities.contains(new SimpleGrantedAuthority(User.Role.ADMIN.toString()))
                || authorities.contains(new SimpleGrantedAuthority(User.Role.DATA_CONSUMER.toString()))
                || authorities.contains(new SimpleGrantedAuthority(User.Role.DATA_MANAGER.toString()))) {
            return Mapper.map(course, FullCourseResponseDTO.class);
        } else {
            return Mapper.map(course, LimitedCourseResponseDTO.class);
        }
    }

    /**
     * Saves a course to the database
     * @param courseDTO                     Data Transfer Object containing information on course to be created.
     * @return                              The saved course.
     */
    @Transactional
    public CourseResponseDTO save(CourseRequestDTO courseDTO) {
        Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(() ->
                new NotFoundException("Province with id " + courseDTO.getProvinceId() + " could not be found while " +
                        "trying to save new course"));
        Course course = Mapper.map(courseDTO, Course.class);
        course.setProvince(province);
        return Mapper.map(courseRepository.save(course), FullCourseResponseDTO.class);
    }

    /**
     * Edits a course, can also be used for archiving.
     * @param courseId                      id of course to be edited.
     * @param courseDTO                     Data Transfer Object containing new data to be saved.
     * @return                              The new Course object
     */
    @Transactional
    public CourseResponseDTO edit(Long courseId, CourseRequestDTO courseDTO) {
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new NotFoundException("Course with id " + courseId + " could not be found while trying to edit"));
        Mapper.map(courseDTO, course);
        if(courseDTO.getProvinceId() != course.getProvince().getId()) {
            Province province = provinceRepository.findById(courseDTO.getProvinceId()).orElseThrow(() ->
                    new NotFoundException("Province with id " + courseDTO.getProvinceId() + " could not be found while " +
                            "trying to edit course with id" + courseId));
            course.setProvince(province);
        }
        return Mapper.map(courseRepository.save(course), FullCourseResponseDTO.class);
    }

    /**
     * Deletes a course by Id.
     * @param id    id of course to be deleted.
     */
    public void deleteById(long id) {
        courseRepository.deleteById(id);
    }
}
