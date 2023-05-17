import FormEntry from "components/forms/FormEntry/FormEntry";
import AuthService, { errorCodes } from "services/AuthService";
import { CircularProgress } from "@mui/material";
import './LoginPage.css'
import { useState } from "react";
import {Link} from "react-router-dom";

/* Main Login page component */
export default function LoginPage() {
  return (
    <div className="login">
      <LoginForm />
      
    </div>
  );
}

/* Form component for loggin in */
function LoginForm() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  /* Function that is called when the form is submitted */
  function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    const formData = new FormData(e.target);
    const credentials = Object.fromEntries(formData.entries());
    AuthService.login(credentials).then(() => {
      setTimeout(() => {
        setIsSubmitting(false);
        window.location.reload();
      }, 1500);
    }, (errorCode) => {
      switch (errorCode) {
        case errorCodes.ERR_LOGIN_INVALID:
          setErrorMessage("Emailadres / wachtwoord is onjuist");
          setIsSubmitting(false);
          break;
        
        case errorCodes.ERR_NETWORK:
          setErrorMessage("Server kan niet worden bereikt");
          setIsSubmitting(false);
          break;
        
        default:
          setErrorMessage("Er is iets misgegaan, probeer aub nog een keer");
          setIsSubmitting(false);
          break;
      }
    });
  }

  /* LoginForm body */
  return (
    <form className="login-form" onSubmit={handleSubmit}>
        <h2>Opleidingsregister Login</h2>
        <FormEntry type="email" propertyName="Emailadres" propertyKey="username" required/>
        <FormEntry type="password" propertyName="Wachtwoord" propertyKey="password" required/>
        <div>
          <div className="login-form-footer">
            <button type="submit">
              {(isSubmitting &&
                <CircularProgress className="submit-loading"/>) ||
                <span>Login</span>
              }
            </button>
            <Link to="/register" className="register-link">Registreer</Link>
          </div>
          {errorMessage && <span className="login-error-message">{errorMessage}</span>}
        </div>
    </form>
  )
}