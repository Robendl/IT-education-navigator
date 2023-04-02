import logo from 'assets/svg/logo.svg';
import MainHeader from 'components/layout/MainHeader';
import Home from 'pages/home/Home';
import Map from 'pages/map/Map';
import { Routes, Route, Outlet } from 'react-router-dom';
import './App.css';

import LoginTest from 'pages/testing/LoginTest';

export default function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/logintest" element={<LoginTest />}>

        </Route>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />}></Route>
          <Route path="kaart" element={<Map />}></Route>
        </Route>
      </Routes>
    </div>
  );
}

function Layout() {
  return (
    <div>
      <header className="logo-header">
        <img src={logo} className="ro-logo" alt="logo" />
      </header>
      <MainHeader />
      <Outlet />
    </div>
  );
}