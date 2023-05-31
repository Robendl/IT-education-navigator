import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import './EditItemForm.css'
import { useState } from 'react';
import { useEffect } from 'react';
import ProvinceLoader, { errorCodes } from 'services/ProvinceLoader';

/* Form component for editing a course */
export default function EditItemForm({ onSubmit, onCancel, entry }) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [provinces, setProvinces] = useState(null);
  const regions = [{ id: "midden", name: "Midden" }, { id: "oost", name: "Oost" }, { id: "west", name: "West" }];
  const [selectedProvince, setSelectedProvince] = useState(null);
  const [selectedRegion, setSelectedRegion] = useState(null);

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
    courseObject["id"] = entry.id;
    CourseLoader.editCourse(courseObject).then(() => {
      setTimeout(() => {
        setIsSubmitting(false);
        onSubmit();
        window.location.reload();
      }, 1500);
    });
  }

  useEffect(populate, [entry]);

  /* Function for filling in the form with the course data */
  function populate() {
    ProvinceLoader.loadProvinces().then((response) => {
      setProvinces(response);
    }, (error) => {
      switch (error) {
        case errorCodes.ERR_CANCELED:
          break;
        default:
          console.log("Kon provincies niet ophalen van server.");
          break;
      }
    });
    Object.keys(entry).forEach(key => {
      let inputElement = document.querySelectorAll(`.edit-item-form input[name=${key}]`);
      inputElement = inputElement[inputElement.length - 1];
      let value = (entry[key] === null) ? "" : entry[key];
      if (key === "province") {
        setSelectedProvince(value.id.toString());
        return;
      }
      if (key === "region") {
        setSelectedRegion(value);
        return;
      }
      if (inputElement && inputElement.getAttribute("type") === "checkbox") {
        inputElement.checked = value;
        return;
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
        <FormEntry type="text" propertyName="Instelling" propertyKey="institution" required />
      </div>
      <div>
        <FormEntry type="text" propertyName="Locatie" propertyKey="location" required />
        {selectedProvince && provinces && <FormEntry type="dropdown" propertyName="Provincie" propertyKey="provinceId" options={provinces} defaultValue={selectedProvince} />}
        {selectedRegion && <FormEntry type="dropdown" propertyName="Regio" propertyKey="region" options={regions} defaultValue={selectedRegion} />}
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