import { CircularProgress } from '@mui/material';
import UserLoader from 'services/UserLoader';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import { useState } from 'react';
import { useEffect } from 'react';

export default function ChangePermissionsForm({ onSubmit, onCancel, entry }) {
  const [isSubmitting, setIsSubmitting] = useState(false);

  function handleCancel(e) {
    e.preventDefault();
    console.log(entry);
    onCancel();
  }

  function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    const formData = new FormData(e.target);
    const userObject = Object.fromEntries(formData.entries);
    userObject["id"] = entry.id;
    UserLoader.changeUserPermissions(userObject).then(() => {
        setTimeout(() => {
            setIsSubmitting(false);
            onSubmit();
            Window.location.reload()
        }, 1500);
    });
  }

  useEffect(() => {
    populate();
  });

  function populate() {
    Object.keys(entry).forEach(key => {
      let inputElement = document.querySelector(`.edit-item-form input[name=${key}]`);
      let value = entry[key];
      if (key === "province") {
        value = value.id;
      }
      if (inputElement && inputElement.getAttribute("type") === "checkbox") {
        inputElement?.setAttribute("checked", value);
      }
      inputElement?.setAttribute("value", value);
    });
  }

  return (
    <form className="change-permissions-form" onSubmit={handleSubmit}>
      <h2>Rechten Bewerken</h2>
      <div>
        <FormEntry type="dropdown" propertyName="Role" propertyKey="role" required />
      </div>
      <div className="form-actions">
        <button className="save-button" type="submit">
          {(isSubmitting && <CircularProgress className="submit-loading" />) || <span>Opslaan</span>}
        </button>
        <button className="cancel-button" onClick={handleCancel}>Annuleren</button>
      </div>
    </form>
  );
}