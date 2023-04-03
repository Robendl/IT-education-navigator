import { useContext, useState } from "react";
import AuthService, { UserContext } from "services/AuthService";

export default function MainHeader() {
  const user = useContext(UserContext);
  console.log(user);

  return (
    <div className="main-header ignore-overlay">
      <div className="page-wide">
        <h2>Opleidingsregister</h2>
        {user.loggedIn && <UserOption user={user} />}
      </div>
    </div>
  );
}

function UserOption({ user }) {
  const [toolTipOpen, setToolTipOpen] = useState(false);

  function handleHover() {
    setToolTipOpen(true);
  }

  function handleLeave() {
    setToolTipOpen(false);
  }

  return (
    <div className="user-option" onMouseOver={handleHover} onMouseLeave={handleLeave}>
      <span>Ingelogd als {user.name}</span>
      <span className="material-symbols-outlined tool-icon">person</span>
      {toolTipOpen && <UserToolTip onClose={() => setToolTipOpen(false)} />}
    </div>
  );
}

function UserToolTip({ onClose }) {

  return(
    <div className="user-tooltip">
      <div className="user-tooltip-option">
        <span onClick={() => {AuthService.logout()}}>Logout</span>
      </div>
    </div>
  )
}