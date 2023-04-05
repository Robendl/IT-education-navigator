import { useContext } from "react";
import { OverlayContext } from "./PageOverlay/PageOverlay";
import { UserContext } from "services/AuthService";
import { useNavigate } from "react-router-dom";

export default function ToolMenu() {

  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);

  const navigate = useNavigate();

  return (
    <div className="tool-menu">
      {user.loggedIn && (user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolOption name="Nieuw Item" icon="post_add" action={overlay.openAdd}/>}
      {user.loggedIn && <ToolOption name="Overzicht" icon="list" selected/>}
      {user.loggedIn && (user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolOption name="Archief" icon="inventory_2" action={() => navigate("/?archived=1")} />}
      {user.loggedIn && user.role === "ADMIN" && <ToolOption name="Accountbeheer" icon="manage_accounts"/>}
    </div>
  );
}

function ToolOption({name, icon, action, selected }) {
  return (
    <button onClick={action} className={selected ? "selected" : ""}>
      <span className="material-symbols-outlined tool-icon">{icon}</span>
      <span className="tool-name">{name}</span>
    </button>
  );
}