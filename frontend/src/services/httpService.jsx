import axios from "axios";
import AuthService from "./AuthService";

axios.interceptors.request.use(
  (config) => {
    config.baseURL = "http://localhost:8081/rijksoverheid/api";
    let user = AuthService.getUser();
    if (user && user.token) {
      config.headers = {
        'Authorization': `Bearer ${user.token}`
      }
    }
    return config;
});

const http = {
  get: axios.get,
  post: axios.post,
  put: axios.put,
  delete: axios.delete,
  patch: axios.patch,
};

export default http;