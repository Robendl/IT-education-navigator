import { CircularProgress } from "@mui/material";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext, useState } from "react";
import UserLoader, { errorCodes } from "services/UserLoader";
import FocusTrap from '@mui/base/FocusTrap';

import "./DeleteUserPopup.css";

/* Popup component for showing a new password to an admin */
export default function DeleteUserPopup({ user }) {
  const [accepted, setAccepted] = useState(false);
  const overlay = useContext(OverlayContext);

  /* Function that is called when the popup is closed */
  function handleClose() {
    overlay.closeDeleteUser();
  }

  /* DeleteItemPopup body */
  return (
    <FocusTrap open>
      <div className="delete-item pop-up ignore-overlay" tabIndex={-1}>
        {(accepted && <DeleteItem handleClose={handleClose} />)
          || <ConfirmDeleteUser handleClose={handleClose} setAccepted={setAccepted} user={user} />}
      </div>
    </FocusTrap>
  );
}

/* Component that lets the user know the item was deleted. */
function DeleteItem({ handleClose }) {
  return (
    <>
      <span>Item is succesvol verwijderd</span>
      <div className="delete-item-actions">
        <button className="close-button" onClick={handleClose}>Sluiten</button>
      </div>
    </>
  );
}

/* Component that asks the user for confirmation to delete a user */
function ConfirmDeleteUser({ setAccepted, handleClose, user }) {
  const [isAccepting, setIsAccepting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  /* Function that deletes the specific user
   */
  function handleDeleteUser() {
    setIsAccepting(true);
    UserLoader.deleteUser(user).then((response) => {
      setAccepted(true);
      setIsAccepting(false);
    }, (error) => {
      setIsAccepting(false);
      switch (error) {
        case errorCodes.ERR_NETWORK:
          setErrorMessage("Kon niet verbinden met server.");
          break;
        case errorCodes.ERR_OTHER:
          setErrorMessage("Er is iets misgegaan bij het verwijderen.");
          break;
        default:
          break;
      }
    });
  }

  /* ConfirmDeleteUser body */
  return (
    <>
      <span>Dit account verwijderen?</span>
      <b>{user.username}</b>
      <span className="warning-message">Dit kan niet ongedaan worden gemaakt</span>
      {errorMessage && <span className="delete-error-message">{errorMessage}</span>}
      <div className="delete-item-actions">
        <button className="delete-button" onClick={handleDeleteUser}>
          {(isAccepting && <CircularProgress className="delete-loading" />) || <span>Verwijder</span>}
        </button>
        <button className="cancel-button" onClick={handleClose}>Annuleren</button>
      </div>
    </>
  );
}