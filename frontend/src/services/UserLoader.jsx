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

/* Function that rejects a http error with a corresponding error code */
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

/* Function for loading users
 * Returns a promise that, once resolved, returns an object with all users */
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

/* Function for resetting the password for a user
 * Accepts a userId.
 * Returns a promise that, once resolved, returns an object with the newly generated password */
function resetUserPassword(userId) {
  return new Promise((resolve, reject) => {
    http.put(`/user/password/${userId}/reset`).then((response) => {
      resolve(response.data);
    }, (error) => {
      handleHttpError(error, reject);
    })
  });
}

/* Function for deleting a user
 * Accepts an object with user properties (must include an "id" property)
 * Returns a promise that, once resolved, returns the http response body */
function deleteUser(user) {
  return new Promise((resolve, reject) => {
    http.delete(`/user/${user.id}`, user, {
      headers: {
        'Content-type': 'application/json'
      }
    }).then((response) => {
      resolve(response);
    }, (error) => {
      reject("Kon account niet verwijderen.")
    });
  });
}

const UserLoader = {
  loadUsers,
  changeUserPermissions,
  resetUserPassword,
  deleteUser
}

export default UserLoader;