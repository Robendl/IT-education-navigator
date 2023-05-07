import { CircularProgress } from '@mui/material';
import FormEntry from 'components/forms/FormEntry/FormEntry';
import './ChangePasswordForm.css'
import { useState } from 'react';
import authService, {errorCodes} from "../../../services/AuthService";

/* Form component for adding new courses to the database */
export default function ChangePasswordForm({ onSubmit, onCancel }) {

  /* Boolean state that is true when the form is submitted and being processed */
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

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
    const userInfo = Object.fromEntries(formData.entries());

    if (userInfo.newPassword !== userInfo.confirmNewPassword) {
      setErrorMessage("Wachtwoorden zijn niet gelijk")
      setIsSubmitting(false);
      return;
    }

    authService.changePassword(userInfo).then(() => {
      setTimeout(() => {
        setIsSubmitting(false);
        onSubmit();
        window.location.reload();
      }, 1500);
    }, (errorCode) => {
        switch (errorCode) {
            case errorCodes.ERR_LOGIN_INVALID:
                setErrorMessage("Wachtwoord is onjuist");
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
      <h2 className="change-password-form">Wachtwoord veranderen</h2>
      <div>
          <FormEntry type="text" propertyName="Huidig wacthtwoord" propertyKey="password" required />
          <FormEntry type="text" propertyName="Nieuw wachtwoord" propertyKey="newPassword" required />
          <FormEntry type="text" propertyName="Herhaal nieuw wachtwoord" propertyKey="confirmNewPassword" required />
      </div>
      <div className="form-actions">
        <button className="save-button" type="submit">
          {(isSubmitting && <CircularProgress className="submit-loading" />) || <span>Opslaan</span>}
        </button>
        <button className="cancel-button" onClick={handleCancel}>Annuleren</button>
      </div>
      {errorMessage && <span className="login-error-message">Wachtwoorden zijn niet gelijk</span>}
    </form>
  );
}