import axios from "axios";
import http from "./httpService";

var loadController = new AbortController();

/* Enum containing various error codes that can be returned */
export const errorCodes = {
  ERR_LOGIN_INVALID: 1,
  ERR_NETWORK: 2,
  ERR_CANCELED: 3,
  ERR_OTHER: 4
}

/* Function for loading courses
 * Accepts a string that holds filters as search parameters (e.g. "?provinceId=1&archived=1")
 * Returns a promise that, once resolved, returns an object with all courses that match the filters */
function loadCourses(filters) {
  loadController.abort();
  loadController = new AbortController();
  return new Promise((resolve, reject) => {
    http.get(`/courses${filters ? "/" + filters : ""}`, {signal: loadController.signal}).then((response) => {
      resolve(response.data);
    }, (error) => {
      if (axios.isCancel(error)) {
        reject(errorCodes.ERR_CANCELED);
      }
      if (error.response && error.response.status === 401) {
        reject(errorCodes.ERR_LOGIN_INVALID)
      }
      if (error.code === "ERR_NETWORK") {
        reject(errorCodes.ERR_NETWORK)
      }
      reject(errorCodes.ERR_OTHER);
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