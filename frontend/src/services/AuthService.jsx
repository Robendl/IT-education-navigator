import http from "./httpService";

// export const UserContext = createContext(null);

function login(username, password) {
    const data = new URLSearchParams();
    data.append('username', username);
    data.append('password', password);

    return http.post('/login', data.toString(), {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
        .then(response => {
            console.log(response);
        });
}


const AuthService = {
  login
};

export default AuthService;