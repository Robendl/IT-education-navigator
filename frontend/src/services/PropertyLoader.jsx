/* Dummy object for API response that could be implemented when properties need to be dynamic.
 * (Dynamic: all filters with their available values are loaded from the backend).
 */
const filters = {
  "province-ids": {
    "multivalue": true,
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
        "name": "Overigen"
      },
    }
  },
  "levels": {
    "multivalue": false,
    "options": {
      "mbo": {
        "name": "MBO"
      },
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
  "course-types": {
    "multivalue": true,
    "options": {
      "associate degree": {
        "name": "Associate Degree"
      },
      "bachelor": {
        "name": "Bachelor"
      },
      "kenniscentrum": {
        "name": "Kenniscentrum"
      },
      "lectoraat": {
        "name": "Lectoraat"
      },
      "master": {
        "name": "Master"
      }
    }
  },
  "regions": {
    "multivalue": true,
    "options": {
      "midden": {
        "name": "Midden"
      },
      "oost": {
        "name": "Oost"
      },
      "west": {
        "name": "West"
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