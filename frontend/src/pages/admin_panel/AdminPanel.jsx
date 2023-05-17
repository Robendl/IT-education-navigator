import { Outlet } from 'react-router-dom';

import "./AdminPanel.css";

export default function AdminPanel() {
  return (
    <div className="page-wrap">
      <Outlet />
    </div>
  );
}