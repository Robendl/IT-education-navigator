import FormEntry from "components/forms/FormEntry/FormEntry";
import AuthService from "services/AuthService";

export default function LoginPage() {
  return (
    <div className="login">
      <RegisterForm />
      
    </div>
  );
}

function RegisterForm() {

  function handleSubmit(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const userInfo = Object.fromEntries(formData.entries());
    AuthService.register(userInfo);
  }

  return (
    <form className="login-form" onSubmit={handleSubmit}>
        <h2>Maak nieuwe gebruiker (DEBUG)</h2>
        <FormEntry propertyName="Gebruikersnaam" propertyKey="username" required/>
        <FormEntry propertyName="Wachtwoord" propertyKey="password" required/>
        <FormEntry propertyName="Rol" propertyKey="role" required/>
        <button type="submit">Register</button>
    </form>
  )
}