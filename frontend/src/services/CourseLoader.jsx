import http from "./httpService";

const CourseLoader = {
  loadCourses: () => {
    return new Promise((resolve, reject) => {
      http.get("/courses").then(response => {
        console.log(response);
      });
      http.get("/courses").then((response) => {
        resolve(response.data);
      }, (error) => {
        reject("Kon opleidingen niet ophalen.");
      });
    });
  },
  addCourse: (course) => {
    http.post("/courses", course);
  }
}

export default CourseLoader;