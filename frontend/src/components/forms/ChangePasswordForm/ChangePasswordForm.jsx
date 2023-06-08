import { CircularProgress } from '@mui/material';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import './ChangePasswordForm.css'
import { useState } from 'react';
import authService, { errorCodes } from "services/AuthService";

/* Form component for adding new courses to the database */
export default function ChangePasswordForm({ onSubmit, onCancel }) {

  /* Boolean state that is true when the form is submitted and being processed */
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [success, setSuccess] = useState(false);

  /* Function that is called when the form is closed */
  function handleCancel(e) {
    e.preventDefault();
    onCancel();
  }

  /* Function that is called when the form is submitted */
  function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    setErrorMessage(null);
    setSuccess(false);
    const formData = new FormData(e.target);
    const userInfo = Object.fromEntries(formData.entries());

    if (userInfo.newPassword !== userInfo.confirmNewPassword) {
      setErrorMessage("Wachtwoorden komen niet overeen")
      setIsSubmitting(false);
      return;
    }

    authService.changePassword(userInfo).then(() => {
      setTimeout(() => {
        setIsSubmitting(false);
        setSuccess(true);
      }, 1500);
    }, (errorCode) => {
      switch (errorCode) {
        case errorCodes.ERR_LOGIN_INVALID:
          setErrorMessage("Wachtwoord is onjuist");
          setIsSubmitting(false);
          break;

        case errorCodes.ERR_NETWORK:
          setErrorMessage("Kon niet verbinden met de server");
          setIsSubmitting(false);
          break;

        default:
          setErrorMessage("Er is iets misgegaan, probeer aub nog een keer");
          setIsSubmitting(false);
          break;
      }
    });
  }

  /* Form Body */
  return (
    <form className="change-password-form" onSubmit={handleSubmit}>
      <h2>Wachtwoord veranderen</h2>
      <div>
        <FormEntry type="password" propertyName="Huidig wachtwoord" propertyKey="password" required />
        <FormEntry type="password" propertyName="Nieuw wachtwoord" propertyKey="newPassword" required />
        <FormEntry type="password" propertyName="Herhaal nieuw wachtwoord" propertyKey="confirmNewPassword" required />
      </div>
      <div className="form-footer">
        {errorMessage && <span className="error-message">{errorMessage}</span>}
        {success && <span className="success-message">Wachtwoord veranderd</span>}
        <div className="form-actions">
          <button className="save-button" type="submit">
            {(isSubmitting && <CircularProgress className="submit-loading" />) || <span>Verander</span>}
          </button>
          <button className="cancel-button" onClick={handleCancel}>{success ? "Sluiten" : "Annuleren"}</button>
        </div>
      </div>
    </form>
  );
}