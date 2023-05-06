/* Dummy object for API response */
const filters = {
  "provinceId": {
    "options": {
      0: {
        "name": "Noord-Brabant"
      },
      1: {
        "name": "Utrecht"
      },
      2: {
        "name": "Drenthe"
      },
      3: {
        "name": "Flevoland"
      },
      4: {
        "name": "Friesland"
      },
      5: {
        "name": "Gelderland"
      },
      6: {
        "name": "Groningen"
      },
      7: {
        "name": "Heerlen"
      },
      8: {
        "name": "Limburg"
      },
      9: {
        "name": "Noord-Holland"
      },
      10: {
        "name": "Overijssel"
      },
      11: {
        "name": "Zuid-Holland"
      },
      12: {
        "name": "Other"
      },
    }
  },
  "level": {
    "options": {
      "hbo": {
        "name": "HBO"
      },
      "wo": {
        "name": "WO"
      },
      // "other": {
      //   "name": "Anders"
      // }
    }
  },
  "courseType": {
    "options": {
      "bachelor": {
        "name": "Bachelor"
      },
      "associateDegree": {
        "name": "Associate Degree (AD)"
      },
      "kenniscentrum": {
        "name": "Kenniscentrum"
      },
      "master": {
        "name": "Master"
      }
    }
  }
};

/* Placeholder function for retrieving all properties with their possible values */
function loadProperties() {
  return new Promise((resolve, reject) => {
    resolve(filters);
  });
};

const PropertyLoader = {
  loadProperties
};

export default PropertyLoader;