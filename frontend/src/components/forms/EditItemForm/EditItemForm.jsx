import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import './EditItemForm.css'
import { useState } from 'react';
import { useEffect } from 'react';

/* Form component for editing a course */
export default function EditItemForm({ onSubmit, onCancel, entry }) {
  const [isSubmitting, setIsSubmitting] = useState(false);

  /* Function that is called when the form is closed */
  function handleCancel(e) {
    e.preventDefault();
    console.log(entry);
    onCancel();
  }

  /* Function that is called when the form is submitted */
  function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    const formData = new FormData(e.target);
    const courseObject = Object.fromEntries(formData.entries());
    courseObject["id"] = entry.id;
    CourseLoader.editCourse(courseObject).then(() => {
      setTimeout(() => {
        setIsSubmitting(false);
        onSubmit();
        window.location.reload();
      }, 1500);
    });
  }

  useEffect(() => {
    populate();
  });

  /* Function for filling in the form with the course data */
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

  /* Form body */
  return (
    <form className="edit-item-form" onSubmit={handleSubmit}>
      <h2>Item bewerken</h2>
      <div>
        <FormEntry type="text" propertyName="Naam" propertyKey="name" required />
        <FormEntry type="browse" propertyName="Instelling" propertyKey="institution" required />
      </div>
      <div>
        <FormEntry type="text" propertyName="Locatie" propertyKey="location" required />
        <FormEntry type="dropdown" propertyName="Provincie" propertyKey="province" />
        <FormEntry type="dropdown" propertyName="Regio" propertyKey="region" />
      </div>
      <div>
        <FormEntry type="browse" propertyName="Niveau" propertyKey="level" />
        <FormEntry type="browse" propertyName="Type" propertyKey="courseType" />
        <FormEntry type="browse" propertyName="Tijd" propertyKey="timeOccupation" />
        <FormEntry type="checkbox" propertyName="Informatiehuishouding Gerelateerd" propertyKey="housekeepingRelated" />
        <FormEntry type="checkbox" propertyName="Samenwerking" propertyKey="collaboration" />
      </div>
      <div>
        <FormEntry type="text" propertyName="Verantw. Taskforce" propertyKey="responsibleTaskForce" />
        <FormEntry type="text" propertyName="Lector / Hoogleraar" propertyKey="professor" />
        <FormEntry type="text" propertyName="Contact" propertyKey="contact" />
        <FormEntry type="text" propertyName="Web" propertyKey="web" />
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