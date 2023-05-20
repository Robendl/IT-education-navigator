import { Outlet } from 'react-router-dom';

import "./AdminPanel.css";
import PageOverlay, { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import { useContext, useState } from 'react';
import PasswordPopup from 'components/popups/PasswordPopup/PasswordPopup';
import { UserContext, userRoles } from 'services/AuthService';

/* Admin Panel component for users that are logged in as admin */
export default function AdminPanel() {
  const [isShowingPassword, setIsShowingPassword] = useState(false);
  const [userInfo, setUserInfo] = useState(null);

  const user = useContext(UserContext);

  if (user.role < userRoles.ADMIN) {
    return;
  }

  /* Function that is called when the password popup should be displayed */
  function handleShowNewPassword(user) {
    setIsShowingPassword(true);
    setUserInfo(user);
  }

  /* Function that is called when the password popup should be closed */
  function handleCloseNewPassword(user) {
    setIsShowingPassword(false);
    setUserInfo(null);
  }

  /* AdminPanel body */
  return (
    <div className="page-wrap">
      <OverlayContext.Provider value={{ showNewPassword: handleShowNewPassword, closeNewPassword: handleCloseNewPassword }} >
        <Outlet />
        <PageOverlay isOpen={isShowingPassword}>
          <PasswordPopup user={userInfo} />
        </PageOverlay>
      </OverlayContext.Provider>

    </div>
  );
}