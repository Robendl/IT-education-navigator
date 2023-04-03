import Search from "components/layout/Search";
import UserPanel from "components/layout/UserPanel";
import CourseLoader from "services/CourseLoader";
import PageOverlay, { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import AddItemPopup from "components/popups/AddItemPopup";
import "./Home.css";
import { useEffect, useState } from "react";

function Home() {
  const [isAdding, setIsAdding] = useState(false);

  useEffect(() => {
    CourseLoader.loadCourses();
  }, [])
  
  return (
    <div className="page-wrap">
      <Search />
      <OverlayContext.Provider value={{openAdd: () => setIsAdding(true), closeAdd: () => {setIsAdding(false)}}} >
        <UserPanel />
        <PageOverlay isOpen={isAdding} onClose={() => setIsAdding(false)}>
          <AddItemPopup />
        </PageOverlay>
      </OverlayContext.Provider>
    </div>
  );
}

export default Home;