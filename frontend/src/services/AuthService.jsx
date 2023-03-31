import http from "./httpService";

// export const UserContext = createContext(null);

function login(username, password) {
  let credentials = {
    username: username,
    password: password
  }
  console.log(`Logging in with { username: ${username}, password: ${password}}`)
  return http
    .post('/login', credentials, {withCredentials: true})
      .then(response => {
        console.log(response);
    })
}

const AuthService = {
  login
};

export default AuthService;