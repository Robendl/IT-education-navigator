import logo from 'assets/svg/logo.svg';
import MainHeader from 'components/layout/MainHeader';
import Home from 'pages/home/Home';
import Map from 'pages/map/Map';
import LoginPage from 'pages/login/LoginPage';
import { Routes, Route, Outlet } from 'react-router-dom';
import './App.css';
import AuthService, { UserContext } from 'services/AuthService';
import { useEffect, useState } from 'react';
import RegisterPage from "./pages/register/RegisterPage";

export default function App() {
  const [userInfo, setUserInfo] = useState({});

  useEffect(() => {
    setUserInfo({
      name: AuthService.getUser()?.name,
      loggedIn: AuthService.isLoggedIn(),
      role: AuthService.getRole()
    })
  }, []);

  return (
    <div className="App">
      <UserContext.Provider value={userInfo}>
        <Routes>
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />}></Route>
            <Route path="kaart" element={<Map />}></Route>
          </Route>
        </Routes>
      </UserContext.Provider>
    </div>
  );
}

function Layout() {
  return (
    <div>
      {(AuthService.getRole() &&
        <div>
          <header className="logo-header ignore-overlay">
            <img src={logo} className="ro-logo" alt="logo" />
          </header>
          <MainHeader />
          <Outlet />
        </div>)
        ||
        <LoginPage />
      }
      
    </div>
  );
}