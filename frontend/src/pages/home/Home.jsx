import Search from "components/layout/home/Search";
import UserPanel from "components/layout/home/UserPanel";
import "./Home.css";

/* Home page component for logged in users */
function Home() {
  /* Home body */
  return (
    <div className="page-wrap">
      <Search />
      <UserPanel />
    </div>
  );
}

export default Home;