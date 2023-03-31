import ToolMenu from "./ToolMenu";
import FilterMenu from "./FilterMenu";
import ResultList from "./ResultList";

export default function UserPanel() {
  return (
    <div className="user-panel page-wide">
      <ToolMenu />
      <div className="user-panel-body">
        <FilterMenu />
        <ResultList />
      </div>
    </div>
  );
}