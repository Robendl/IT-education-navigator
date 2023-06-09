import axios from "axios";
import AuthService from "./AuthService";

let xsrfToken = null;

function getXsrfToken() {
  if (xsrfToken) {
    return xsrfToken;
  }
  const cookies = document.cookie.split(';')
  for (let value of cookies) {
    const cookie = value.trim();
    if (cookie.startsWith('XSRF-TOKEN=')) {
      xsrfToken = cookie.substring('XSRF-TOKEN='.length, cookie.length);
      return xsrfToken;
    }
  }
  return null;
}

/* Setup default axios configuration */
axios.interceptors.request.use(
  (config) => {
    config.baseURL = "http://localhost:8081/rijksoverheid/api";
    let xsrfToken = getXsrfToken();
    if (xsrfToken) {
      config.headers['X-XSRF-TOKEN'] = xsrfToken;
    }
    return config;
  });

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401 && !error.config.url.startsWith('/auth')) {
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