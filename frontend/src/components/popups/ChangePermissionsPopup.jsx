import ChangePermissionsForm from "components/forms/ChangePermissionsForm/ChangePermissionsForm";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import UserLoader from "services/UserLoader";

export default function ChangePermissionsPopup() {
  const overlay = useContext(OverlayContext);

  function handleSubmit() {
    overlay.closeEdit();
    UserLoader.loadUsers();
  }

  function handleClose() {
    overlay.closeEdit();
  }

  return (
    <div className="add-item pop-up ignore-overlay">
      <ChangePermissionsForm onCancel={handleClose} onSubmit={handleSubmit} entry={overlay.editEntry} />
    </div>
  )
}