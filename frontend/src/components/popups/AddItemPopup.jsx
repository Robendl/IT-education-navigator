import AddItemForm from "components/forms/AddItemForm/AddItemForm";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import CourseLoader from "services/CourseLoader";

/* Popup component for adding courses */
export default function AddItemPopup() {
  const overlay = useContext(OverlayContext);

  /* Function that is called when the popup is accepted */
  function handleSubmit() {
    overlay.closeAdd();
    CourseLoader.loadCourses();
  }

  /* Function that is called when the popup is closed */
  function handleClose() {
    overlay.closeAdd();
  }

  return (
    <div className="add-item pop-up ignore-overlay">
      <AddItemForm onCancel={handleClose} onSubmit={handleSubmit} />
    </div>
  )
}