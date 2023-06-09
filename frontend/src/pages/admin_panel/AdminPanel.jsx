import { Outlet, useNavigate } from 'react-router-dom';

import PageOverlay, { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import { useContext, useState, useMemo, useCallback } from 'react';
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

  /* Function that is called when the account deletion popup should be closed */
  const handleCloseDeleteUser = useCallback(() => {
    setIsShowingUserDelete(false);
    setUserInfo(null);
    navigate(0);
  }, [navigate]);

  const overlayConfig = useMemo(() => ({
    showNewPassword: handleShowNewPassword,
    closeNewPassword: handleCloseNewPassword,
    showDeleteUser: handleShowDeleteUser,
    closeDeleteUser: handleCloseDeleteUser
  }), [handleCloseDeleteUser]);

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

  if (user.role < userRoles.ADMIN) {
    return;
  }

  /* AdminPanel body */
  return (
    <div className="page-wrap">
      <OverlayContext.Provider value={overlayConfig} >
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