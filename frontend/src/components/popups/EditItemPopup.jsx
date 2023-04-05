import EditItemForm from "components/forms/EditItemForm/EditItemForm";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import CourseLoader from "services/CourseLoader";

export default function AddItemPopup() {
  const overlay = useContext(OverlayContext);

  function handleSubmit() {
    overlay.closeEdit();
    CourseLoader.loadCourses();
  }

  function handleClose() {
    overlay.closeEdit();
  }

  return (
    <div className="add-item pop-up ignore-overlay">
      <EditItemForm onCancel={handleClose} onSubmit={handleSubmit} entry={overlay.editEntry} />
    </div>
  )
}