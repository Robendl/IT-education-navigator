import { Outlet } from 'react-router-dom';

import "./AdminPanel.css";
import PageOverlay, { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import { useState } from 'react';
import PasswordPopup from 'components/popups/PasswordPopup/PasswordPopup';

export default function AdminPanel() {
  const [isShowingPassword, setIsShowingPassword] = useState(false);
  const [userInfo, setUserInfo] = useState(null);

  function handleShowNewPassword(user) {
    setIsShowingPassword(true);
    setUserInfo(user);
  }

  function handleCloseNewPassword(user) {
    setIsShowingPassword(false);
    setUserInfo(null);
  }

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