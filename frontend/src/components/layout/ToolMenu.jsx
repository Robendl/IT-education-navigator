import { useContext } from "react";
import { OverlayContext } from "./PageOverlay/PageOverlay";
import { UserContext } from "services/AuthService";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function ToolMenu() {

  const [selected, setSelected] = useState("overview");

  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);

  const navigate = useNavigate();

  function handleOpenOverview() {
    navigate("/");
    setSelected("overview");
  }

  function handleOpenArchive() {
    navigate("/?archived=1");
    setSelected("archive");
  }

  function handleOpenAccountManagement() {
    navigate("/users");
    setSelected("Accountbeheer")
  }

  return (
    <div className="tool-menu">
      {user.loggedIn && (user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolOption name="Nieuw Item" icon="post_add" action={overlay.openAdd}/>}
      {user.loggedIn && <ToolOption name="Overzicht" icon="list" selected={selected === "overview"} action={handleOpenOverview}/>}
      {user.loggedIn && (user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolOption name="Archief" icon="inventory_2" selected={selected === "archive"} action={handleOpenArchive} />}
      {user.loggedIn && user.role === "ADMIN" && <ToolOption name="Accountbeheer" icon="manage_accounts" action={handleOpenAccountManagement}/>}
    </div>
  );
}

function ToolOption({name, icon, action, selected}) {

  function handleClick(e) {
    e.target.classList.add("selected");
    action();
  }

  return (
    <button onClick={handleClick} className={selected ? "selected" : ""}>
      <span className="material-symbols-outlined tool-icon">{icon}</span>
      <span className="tool-name">{name}</span>
    </button>
  );
}