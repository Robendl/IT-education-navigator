import './FormEntry.css'

/* General component for a form input that covers multiple input types */
export default function FormEntry({ type, propertyName, propertyKey, required }) {
  return (
    <label>
      <div className={`form-entry form-entry-${type}`}>
        <span className='form-entry-name'>{propertyName}</span>
        <div className="form-input">
          {
            (type === "checkbox" &&
              <>
                <input type="checkbox" name={propertyKey} value="true" required={required}/>
                <input type="hidden" name={propertyKey} value="false" required={required}/>
              </>
            ) ||
            (type === "password" &&
              <input type="password" name={propertyKey} required={required}/>
            ) ||
              (type === "email" &&
              <input type="email" pattern="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" name={propertyKey} required={required}/>
            ) ||
            <input type="text" name={propertyKey} required={required}/>
          }
          {
            (type === "dropdown" &&
              <div className="form-input-icon">
                <span className="material-symbols-outlined">arrow_drop_down</span>
              </div>
            ) ||
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