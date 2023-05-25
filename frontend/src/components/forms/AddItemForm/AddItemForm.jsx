import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import './AddItemForm.css'
import { useEffect, useState } from 'react';
import ProvinceLoader from 'services/ProvinceLoader';

/* Form component for adding new courses to the database */
export default function AddItemForm({ onSubmit, onCancel }) {
  const [provinces, setProvinces] = useState(null);
  const regions = [{ id: "midden", name: "Midden" }, { id: "oost", name: "Oost" }, { id: "west", name: "West" }];

  /* Boolean state that is true when the form is submitted and being processed */
  const [isSubmitting, setIsSubmitting] = useState(false);

  /* Function that is called when the form is closed */
  function handleCancel(e) {
    e.preventDefault();
    onCancel();
  }

  /* Function that is called when the form is submitted */
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

  useEffect(() => {
    ProvinceLoader.loadProvinces().then((response) => {
      setProvinces(response);
    });
  }, []);

  /* Form Body */
  return (
    <form className="add-item-form" onSubmit={handleSubmit}>
      <h2>Item toevoegen</h2>
      <div>
        <FormEntry type="text" propertyName="Naam" propertyKey="name" required />
        <FormEntry type="text" propertyName="Instelling" propertyKey="institution" required />
      </div>
      <div>
        <FormEntry type="text" propertyName="Locatie" propertyKey="location" required />
        <FormEntry type="dropdown" propertyName="Provincie" propertyKey="provinceId" options={provinces} />
        <FormEntry type="dropdown" propertyName="Regio" propertyKey="region" options={regions} />
      </div>
      <div>
        <FormEntry type="text" propertyName="Niveau" propertyKey="level" />
        <FormEntry type="text" propertyName="Type" propertyKey="courseType" />
        <FormEntry type="text" propertyName="Tijd" propertyKey="timeOccupation" />
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