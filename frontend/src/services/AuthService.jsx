import { createContext } from "react";
import http from "./httpService";

export const UserContext = createContext({
  name: null,
  loggedIn: false,
  role: null
})

export const errorCodes = {
  ERR_LOGIN_INVALID: 1,
  ERR_NETWORK: 2,
  ERR_USERNAME_DUPLICATE: 3
}

function login(credentials) {
  return new Promise((resolve, reject) => {
    http.post('/auth/login', credentials)
      .then(response => {
        if (response.data.token) {
          localStorage.setItem("user", JSON.stringify({
            role: response.data.role,
            token: response.data.token,
            name: credentials.username
          }));
          resolve()
        }
    }, (error) => {
      if (error.response && error.response.status === 401) {
        reject(errorCodes.ERR_LOGIN_INVALID)
      }
      if (error.code === "ERR_NETWORK") {
        reject(errorCodes.ERR_NETWORK)
      } else {
        reject(error.code);
      }
    });
  });
    
}

function logout() {
  localStorage.removeItem("user");
  window.location.reload();
}

function isLoggedIn() {
  let user = getUser();
  if (user && user.token) {
    return true;
  } else {
    return false;
  }
}

function getUser() {
  return JSON.parse(localStorage.getItem("user"));
}

function getRole() {
  let user = getUser();
  if (user && user.token) {
    return user.role;
  } else {
    return null;
  }
}

function register(userInfo) {
  return http
    .post('/auth/register', userInfo)
      .then(response => {
        console.log(response);
    });
}


const AuthService = {
  login,
  logout,
  register,
  getUser,
  getRole,
  isLoggedIn
};

export default AuthService;