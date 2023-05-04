import ToolMenu from "./ToolMenu";
import { useContext } from "react";
import { UserContext } from "services/AuthService";

export default function UserPanel() {
  const user = useContext(UserContext);

  return (
    <div className="user-panel page-wide">
      {(user.role === "ADMIN") && <ToolMenu />}
      <div className="user-panel-body">
        <UserList/>
      </div>
    </div>
  );
}