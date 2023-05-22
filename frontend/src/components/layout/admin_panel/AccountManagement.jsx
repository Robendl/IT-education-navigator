import AccountList from "components/layout/admin_panel/AccountList";
import { Link } from "react-router-dom";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

import "./AccountManagement.css";

/* AccountManagement component */
export default function AccountManagement() {
  /* AccountManagement body */
  return (
    <div className="account-management page-wide">
      <div className="account-management-sidebar">
        <Link to="/" className="back-button"><ArrowBackIcon /> Terug naar overzicht</Link>
      </div>
      <div className="account-management-content">
        <div className="account-management-header">
          <h2>Accountbeheer</h2>
        </div>
        <div className="account-management-body">
          <AccountList />
        </div>
      </div>

    </div>
  );
}