import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import './AddItemForm.css'
import { useState } from 'react';

export default function AddItemForm({ onSubmit, onCancel }) {
  const [isSubmitting, setIsSubmitting] = useState(false);

  function handleCancel(e) {
    e.preventDefault();
    onCancel();
  }

  function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    const formData = new FormData(e.target);
    const courseObject = Object.fromEntries(formData.entries());
    CourseLoader.addCourse(courseObject).then(() => {
      setTimeout(() => {
        setIsSubmitting(false);
        onSubmit();
        window.location.reload();
      }, 1500);
    });
  }

  return (
    <form className="add-item-form" onSubmit={handleSubmit}>
      <h2>Item toevoegen</h2>
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