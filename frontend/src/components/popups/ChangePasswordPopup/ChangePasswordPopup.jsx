import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { useContext } from "react";
import ChangePasswordForm from "../../forms/ChangePasswordForm/ChangePasswordForm";

/* Popup component for adding courses */
export default function ChangePasswordPopup() {
    const overlay = useContext(OverlayContext);

    /* Function that is called when the popup is accepted */
    function handleSubmit() {
        console.log("closing..")
        overlay.closeChangePassword();
    }

    /* Function that is called when the popup is closed */
    function handleClose() {
        overlay.closeChangePassword();
    }

    return (
        <div className="change-password pop-up ignore-overlay">
            <ChangePasswordForm onCancel={handleClose} onSubmit={handleSubmit} />
        </div>
    )
}