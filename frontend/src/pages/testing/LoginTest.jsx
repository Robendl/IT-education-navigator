import { useRef } from 'react';
import AuthService from 'services/AuthService';

export default function LoginTest() {

  const nameInput = useRef(null);
  const passInput = useRef(null);

  function handleLogin() {
    AuthService.login(nameInput.current.value, passInput.current.value);
  }

  return (
    <div className="container" style={{display: "flex", justifyContent: "center", paddingTop: "20px", fontSize: "1.5rem"}}>
      <div className="login" style={{display: "flex", flexDirection: "column"}}>
        <div className="login-form" style={{display: "flex", flexDirection: "column", gap: "5px"}}>
          <label htmlFor="user">Gebruikersnaam</label>
          <input type="text" name="username" id="user" ref={nameInput} />
          <label htmlFor="pass">Wachtwoord</label>
          <input type="password" name="password" id="pass" ref={passInput}/>
          <button onClick={handleLogin}>Login</button>
        </div>
      </div>
    </div>
  )
}