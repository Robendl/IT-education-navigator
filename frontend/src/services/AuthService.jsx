import { createContext } from "react";
import http from "./httpService";

/* Context for logged-in user information */
export const UserContext = createContext({
  name: null,
  loggedIn: false,
  role: null
})

/* Enum containing the possible roles a user can have */
export const userRoles = {
  LIM_DATA_CONSUMER: 0,
  DATA_CONSUMER: 1,
  DATA_MANAGER: 2,
  ADMIN: 3
}

/* Enum containing various error codes that can be returned */
export const errorCodes = {
  ERR_LOGIN_INVALID: 1,
  ERR_NETWORK: 2,
  ERR_USERNAME_DUPLICATE: 3,
  ERR_INVALID_ROLE: 4,
  ERR_PASSWORD_INVALID: 5
}

/* Function that rejects a http error with a corresponding error code */
function handleHttpError(error, reject) {
  if (error.response && error.response.status === 404) {
    reject(errorCodes.ERR_LOGIN_INVALID);
  }
  if (error.response && error.response.status === 401) {
    reject(errorCodes.ERR_PASSWORD_INVALID);
  }
  if (error.response && error.response.status === 409) {
    reject(errorCodes.ERR_USERNAME_DUPLICATE);
  }
  if (error.code === "ERR_NETWORK") {
    reject(errorCodes.ERR_NETWORK);
  }
  reject(errorCodes.ERR_OTHER);
}

/* Function for loggin in that accepts an object containing a "username" and "password" */
/* Currently login information is stored in localStorage, which should be changed for security purposes */
function login(credentials) {
  return new Promise((resolve, reject) => {
    http.post('/auth/login', credentials)
      .then(response => {
        if (response.data.role) {
          if (!userRoles.hasOwnProperty(response.data.role)) {
            reject(errorCodes.ERR_INVALID_ROLE);
          }
          localStorage.setItem("user", JSON.stringify({
            role: userRoles[response.data.role],
            name: response.data.username
          }));
          resolve()
        }
      }, (error) => {
        handleHttpError(error, reject);
      });
  });

}

/* Function for loggin out */
function logout() {
  localStorage.removeItem("user");
  window.location.reload();
}

/* Function for checking if the user is logged in; returns a boolean */
function isLoggedIn() {
  let user = getUser();
  if (user) {
    return true;
  } else {
    return false;
  }
}

/* Function for getting the user object from localStorage */
function getUser() {
  return JSON.parse(localStorage.getItem("user"));
}

/* Function for getting the role of the current user */
function getRole() {
  let user = getUser();
  if (user) {
    return user.role;
  } else {
    return null;
  }
}

/* Function for getting username of logged in user */
function getUsername() {
  let user = getUser();
  if (user) {
    return user.name;
  } else {
    return null;
  }
}


/* Function for registering a user */
function register(userInfo) {
  return new Promise((resolve, reject) => {
    return http
      .post('/auth/register', userInfo)
      .then(response => {
        resolve(response);
      }, (error) => {
        handleHttpError(error, reject);
      });
  });
}

function changePassword(userInfo) {
  userInfo.username = getUsername();
  return new Promise((resolve, reject) => {
    http.put('/auth/password', userInfo)
      .then(response => {
        console.log(response);
        resolve()
      }, (error) => {
        if (error.response && error.response.status === 401) {
          reject(errorCodes.ERR_LOGIN_INVALID)
        }
      })
  });
}

const AuthService = {
  login,
  logout,
  register,
  getUser,
  getRole,
  isLoggedIn,
  changePassword
};

export default AuthService;