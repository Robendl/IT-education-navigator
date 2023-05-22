import { CircularProgress } from "@mui/material";
import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext, useState } from "react";
import { errorCodes } from "services/UserLoader";
import FocusTrap from '@mui/base/FocusTrap';

import "./DeleteItemPopup.css";
import CourseLoader from "services/CourseLoader";
import { useNavigate, useSearchParams } from "react-router-dom";

/* Popup component for showing a new password to an admin */
export default function DeleteCoursePopup() {
  const [accepted, setAccepted] = useState(false);
  const overlay = useContext(OverlayContext);
  const navigate = useNavigate();
  const searchParams = useSearchParams();

  /* Function that is called when the popup is closed */
  function handleClose() {
    overlay.closeDeleteCourse();
    navigate(`/?${searchParams[0].toString()}`);
  }

  /* DeleteItemPopup body */
  return (
    <FocusTrap open>
      <div className="delete-item pop-up ignore-overlay" tabIndex={-1}>
        {(accepted && <DeleteItem handleClose={handleClose} />)
          || <ConfirmDeleteCourse handleClose={handleClose} setAccepted={setAccepted} course={overlay.deleteEntry} />}
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

/* Component that asks the user for confirmation to reset a password */
function ConfirmDeleteCourse({ setAccepted, handleClose, course }) {
  const [isAccepting, setIsAccepting] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);

  /* Function that deletes the specific course
   */
  function handleDeleteCourse() {
    setIsAccepting(true);
    CourseLoader.deleteCourse(course).then((response) => {
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

  /* ConfirmDeleteCourse body */
  return (
    <>
      <span>Dit item verwijderen?</span>
      <b>{course.name}</b>
      <span className="warning-message">Dit kan niet ongedaan worden gemaakt</span>
      {errorMessage && <span className="delete-error-message">{errorMessage}</span>}
      <div className="delete-item-actions">
        <button className="delete-button" onClick={handleDeleteCourse}>
          {(isAccepting && <CircularProgress className="delete-loading" />) || <span>Verwijder</span>}
        </button>
        <button className="cancel-button" onClick={handleClose}>Annuleren</button>
      </div>
    </>
  );
}