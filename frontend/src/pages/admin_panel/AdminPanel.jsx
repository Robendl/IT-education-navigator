import { Outlet, useNavigate } from 'react-router-dom';

import "./AdminPanel.css";
import PageOverlay, { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import { useContext, useState } from 'react';
import PasswordPopup from 'components/popups/PasswordPopup/PasswordPopup';
import { UserContext, userRoles } from 'services/AuthService';
import DeleteUserPopup from 'components/popups/DeleteUserPopup/DeleteUserPopup';

/* Admin Panel component for users that are logged in as admin */
export default function AdminPanel() {
  const [isShowingPassword, setIsShowingPassword] = useState(false);
  const [isShowingUserDelete, setIsShowingUserDelete] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const navigate = useNavigate();

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
  function handleCloseNewPassword() {
    setIsShowingPassword(false);
    setUserInfo(null);
  }

  /* Function that is called when the account deletion popup should be displayed */
  function handleShowDeleteUser(user) {
    setIsShowingUserDelete(true);
    setUserInfo(user);
  }

  /* Function that is called when the account deletion popup should be closed */
  function handleCloseDeleteUser() {
    setIsShowingUserDelete(false);
    setUserInfo(null);
    navigate(0);
  }

  /* AdminPanel body */
  return (
    <div className="page-wrap">
      <OverlayContext.Provider value={{ showNewPassword: handleShowNewPassword, closeNewPassword: handleCloseNewPassword, showDeleteUser: handleShowDeleteUser, closeDeleteUser: handleCloseDeleteUser }} >
        <Outlet />
        <PageOverlay isOpen={isShowingPassword}>
          <PasswordPopup user={userInfo} />
        </PageOverlay>
        <PageOverlay isOpen={isShowingUserDelete}>
          <DeleteUserPopup user={userInfo} />
        </PageOverlay>
      </OverlayContext.Provider>

    </div>
  );
}