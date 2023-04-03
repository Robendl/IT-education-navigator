import ToolMenu from "./ToolMenu";
import FilterMenu from "./FilterMenu";
import ResultList from "./ResultList";
import { useContext } from "react";
import { UserContext } from "services/AuthService";

export default function UserPanel() {
  const user = useContext(UserContext);

  return (
    <div className="user-panel page-wide">
      {(user.role === "DATA_MANAGER" || user.role === "ADMIN") && <ToolMenu />}
      <div className="user-panel-body">
        <FilterMenu />
        <ResultList />
      </div>
    </div>
  );
}