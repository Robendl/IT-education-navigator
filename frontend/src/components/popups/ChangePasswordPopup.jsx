import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import ChangePasswordForm from "../forms/ChangePasswordForm/ChangePasswordForm";

/* Popup component for adding courses */
export default function ChangePasswordPopup() {
    const overlay = useContext(OverlayContext);

    /* Function that is called when the popup is accepted */
    function handleSubmit() {
        overlay.closeAdd();
    }

    /* Function that is called when the popup is closed */
    function handleClose() {
        overlay.closeAdd();
    }

    return (
        <div className="add-item pop-up ignore-overlay">
            <ChangePasswordForm onCancel={handleClose} onSubmit={handleSubmit} />
        </div>
    )
}