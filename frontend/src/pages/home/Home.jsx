import Search from "components/layout/home/Search";
import UserPanel from "components/layout/home/UserPanel";
import PageOverlay, { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import AddItemPopup from "components/popups/AddItemPopup";
import EditItemPopup from "components/popups/EditItemPopup";
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