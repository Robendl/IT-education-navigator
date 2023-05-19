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

function handleHttpError(error, reject) {
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
}

function loadUsers() {
  loadController.abort();
  loadController = new AbortController();
  return new Promise((resolve, reject) => {
    http.get(`/user`, { signal: loadController.signal }).then((response) => {
      resolve(response.data);
    }, (error) => {
      handleHttpError(error, reject);
    })
  });
}

/* Function for changing the role of a user
 * Accepts a userId and the new role (which is a string key from the AuthService userRoles enum).
 * Returns a promise that, once resolved, returns the http response body */
function changeUserPermissions(userId, newRole) {
  return new Promise((resolve, reject) => {
    http.put(`/user/perm/${userId}`, { role: newRole }, {
      headers: {
        'Content-type': 'application/json'
      }
    }).then((response) => {
      resolve(response.data);
    }, (error) => {
      handleHttpError(error, reject);
    })
  });
}

function resetUserPassword(userId) {
  return new Promise((resolve, reject) => {
    http.put(`/user/password/${userId}/reset`).then((response) => {
      resolve(response.data);
    }, (error) => {
      handleHttpError(error, reject);
    })
  });
}

const UserLoader = {
  loadUsers,
  changeUserPermissions,
  resetUserPassword
}

export default UserLoader;