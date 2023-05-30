import { useContext, useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import AuthService, { UserContext } from "services/AuthService";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import ChangePasswordPopup from "../popups/ChangePasswordPopup/ChangePasswordPopup";
import PageOverlay from "./PageOverlay/PageOverlay";

/* Header Component present on all user pages */
export default function MainHeader() {
  const user = useContext(UserContext);

  /* MainHeader Body */
  return (
    <div className="main-header ignore-overlay">
      <div className="page-wide">
      <h2><Link to="/" className="header-title">Opleidingsregister</Link></h2>
        {user.loggedIn && <UserOption user={user} />}
      </div>
    </div>
  );
}

/* User information component showing the logged-in user */
function UserOption({ user }) {
  const [toolTipOpen, setToolTipOpen] = useState(false);
  const [lock, setLock] = useState(false);

  /* Function that is called when the user starts hovering the component */
  function handleHover() {
    setToolTipOpen(true);
  }

  /* Function that is called when the user stops hovering the component */
  function handleLeave() {
    if (!lock) {
      setToolTipOpen(false);
    }
  }

  function handleClose() {
    setToolTipOpen(false);
    setLock(false);
  }

  /* UserOption body */
  return (
    <div className="user-option" onMouseOver={handleHover} onMouseLeave={handleLeave}>
      <span>Ingelogd als {user.name}</span>
      <span className="material-symbols-outlined tool-icon">person</span>
      {toolTipOpen && <UserToolTip onClose={handleClose} setLock={setLock} />}
    </div>
  );
}

/* Tooltip component that allows for interaction with user related options */
function UserToolTip({ onClose, setLock }) {
  const [changingPassword, setChangingPassword] = useState(false);
  const navigate = useNavigate();

  function handleLogout() {
    navigate("/");
    AuthService.logout();
  }

  function handleChangePassword() {
    setChangingPassword(true);
    setLock(true);
  }

  return (
    <div className="user-tooltip">
      <div className="user-tooltip-option">
        <span onClick={handleLogout}>Logout</span>
      </div>
      <div>
        <span onClick={handleChangePassword}>Verander wachtwoord</span>
      </div>
      <OverlayContext.Provider value={{
        closeChangePassword: () => { setChangingPassword(false); onClose(); }
      }} >
        <PageOverlay isOpen={changingPassword}>
          <ChangePasswordPopup />
        </PageOverlay>
      </OverlayContext.Provider>
    </div>
  )
}