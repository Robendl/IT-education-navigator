import logo from 'assets/svg/logo.svg';
import MainHeader from 'components/layout/MainHeader';
import Home from 'pages/home/Home';
import LoginPage from 'pages/login/LoginPage';
import { Routes, Route, Outlet, Link } from 'react-router-dom';
import './App.css';
import AuthService, { UserContext } from 'services/AuthService';
import { useEffect, useState } from 'react';
import RegisterPage from "./pages/register/RegisterPage";
import AdminPanel from 'pages/admin_panel/AdminPanel';
import AccountManagement from 'components/layout/admin_panel/AccountManagement';
import CoursePage from 'pages/course/CoursePage';
import PageOverlay, { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import AddItemPopup from 'components/popups/AddItemPopup/AddItemPopup';
import EditItemPopup from 'components/popups/EditItemPopup/EditItemPopup';
import DeleteCoursePopup from 'components/popups/DeleteItemPopups/DeleteCoursePopup';

/* Main App component */
export default function App() {
  const [userInfo, setUserInfo] = useState({});

  useEffect(() => {
    /* Fetch user info on page load */
    setUserInfo({
      name: AuthService.getUser()?.name,
      loggedIn: AuthService.isLoggedIn(),
      role: AuthService.getRole()
    })
  }, []);

  /* App body with routing to different pages */
  return (
    <div className="App">
      <UserContext.Provider value={userInfo}>
        <Routes>
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />
            <Route path="course/:courseId" element={<CoursePage />}></Route>
            <Route path="admin" element={<AdminPanel />}>
              <Route path="accounts" element={<AccountManagement />} />
            </Route>
          </Route>
        </Routes>
      </UserContext.Provider>
    </div>
  );
}

/* Layout component that holds all pages for logged in users and a page where users can log in */
function Layout() {
  const [isAdding, setIsAdding] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [isDeletingCourse, setIsDeletingCourse] = useState(false);
  const [editEntry, setEditEntry] = useState({});
  const [deleteEntry, setDeleteEntry] = useState({});

  /* Function that is called when the user starts editing a course */
  function handleOpenEdit(entry) {
    setIsEditing(true);
    setEditEntry(entry);
  }

  /* Function that is called when the user starts deleting a course */
  function handleOpenDeleteCourse(entry) {
    setIsDeletingCourse(true);
    setDeleteEntry(entry);
  }

  return (
    <div>
      {(AuthService.isLoggedIn() &&
        <div>
          <OverlayContext.Provider value={{
            openAdd: () => setIsAdding(true), closeAdd: () => setIsAdding(false), openEdit: handleOpenEdit, closeEdit: () => setIsEditing(false), editEntry: editEntry, openDeleteCourse: handleOpenDeleteCourse, closeDeleteCourse: () => setIsDeletingCourse(false), deleteEntry: deleteEntry
          }} >
            <header className="logo-header ignore-overlay">
              <Link to="/">
                <img src={logo} className="ro-logo" alt="logo" />
              </Link>
            </header>
            <MainHeader />
            <Outlet />
            <PageOverlay isOpen={isAdding}>
              <AddItemPopup />
            </PageOverlay>
            <PageOverlay isOpen={isEditing}>
              <EditItemPopup />
            </PageOverlay>
            <PageOverlay isOpen={isDeletingCourse}>
              <DeleteCoursePopup />
            </PageOverlay>
          </OverlayContext.Provider>
        </div>)
        ||
        <LoginPage />
      }

    </div>
  );
}