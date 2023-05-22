import EditItemForm from "components/forms/EditItemForm/EditItemForm";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import CourseLoader from "services/CourseLoader";

import "./EditItemPopup.css";

/* Popup component for editing courses */
export default function AddItemPopup() {
  const overlay = useContext(OverlayContext);

  /* Function that is called when the popup is accepted */
  function handleSubmit() {
    overlay.closeEdit();
    CourseLoader.loadCourses();
  }

  /* Function that is called when the popup is closed */
  function handleClose() {
    overlay.closeEdit();
  }

  return (
    <div className="edit-item pop-up ignore-overlay">
      <EditItemForm onCancel={handleClose} onSubmit={handleSubmit} entry={overlay.editEntry} />
    </div>
  )
}