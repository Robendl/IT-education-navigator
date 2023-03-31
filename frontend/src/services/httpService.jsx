import axios from "axios";

axios.interceptors.request.use(
  (config) => {
    config.baseURL = "http://localhost:8081/rijksoverheid/api";
    return config;
});

const http = {
  get: axios.get,
  post: axios.post,
  put: axios.put,
  delete: axios.delete,
  patch: axios.patch
};

export default http;