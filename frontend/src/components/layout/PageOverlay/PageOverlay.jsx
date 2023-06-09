import { createContext } from "react"
import './PageOverlay.css';

export const OverlayContext = createContext({ openAdd: null });

/* PageOverlay component creates an overlay over the page (excluding the header)
 * Components such as popups can appear over this overlay */
export default function PageOverlay({ isOpen, children }) {
  return (
    <>
      {isOpen &&
        <div className="page-overlay">
          {children}
          <div className="page-overlay-back"></div>
        </div>
      }
    </>
  );
}