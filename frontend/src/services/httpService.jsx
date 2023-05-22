import axios from "axios";
import AuthService from "./AuthService";

/* Setup default axios configuration */
axios.interceptors.request.use(
  (config) => {
    config.baseURL = "http://localhost:8081/rijksoverheid/api";
    return config;
});

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response.status === 401 && !error.config.url.startsWith('/auth')) {
      console.log(error.config.url);
      AuthService.logout();
    }
    return Promise.reject(error);
  }
);

axios.defaults.withCredentials = true;

const http = {
  get: axios.get,
  post: axios.post,
  put: axios.put,
  delete: axios.delete,
  patch: axios.patch,
};

export default http;