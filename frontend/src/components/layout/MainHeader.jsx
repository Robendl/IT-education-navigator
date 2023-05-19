import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService, { UserContext } from "services/AuthService";

/* Header Component present on all user pages */
export default function MainHeader() {
  const user = useContext(UserContext);

  /* MainHeader Body */
  return (
    <div className="main-header ignore-overlay">
      <div className="page-wide">
        <h2>Opleidingsregister</h2>
        {user.loggedIn && <UserOption user={user} />}
      </div>
    </div>
  );
}

/* User information component showing the logged-in user */
function UserOption({ user }) {
  const [toolTipOpen, setToolTipOpen] = useState(false);

  /* Function that is called when the user starts hovering the component */
  function handleHover() {
    setToolTipOpen(true);
  }

  /* Function that is called when the user stops hovering the component */
  function handleLeave() {
    setToolTipOpen(false);
  }

  /* UserOption body */
  return (
    <div className="user-option" onMouseOver={handleHover} onMouseLeave={handleLeave}>
      <span>Ingelogd als {user.name}</span>
      <span className="material-symbols-outlined tool-icon">person</span>
      {toolTipOpen && <UserToolTip onClose={() => setToolTipOpen(false)} />}
    </div>
  );
}

/* Tooltip component that allows for interaction with user related options */
function UserToolTip({ onClose }) {

  const navigate = useNavigate();

  function handleLogout() {
    navigate("/");
    AuthService.logout();
  }

  return (
    <div className="user-tooltip">
      <div className="user-tooltip-option">
        <span onClick={handleLogout}>Logout</span>
      </div>
    </div>
  )
}