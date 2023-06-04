import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { BrowserRouter } from "react-router-dom";
import { UserContext, userRoles } from "services/AuthService";

export default function TestContainer({ children, userInfo = { loggedIn: true, role: userRoles.ADMIN }, overlay = { openAdd: jest.fn() } }) {
  return (
    <BrowserRouter>
      <OverlayContext.Provider value={overlay}>
        <UserContext.Provider value={userInfo}>
          {children}
        </UserContext.Provider>
      </OverlayContext.Provider>
    </BrowserRouter>
  )
}