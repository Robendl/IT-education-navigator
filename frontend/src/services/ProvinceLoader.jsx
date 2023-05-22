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

/* Function for loading provinces
 * Returns a promise that, once resolved, returns an object with all provinces */
function loadProvinces() {
  loadController.abort();
  loadController = new AbortController();
  return new Promise((resolve, reject) => {
    http.get(`/provinces`, { signal: loadController.signal }).then((response) => {
      resolve(response.data);
    }, (error) => {
      handleHttpError(error, reject);
    })
  });
}

const ProvinceLoader = {
  loadProvinces
}

export default ProvinceLoader;