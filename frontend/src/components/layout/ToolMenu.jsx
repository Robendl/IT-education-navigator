import { useContext, useEffect } from "react";
import { OverlayContext } from "./PageOverlay/PageOverlay";
import { UserContext, userRoles } from "services/AuthService";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useState } from "react";

/* Enum containing possible tab options */
const tabOption = {
  OVERVIEW: 0,
  ARCHIVE: 1
}

/* Toolmenu component with tabs and buttons */
export default function ToolMenu() {

  const [selected, setSelected] = useState(null);

  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);
  
  const [searchParams] = useSearchParams();

  const navigate = useNavigate();

  /* Function that is called when the course overview is opened */
  function handleOpenOverview() {
    navigate("/");
    setSelected(tabOption.OVERVIEW);
  }

  /* Function that is called when the course archive is opened */
  function handleOpenArchive() {
    navigate("/?archived=1");
    setSelected(tabOption.ARCHIVE);
  }

  useEffect(() => {
    /* Load tab selection state from url search parameters */
    if (searchParams.get("archived") === "1") {
      setSelected(tabOption.ARCHIVE);
    } else {
      setSelected(tabOption.OVERVIEW);
    }
  }, [searchParams]);

  /* ToolMenu body */
  return (
    <div className="tool-menu">
      {user.loggedIn && (user.role >= userRoles.DATA_MANAGER) && <ToolOption name="Nieuw Item" icon="post_add" action={overlay.openAdd}/>}
      {user.loggedIn && <ToolOption name="Overzicht" icon="list" selected={selected === tabOption.OVERVIEW} action={handleOpenOverview}/>}
      {user.loggedIn && (user.role >= userRoles.DATA_MANAGER) && <ToolOption name="Archief" icon="inventory_2" selected={selected === tabOption.ARCHIVE} action={handleOpenArchive} />}
      {user.loggedIn && (user.role >= userRoles.ADMIN) && <ToolOption name="Accountbeheer" icon="manage_accounts"/>}
    </div>
  );
}

/* ToolOption component for the ToolMenu */
function ToolOption({name, icon, action, selected}) {

  /* Function that is called when clicking on the tool option */
  function handleClick(e) {
    e.target.classList.add("selected");
    action();
  }

  /* ToolOption body */
  return (
    <button onClick={handleClick} className={selected ? "selected" : ""}>
      <span className="material-symbols-outlined tool-icon">{icon}</span>
      <span className="tool-name">{name}</span>
    </button>
  );
}