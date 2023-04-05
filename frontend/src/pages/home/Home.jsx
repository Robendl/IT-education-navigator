import Search from "components/layout/Search";
import UserPanel from "components/layout/UserPanel";
import CourseLoader from "services/CourseLoader";
import PageOverlay, { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import AddItemPopup from "components/popups/AddItemPopup";
import EditItemPopup from "components/popups/EditItemPopup";
import "./Home.css";
import { useEffect, useState } from "react";

function Home() {
  const [isAdding, setIsAdding] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editEntry, setEditEntry] = useState({});

  useEffect(() => {
    CourseLoader.loadCourses();
  }, [])

  function handleOpenEdit(entry) {
    setIsEditing(true);
    setEditEntry(entry);
  }
  
  return (
    <div className="page-wrap">
      <Search />
      <OverlayContext.Provider value={{
        openAdd: () => setIsAdding(true), closeAdd: () => setIsAdding(false), openEdit: handleOpenEdit, closeEdit: () => setIsEditing(false), editEntry: editEntry
        }} >
        <UserPanel />
        <PageOverlay isOpen={isAdding} onClose={() => setIsAdding(false)}>
          <AddItemPopup />
        </PageOverlay>
        <PageOverlay isOpen={isEditing} onClose={() => setIsEditing(false)}>
          <EditItemPopup />
        </PageOverlay>
      </OverlayContext.Provider>
    </div>
  );
}

export default Home;