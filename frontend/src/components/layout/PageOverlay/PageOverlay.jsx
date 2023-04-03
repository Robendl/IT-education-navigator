import { createContext } from "react"
import './PageOverlay.css';

export const OverlayContext = createContext();

export default function PageOverlay({ isOpen, onClose, children }) {
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