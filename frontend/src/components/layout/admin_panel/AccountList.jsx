import { useContext, useEffect, useRef, useState } from 'react';
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';

import UserLoader, { errorCodes } from 'services/UserLoader';
import { UserContext, userRoles } from 'services/AuthService';
import LockResetIcon from '@mui/icons-material/LockReset';
import DeleteIcon from '@mui/icons-material/Delete';
import { Tooltip } from '@mui/material';
import { OverlayContext } from '../PageOverlay/PageOverlay';

/* AccountList component that displays a table with all users and corresponding actions */
export default function AccountList() {
  const [results, setResults] = useState([]);

  /* Load the users when the account list is opened */
  useEffect(() => {
    UserLoader.loadUsers().then((users) => {
      setResults(users);
    }, (error) => {
      switch (error) {
        case errorCodes.ERR_NETWORK:
          console.error("Kon niet verbinden met server.");
          break;
        case errorCodes.ERR_OTHER:
          console.error("Kon gebruikers niet ophalen.");
          break;
        default:
          break;
      }
    })
  }, []);

  /* AccountList component */
  return (
    <table className="account-table">
      <thead>
        <tr>
          <th>Email</th>
          <th style={{ minWidth: "30%" }}>Bevoegdheid</th>
          <th style={{ minWidth: "30%" }}>Acties</th>
        </tr>
      </thead>
      <tbody>
        {results.map((result, idx) => <AccountResult entry={result} key={idx} />)}
      </tbody>
    </table>
  )
}

function AccountResult({ entry }) {
  const [isEditingRole, setIsEditingRole] = useState(false);
  const [isSavingRole, setIsSavingRole] = useState(false);
  const roleSelect = useRef();

  const user = useContext(UserContext);
  const overlay = useContext(OverlayContext);

  function handleSaveRole() {
    setIsSavingRole(true);
    let newRole = roleSelect.current.value;
    UserLoader.changeUserPermissions(entry.id, newRole).then((response) => {
      entry["role"] = newRole;
      setIsEditingRole(false);
      setIsSavingRole(false);
    }, (error) => {
      switch (error) {
        case errorCodes.ERR_NETWORK:
          console.error("Kon niet verbinden met server.");
          break;
        case errorCodes.ERR_OTHER:
          console.error("Er is iets misgegaan bij het bewerken van de gebruiker.");
          break;
        default:
          break;
      }
    })
  }

  function handleEditRole() {
    setIsEditingRole(true);
  }

  function handleCancelEditRole() {
    setIsEditingRole(false);
  }

  function handlePasswordReset() {
    overlay.showNewPassword(entry);
  }

  function handleDeleteUser() {
    overlay.showDeleteUser(entry);
  }

  return (
    <tr className="account-row">
      <td className="username-col">
        {entry["username"]}
      </td>
      <td>
        <div className="role-col">
          {
            (!isEditingRole &&
              <>
                <span className={isSavingRole ? "saving" : ""}>{entry["role"]}</span>
                {!isSavingRole &&
                  <Tooltip title="Bewerk" disableInteractive>
                    <button className="role-action-button" onClick={handleEditRole}><EditIcon /></button>
                  </Tooltip>
                }
              </>
            )
            ||
            (<>
              <select defaultValue={entry["role"]} ref={roleSelect}>
                {
                  Object.keys(userRoles).map(roleKey => {
                    return (<option value={roleKey} key={roleKey}>{roleKey}</option>)
                  })
                }
              </select>
              <ToolTipButton title="Opslaan" buttonClass="role-action-button role-save-icon outlined" onClick={handleSaveRole}>
                <SaveIcon />
              </ToolTipButton>
              <ToolTipButton title="Annuleren" buttonClass="role-action-button role-close-icon outlined" onClick={handleCancelEditRole}>
                <CloseIcon />
              </ToolTipButton>
            </>)
          }
        </div>
      </td>
      <td>
        <div className="options-col">
          {(user.name !== entry["username"]) &&
            <ToolTipButton title="Wachtwoord resetten" onClick={handlePasswordReset}>
              <LockResetIcon />
            </ToolTipButton>
          }
          {(user.name !== entry["username"]) &&
            <ToolTipButton title="Verwijderen" onClick={handleDeleteUser}>
              <DeleteIcon />
            </ToolTipButton>
          }
        </div>
      </td>
    </tr>
  );
}

function ToolTipButton({ title, buttonClass, onClick, children }) {
  const [isHover, setIsHover] = useState(false);

  return (
    <Tooltip title={title} disableInteractive open={isHover}>
      <button className={buttonClass} onClick={onClick} onMouseOver={() => setIsHover(true)} onMouseOut={() => setIsHover(false)}>{children}</button>
    </Tooltip>
  )
}