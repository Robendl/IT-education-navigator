import http from "./httpService";

/* Function for loading courses
 * Accepts a string that holds filters as search parameters (e.g. "?province=1&archived=1")
 * Returns a promise that, once resolved, returns an object with all courses that match the filters */
function loadCourses(filters) {
  return new Promise((resolve, reject) => {
    http.get(`/courses${filters ? "/" + filters : ""}`).then((response) => {
      resolve(response.data);
    }, (error) => {
      console.log(error);
      reject("Kon opleidingen niet ophalen.");
    });
  });
}

/* Function for adding a course
 * Accepts an object with course properties 
 * Returns a promise that, once resolved, returns the http response body */
function addCourse(course) {
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
}

/* Function for editing a course
 * Accepts an object with course properties (must include an "id" property)
 * Returns a promise that, once resolved, returns the http response body */
function editCourse(course) {
  return new Promise((resolve, reject) => {
    http.put(`/courses/${course.id}`, course, {
      headers: {
        'Content-type': 'application/json'
      }
    }).then((response) => {
      console.log(response);
      resolve(response);
    }, (error) => {
      reject("Kon opleiding niet bewerken.")
    });
  });
}

/* Function for archiving a course
 * Accepts an object with course properties (must include an "id" property)
 * Returns a promise that, once resolved, returns the http response body */
function archiveCourse(course) {
  return new Promise((resolve, reject) => {
    http.put(`/courses/${course.id}`, {...course, archived: true}, {
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

/* Function for restoring a course
 * Accepts an object with course properties (must include an "id" property)
 * Returns a promise that, once resolved, returns the http response body */
function restoreCourse(course) {
  return new Promise((resolve, reject) => {
    http.put(`/courses/${course.id}`, {...course, archived: false}, {
      headers: {
        'Content-type': 'application/json'
      }
    }).then((response) => {
      console.log(response);
      resolve(response);
    }, (error) => {
      reject("Kon opleiding niet restoren.")
    });
  });
}

/* Function for deleting a course
 * Accepts an object with course properties (must include an "id" property)
 * Returns a promise that, once resolved, returns the http response body */
function deleteCourse(course) {
  return new Promise((resolve, reject) => {
    http.delete(`/courses/${course.id}`, course, {
      headers: {
        'Content-type': 'application/json'
      }
    }).then((response) => {
      console.log(response);
      resolve(response);
    }, (error) => {
      reject("Kon opleiding niet verwijderen.")
    });
  });
}

const CourseLoader = {
  loadCourses,
  addCourse,
  editCourse,
  archiveCourse,
  restoreCourse,
  deleteCourse
}

export default CourseLoader;