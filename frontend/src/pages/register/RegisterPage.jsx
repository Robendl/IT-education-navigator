import FormEntry from "components/forms/FormEntry/FormEntry";
import AuthService, { errorCodes } from "services/AuthService";
import { CircularProgress } from "@mui/material";

import '../login/LoginPage.css'
import { useState } from "react";
import { Link } from "react-router-dom";

export default function RegisterPage() {
    return (
        <div className="login">
            <RegisterForm />

        </div>
    );
}

function RegisterForm() {
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState(null);

    function handleSubmit(e) {
        e.preventDefault();
        setIsSubmitting(true);
        const formData = new FormData(e.target);
        const userInfo = Object.fromEntries(formData.entries());

        if (userInfo.password !== userInfo.confirmPassword) {
            setErrorMessage("Wachtwoorden komen niet overeen");
            setIsSubmitting(false);
            return;
        }
        AuthService.register(userInfo).then(() => {
            setTimeout(() => {
                setIsSubmitting(false);
                window.location.href = '/'
            }, 1500);
        }, (errorCode) => {
            switch (errorCode) {
                case errorCodes.ERR_USERNAME_DUPLICATE:
                    setErrorMessage("Emailadres is al in gebruik.");
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

    return (
        <form className="login-form" onSubmit={handleSubmit}>
            <h2>Opleidingsregister Registreren</h2>
            <FormEntry type="email" propertyName="Emailadres" propertyKey="username" required />
            <FormEntry type="password" propertyName="Wachtwoord" propertyKey="password" required />
            <FormEntry type="password" propertyName="Herhaal Wachtwoord" propertyKey="confirmPassword" required />
            <div>
                <div className="login-form-footer">
                    <button type="submit">
                        {(isSubmitting &&
                            <CircularProgress className="submit-loading" />) ||
                            <span>Registreer</span>
                        }
                    </button>
                    <Link to="/" className="register-link">Login</Link>
                </div>
                {errorMessage && <span className="login-error-message">{errorMessage}</span>}
            </div>

        </form>
    )
}