import { userRoles } from "./AuthService";
import http from "./httpService";


function loadUsers() {
  return new Promise((resolve, reject) => {
    http.get(`/user`).then((response) => {
      resolve(response.data);
    }, (error) => {
      console.log(error);
      reject("Kon gebruikers niet ophalen.");
    });
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
      console.log(error);
      reject("Kon rechten niet aanpassen");
    })
  })
}

const UserLoader = {
  loadUsers,
  changeUserPermissions
}

export default UserLoader;