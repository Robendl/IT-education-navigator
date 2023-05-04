import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import UserLoader from 'services/UserLoader';
import { useContext } from 'react';
import { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import { UserContext } from 'services/AuthService';
import ChangePermissionsPopup from 'components/popups/ChangePermissionsPopup';

export default function ManagementPage () {
  const [isEditing, setIsEditing] = useState(false);
  const [editEntry, setEditEntry] = useState({});

  useEffect(() => {
    UserLoader.loadUsers();
  }, [])

  function handleOpenEdit(entry) {
    setIsEditing(true);
    setEditEntry(entry);
  }
  
  return (
    <div className="page-wrap">
      <OverlayContext.Provider value={{
        openEdit: handleOpenEdit, closeEdit: () => setIsEditing(false), editEntry: editEntry
        }} >
        <UserPanel />
        <PageOverlay isOpen={isEditing} onClose={() => setIsEditing(false)}>
          <ChangePermissionsPopup />
        </PageOverlay>
      </OverlayContext.Provider>
    </div>
  );
}