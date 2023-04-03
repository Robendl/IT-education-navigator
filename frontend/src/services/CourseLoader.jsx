import http from "./httpService";

const CourseLoader = {
  loadCourses: () => {
    return new Promise((resolve, reject) => {
      http.get("/courses").then((response) => {
        resolve(response.data);
      }, (error) => {
        console.log(error);
        reject("Kon opleidingen niet ophalen.");
      });
    });
  },
  addCourse: (course) => {
    return new Promise((resolve, reject) => {
      http.post("/courses", course, {
        headers: {
          'Content-type': 'application/json'
        }
      }).then((response) => {
        console.log(response);
        resolve(response);
      }, (error) => {
        reject("Kon opleiding niet toevoegen.")
      });
    });
  },
  archiveCourse: (course) => {
    return new Promise((resolve, reject) => {
      course.archived = true;
      http.put(`/courses/${course.id}`, course, {
        headers: {
          'Content-type': 'application/json'
        }
      }).then((response) => {
        console.log(response);
        resolve(response);
      }, (error) => {
        reject("Kon opleiding niet archiveren.")
      });
    });
  }
}

export default CourseLoader;