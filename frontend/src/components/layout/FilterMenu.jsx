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
    /* Load available filter properties on page load */
    PropertyLoader.loadProperties().then(results => {
      setFilters(results);
    });
  })

  /* FilterMenu body */
  return (
    <div className="filter-menu">
      {/* Create a filter component for every filter */}
      {Object.keys(filters).map(key => <Filter name={propertyTranslations[key]} filterKey={key} key={key} options={filters[key].options} multivalue={filters[key].multivalue ?? false} />)}
    </div>
  );
}

/* Filter component holding one type of filter with multiple options */
function Filter({ name, filterKey, options, multivalue }) {
  const [expanded, setExpanded] = useState(false);
  const [itemCount, setItemCount] = useState(3);
  const [unselectedOptions, setUnselectedOptions] = useState([]);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const minItemCount = 3;
  const [searchParams, setSearchParams] = useSearchParams();

  /* Function that is called when the options list of the filter must be expanded */
  function expandOptions() {
    setItemCount(Object.keys(options).length);
    setExpanded(true);
  }

  /* Function that is called when the options list of the filter must be collapsed */
  function collapseOptions() {
    setItemCount(minItemCount);
    setExpanded(false);
  }

  /* Function for setting the filter to a value {opt}.
   * This value must be a value key that is recognized by the courses API (example for courseType filter: opt = 'associateDegree')
   * The filter will be set to only one value. For multiple values use 'addOption'.
   */
  function setOption(opt) {
    setSearchParams(prevParams => {
      prevParams.set(filterKey, opt);
      return prevParams;
    })
  }

  /* Function for adding a value to a filter {opt}.
   * This value must be a value key that is recognized by the courses API (example for courseType filter: opt = 'associateDegree')
   */
  function addOption(opt) {
    setSearchParams(prevParams => {
      if (prevParams.has(filterKey)) {
        let currentValues = prevParams.get(filterKey).split(',');
        currentValues.push(opt);
        prevParams.set(filterKey, currentValues.join(','));
      } else {
        prevParams.set(filterKey, opt);
      }
      return prevParams;
    })
  }

  /* Function for clearing the filter
   * This function will remove the filter from the current search parameters.
   * For removing only one specific value from the filter, use 'removeOption'.
   */
  function clearFilter() {
    setSearchParams(prevParams => {
      prevParams.delete(filterKey);
      return prevParams;
    })
  }

  /* Function for removing a value {opt} from the filter
   * Should multivalue filters be introduced, this function can be rewritten so that it clears just one option {opt} 
   */
  function removeOption(opt) {
    setSearchParams(prevParams => {
      let currentValues = prevParams.get(filterKey).split(',');
      currentValues.splice(currentValues.indexOf(opt), 1);
      if (currentValues.length === 0) {
        prevParams.delete(filterKey);
        return prevParams;
      }
      prevParams.set(filterKey, currentValues.join(','));
      return prevParams;
    })
  }

  const handleSelect = multivalue ? addOption : setOption;
  const handleDeselect = multivalue ? removeOption : clearFilter;

  useEffect(() => {
    /* Update the selected options based on the url search parameters */
    let currentValues = searchParams.get(filterKey)?.split(',') ?? [];
    let selected = [];
    let unselected = [];
    for (const key of Object.keys(options)) {
      if (currentValues.includes(key)) {
        selected.push(key);
      } else {
        unselected.push(key);
      }
    }
    setSelectedOptions(selected);
    /* Update the remaining unselected options and limit the number of options based on expand status */
    setUnselectedOptions(
      unselected.slice(0, itemCount - (searchParams.has(filterKey) ? 1 : 0))
    );
  }, [options, itemCount, filterKey, searchParams])

  /* Filter body */
  return (
    <div className="filter">
      <div className="filter-header">
        <h2>{name}</h2>
        {(selectedOptions.length > 0) && <button className="filter-reset-button" onClick={clearFilter}>Reset</button>}
      </div>
      <div className={`filter-option-list ${expanded ? "expand" : ""}`}>
        {
          selectedOptions.map(opt => <FilterOption name={options[opt].name} key={opt} selected onSelect={() => handleSelect(opt)} onDeselect={() => handleDeselect(opt)} />)
        }
        {
          unselectedOptions.map(opt => <FilterOption name={options[opt].name} key={opt} onSelect={() => handleSelect(opt)} onDeselect={() => handleDeselect(opt)} />)
        }
        {Object.keys(options).length > 3 &&
          <div className="filter-expand">
            {(expanded &&
              <button className="filter-expand-button" onClick={collapseOptions}><span>Toon minder</span> <ExpandLessIcon /></button>) ||
              <button className="filter-expand-button" onClick={expandOptions}><span>Toon meer</span> <ExpandMoreIcon /></button>}
          </div>
        }
      </div>
    </div>
  );
}

/* FilterOption component that can be checked or unchecked */
function FilterOption({ name, selected = false, onSelect, onDeselect }) {

  const ref = useRef(null);

  /* Function that is called when the user interacts with the option */
  function onChange(e) {
    switch (e.target.checked) {
      case true:
        ref.current.classList.add("checked");
        onSelect();
        break;
      default:
        ref.current.classList.remove("checked");
        onDeselect();
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