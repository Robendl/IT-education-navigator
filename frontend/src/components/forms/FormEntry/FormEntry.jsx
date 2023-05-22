import { useEffect, useRef } from 'react';
import './FormEntry.css'

/* General component for a form input that covers multiple input types */
export default function FormEntry({ type, propertyName, propertyKey, required, options, defaultValue }) {
  const selectElement = useRef();

  useEffect(() => {
    if (selectElement.current) {
      selectElement.current.value = defaultValue;
    }

  }, [selectElement, defaultValue]);
  return (
    <label>
      <div className={`form-entry form-entry-${type}`}>
        <span className='form-entry-name'>{propertyName}</span>
        <div className="form-input">
          {
            (type === "checkbox" &&
              <>
                <input type="hidden" name={propertyKey} value="false" required={required} />
                <input type="checkbox" name={propertyKey} value="true" required={required} />
              </>
            ) ||
            (type === "password" &&
              <input type="password" name={propertyKey} required={required} />
            ) ||
            (type === "email" &&
              <input type="email" pattern="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" name={propertyKey} required={required} />
            ) ||
            (type === "dropdown" &&
              <select name={propertyKey} required={required} ref={selectElement}>
                {options?.map(opt => <option value={opt["id"]} key={opt["id"]}>{opt["name"]}</option>)}
              </select>
            ) ||
            <input type="text" name={propertyKey} required={required} />
          }
          {
            (type === "browse" &&
              <div className="form-input-icon">
                <span className="material-symbols-outlined">list</span>
              </div>
            )
          }

        </div>
      </div>
    </label>
  );
}