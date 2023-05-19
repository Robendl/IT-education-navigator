import ContentCopyIcon from "@mui/icons-material/ContentCopy";
import { CircularProgress, Tooltip, Zoom } from "@mui/material";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext, useState } from "react";
import UserLoader, { errorCodes } from "services/UserLoader";
import FocusTrap from '@mui/base/FocusTrap';

import "./PasswordPopup.css";

/* Popup component for showing a new password to an admin */
export default function PasswordPopup({ user }) {
  const [accepted, setAccepted] = useState(false);
  const [newPass, setNewPass] = useState(null);
  const overlay = useContext(OverlayContext);

  /* Function that is called when the popup is closed */
  function handleClose() {
    overlay.closeNewPassword();
  }


  return (
    <FocusTrap open>
      <div className="new-pass pop-up ignore-overlay" tabIndex={-1}>
        {(accepted && <ResetPassword user={user} handleClose={handleClose} newPass={newPass} />)
          || <ConfirmResetPassword user={user} handleClose={handleClose} setAccepted={setAccepted} setNewPass={setNewPass} />}
      </div>
    </FocusTrap>
  );
}

function ResetPassword({ user, newPass, handleClose }) {
  return (
    <>
      <span>Nieuw wachtwoord voor <b>{user.username}</b>:</span>
      <CopyText text={newPass} />
      <div className="new-pass-actions">
        <button className="close-button" onClick={handleClose}>Sluiten</button>
      </div>
    </>
  );
}

function ConfirmResetPassword({ user, setAccepted, setNewPass, handleClose }) {
  const [isAccepting, setIsAccepting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  function handleResetPassword() {
    setIsAccepting(true);
    UserLoader.resetUserPassword(user.id).then((response) => {
      setAccepted(true);
      setIsAccepting(false);
      setNewPass(response.password);
    }, (error) => {
      setIsAccepting(false);
      switch (error) {
        case errorCodes.ERR_NETWORK:
          setErrorMessage("Kon niet verbinden met server.");
          break;
        case errorCodes.ERR_OTHER:
          setErrorMessage("Er is iets misgegaan bij het resetten van het wachtwoord.");
          break;
        default:
          break;
      }
    });
  }

  return (
    <>
      <span>Wachtwoord resetten voor <b>{user.username}</b>?</span>
      <span className="warning-message">Dit kan niet ongedaan worden gemaakt</span>
      {errorMessage && <span className="login-error-message">{errorMessage}</span>}
      <div className="new-pass-actions">
        <button className="reset-button" onClick={handleResetPassword}>
          {(isAccepting && <CircularProgress className="reset-loading" />) || <span>Reset</span>}
        </button>
        <button className="cancel-button" onClick={handleClose}>Annuleren</button>
      </div>
    </>
  );
}

function CopyText({ text }) {
  const [copied, setCopied] = useState(false);

  function handleCopy() {
    setCopied(true);
    navigator.clipboard.writeText(text);
  }

  return (
    <div className="copy-text">
      <input type="text" readOnly value={text} />
      <Tooltip title="Gekopieerd" disableFocusListener disableTouchListener disableHoverListener open={copied} TransitionComponent={Zoom}>
        <button className="copy-text-button" onClick={handleCopy} >
          <ContentCopyIcon className="copy-text-icon" />
        </button>
      </Tooltip>
    </div>
  );
}