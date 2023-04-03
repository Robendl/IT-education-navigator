import AddItemForm from "components/forms/AddItemForm/AddItemForm";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import CourseLoader from "services/CourseLoader";

export default function AddItemPopup() {
  const overlay = useContext(OverlayContext);

  function handleSubmit() {
    overlay.closeAdd();
    CourseLoader.loadCourses();
  }

  function handleClose() {
    overlay.closeAdd();
  }

  return (
    <div className="add-item pop-up ignore-overlay">
      <AddItemForm onCancel={handleClose} onSubmit={handleSubmit} />
    </div>
  )
}