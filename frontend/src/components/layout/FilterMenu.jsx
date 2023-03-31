import { useRef } from "react";


export default function FilterMenu() {
  const filters = {
    "provincie": {
      "name": "Provincie",
      "options": {
        "limburg": {
          "name": "Limburg",
          "selected": false
        },
        "drenthe": {
          "name": "Drenthe",
          "selected": false
        },
        "flevoland": {
          "name": "Flevoland",
          "selected": false
        }
      }
    },
    "niveau": {
      "name": "Niveau",
      "options": {
        "hbo": {
          "name": "HBO",
          "selected": false
        },
        "wo": {
          "name": "WO",
          "selected": false
        }
      }
    },
    "type": {
      "name": "Type",
      "options": {
        "bachelor": {
          "name": "Bachelor",
          "selected": false
        },
        "associateDegree": {
          "name": "Associate Degree (AD)",
          "selected": false
        },
        "kenniscentrum": {
          "name": "Kenniscentrum",
          "selected": false
        }
      }
    }
  }

  return (
    <div className="filter-menu">
      {Object.keys(filters).map(key => <Filter name={filters[key].name} filterKey={key} key={key} options={filters[key].options} />)}
    </div>
  );
}

function Filter({name, optionKey, options}) {
  return (
    <div className="filter">
      <h2>{name}</h2>
      <div className="filter-option-list">
        {
          Object.keys(options).map(opt => <FilterOption name={options[opt].name} key={opt} selected={options[opt].selected} />)
        }
      </div>
    </div>
  );
}

function FilterOption ({name, selected = false}) {

  const ref = useRef(null);

  function onChange(e) {
    switch (e.target.checked) {
      case true:
        ref.current.classList.add("checked");
        break;
      default:
        ref.current.classList.remove("checked");
        break;
    }
  }

  return (
    <div className={`filter-option ${selected ? "checked" : ""}`} ref={ref}>
      <input type="checkbox" defaultChecked={selected} onChange={onChange} />
      <span>{name}</span>
    </div>
  );
}