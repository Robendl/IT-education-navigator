//package se.rijksoverheid.model;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class CourseRepositoryTest {
//
//    @Autowired
//    private CourseRepository courseRepository;
//
//    private Course setCourse(int id) {
//        Course course = new Course();
//        course.setId(id);
//        course.setArchived(false);
//        course.setName("name" + id);
//        course.setInstitution("in" + id);
//        course.setLocation("loc" + id);
//        Province province = new Province();
//        course.setProvince(province);
//        course.setLevel("lev" + id);
//        course.setCourseType("type" + id);
//        course.setHousekeepingRelated(false);
//        course.setTimeOccupation("timeOc" + id);
//        course.setRegion("reg" + id);
//        course.setCollaboration(false);
//        course.setResponsibleTaskForce("resF" + id);
//        course.setProfessor("prof" + id);
//        course.setContact("con" + id);
//        course.setWeb("web" + id);
//        course.setExplanation("ex" + id);
//        return course;
//    }
//
//    @BeforeEach
//    void setUp() {
//        Course course = setCourse(0);
//        courseRepository.save(course);
//    }
//
//    @AfterEach
//    void tearDown() {
//        courseRepository.deleteAll();
//    }
//
////    @Test
////    void deleteById() {
////    }
////
////    @Test
////    void save() {
////    }
//
//    @Test
//    void testSearchAllFields() {
//        Pageable pageable = PageRequest.of(0, 500, Sort.by("ASC", "name"));
//        Page<Course> coursePage = courseRepository.searchAndFilterAndOrderCourses("name0",false, "level", "region", 2, pageable);
//        assertEquals(1,coursePage.getContent().size());
//        assertEquals(0, coursePage.getContent().get(0).getId());
//        assertEquals(false, coursePage.getContent().get(0).getArchived());
//        assertEquals("in0", coursePage.getContent().get(0).getInstitution());
//    }
//}