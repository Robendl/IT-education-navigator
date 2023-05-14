import Search from "components/layout/Search";
import UserPanel from "components/layout/UserPanel";
import PageOverlay, { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import AddItemPopup from "components/popups/AddItemPopup";
import EditItemPopup from "components/popups/EditItemPopup";
import "./Home.css";
import { useState } from "react";

/* Home page component for logged in users */
function Home() {
  const [isAdding, setIsAdding] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editEntry, setEditEntry] = useState({});

  /* Function that is called when the user starts editing a course */
  function handleOpenEdit(entry) {
    setIsEditing(true);
    setEditEntry(entry);
  }
  
  /* Home body */
  return (
    <div className="page-wrap">
      <Search />
      <OverlayContext.Provider value={{
        openAdd: () => setIsAdding(true), closeAdd: () => setIsAdding(false), openEdit: handleOpenEdit, closeEdit: () => setIsEditing(false), editEntry: editEntry
        }} >
        <UserPanel />
        <PageOverlay isOpen={isAdding}>
          <AddItemPopup />
        </PageOverlay>
        <PageOverlay isOpen={isEditing}>
          <EditItemPopup />
        </PageOverlay>
      </OverlayContext.Provider>
    </div>
  );
}

export default Home;