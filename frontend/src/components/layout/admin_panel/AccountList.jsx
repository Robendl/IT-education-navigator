import { useContext, useEffect, useRef, useState } from 'react';
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from '@mui/icons-material/Close';
import EditIcon from '@mui/icons-material/Edit';

import UserLoader from 'services/UserLoader';
import { UserContext, userRoles } from 'services/AuthService';
import LockResetIcon from '@mui/icons-material/LockReset';
import DeleteIcon from '@mui/icons-material/Delete';
import { Tooltip } from '@mui/material';

export default function AccountList() {
  const [results, setResults] = useState([]);

  useEffect(() => {
    UserLoader.loadUsers().then((users) => {
      setResults(users);
    })
  }, []);

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

  function handleSaveRole() {
    setIsSavingRole(true);
    let newRole = roleSelect.current.value;
    UserLoader.changeUserPermissions(entry.id, newRole).then((response) => {
      entry["role"] = newRole;
      setIsEditingRole(false);
      setIsSavingRole(false);
    })
  }

  function handleEditRole() {
    setIsEditingRole(true);
  }

  function handleCancelEditRole() {
    setIsEditingRole(false);
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
          <ToolTipButton title="Wachtwoord resetten" onClick={() => { return }}>
            <LockResetIcon />
          </ToolTipButton>
          {(user.name !== entry["username"]) &&
            <ToolTipButton title="Verwijderen" onClick={() => { return }}>
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