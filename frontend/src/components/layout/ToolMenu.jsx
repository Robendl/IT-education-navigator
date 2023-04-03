import { useContext } from "react";
import { OverlayContext } from "./PageOverlay/PageOverlay";
import { UserContext } from "services/AuthService";

export default function ToolMenu() {

  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);

  return (
    <div className="tool-menu">
      {user.loggedIn && (user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolOption name="Nieuw Item" icon="post_add" action={overlay.openAdd}/>}
      {user.loggedIn && (user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolOption name="Archief" icon="inventory_2"/>}
      {user.loggedIn && user.role === "ADMIN" && <ToolOption name="Accountbeheer" icon="manage_accounts"/>}
    </div>
  );
}

function ToolOption({name, icon, action}) {
  return (
    <button onClick={action}>
      <span className="material-symbols-outlined tool-icon">{icon}</span>
      <span className="tool-name">{name}</span>
    </button>
  );
}