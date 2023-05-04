import ToolMenu from "./ToolMenu";
import { useContext } from "react";
import { UserContext } from "services/AuthService";
import UserList from "./UserList";

export default function Management() {
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