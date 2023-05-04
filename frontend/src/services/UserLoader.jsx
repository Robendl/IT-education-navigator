import http from "./httpService";

const UserLoader = {
    loadUsers: () => {
        return new Promise((resolve, reject) => {
            http.get(`/user`).then((response) => {
                resolve(response.data);
              }, (error) => {
                console.log(error);
                reject("Kon gebruikers niet ophalen.");
              });
        });
    },

    changeUserPermissions: (user) => {
      return new Promise((resolve, reject) => {
      http.put(`/user/perm${user.id}`, user, {
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
}

export default UserLoader;