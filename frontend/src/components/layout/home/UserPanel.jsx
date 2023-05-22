import ToolMenu from "./ToolMenu";
import FilterMenu from "components/layout/FilterMenu";
import ResultList from "./ResultList";
import { useContext } from "react";
import { UserContext, userRoles } from "services/AuthService";

/* User panel component holding a list of courses and filtering options */
export default function UserPanel() {
  const user = useContext(UserContext);

  /* UserPanel body */
  return (
    <div className="user-panel page-wide">
      {(user.role >= userRoles.DATA_MANAGER) && <ToolMenu />}
      <div className="user-panel-body">
        <FilterMenu />
        <ResultList />
      </div>
    </div>
  );
}