import { propertyTranslations } from "config/translations";
import { useEffect, useRef, useState } from "react";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import PropertyLoader from "services/PropertyLoader";
import { useSearchParams } from "react-router-dom";

/* Menu component holding the filters for the result list */
export default function FilterMenu() {
  const [filters, setFilters] = useState({});
  
  useEffect(() => {
    PropertyLoader.loadProperties().then(results => {
      setFilters(results);
    });
  })

  /* FilterMenu body */
  return (
    <div className="filter-menu">
      {Object.keys(filters).map(key => <Filter name={propertyTranslations[key]} filterKey={key} key={key} options={filters[key].options} />)}
    </div>
  );
}

/* Filter component holding one type of filter with multiple options */
function Filter({name, filterKey, options}) {
  const [expanded, setExpanded] = useState(false);
  const [itemCount, setItemCount] = useState(3);
  const [unselectedOptions, setUnselectedOptions] = useState([]);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const minItemCount = 3;
  const [searchParams, setSearchParams] = useSearchParams();

  function expandOptions() {
    setItemCount(Object.keys(options).length);
    setExpanded(true);
  }

  function collapseOptions() {
    setItemCount(minItemCount);
    setExpanded(false);
  }

  function setOption(opt) {
    setSearchParams(prevParams => {
      prevParams.set(filterKey, opt);
      return prevParams;
    })
  }

  function clearOption() {
    setSearchParams(prevParams => {
      prevParams.delete(filterKey);
      return prevParams;
    })
  }

  useEffect(() => {
    setSelectedOptions(
      Object.keys(options)
        .filter(opt => searchParams.get(filterKey) === opt)
    );
    setUnselectedOptions(
      Object.keys(options)
        .filter(opt => searchParams.get(filterKey) !== opt)
        .slice(0, itemCount - (searchParams.has(filterKey) ? 1 : 0))
    );
  }, [options, itemCount, filterKey, searchParams])

  return (
    <div className="filter">
      <h2>{name}</h2>
      <div className={`filter-option-list ${expanded ? "expand" : ""}`}>
        {
          selectedOptions.map(opt => <FilterOption name={options[opt].name} key={opt} selected setOption={() => setOption(opt)} clearOption={clearOption} />)
        }
        {
          unselectedOptions.filter(opt => searchParams.get(filterKey) !== opt).map(opt => <FilterOption name={options[opt].name} key={opt} setOption={() => setOption(opt)} clearOption={clearOption} />)
        }
        {Object.keys(options).length > 3 && 
          <div className="filter-expand">
            {(expanded && 
              <button className="filter-expand-button" onClick={collapseOptions}><span>Toon minder</span> <ExpandLessIcon /></button> ) || 
              <button className="filter-expand-button" onClick={expandOptions}><span>Toon meer</span> <ExpandMoreIcon /></button>}
          </div>
        }
      </div>
    </div>
  );
}

/* FilterOption component that can be checked or unchecked */
function FilterOption ({ name, selected = false, setOption, clearOption }) {

  const ref = useRef(null);

  /* Function that is called when the user interacts with the option */
  function onChange(e) {
    switch (e.target.checked) {
      case true:
        ref.current.classList.add("checked");
        setOption()
        break;
      default:
        ref.current.classList.remove("checked");
        clearOption();
        break;
    }
  }

  /* FilterOption body */
  return (
    <div className={`filter-option ${selected ? "checked" : ""}`} ref={ref}>
      <input type="checkbox" defaultChecked={selected} onChange={onChange} />
      <span>{name}</span>
    </div>
  );
}