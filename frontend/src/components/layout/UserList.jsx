import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import UserLoader from 'services/UserLoader';
import { useContext } from 'react';
import { OverlayContext } from 'components/layout/PageOverlay/PageOverlay';
import { UserContext } from 'services/AuthService';

export default function UserList () {
    const [results, setResults] = useState([]);

    UserLoader.loadUsers().then((users) => {
        setResults(users);
    })
    
    return (
        <div class="result-list">
            <div class="result-list-entries">
                {results.map((result, idx) => <Result entry={result} key={idx}/>)}
            </div>
        </div>
    )
}

function Result({entry}) {

  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);
  
  
  function handlePermissions() {
    overlay.openEdit(entry);
  }
  
  return (
    <div className="result">
      <div className="result-head">
        {<button className="permissions-button" onClick={handlePermissions}><span>Rechten aanpassen</span></button>}  
      </div>
      <div className="result-body">
        {
          <span className="result-property">
            <b>Gebruikersnaam: </b>{entry["username"]}<br></br>
            <b>Rol: </b>{entry["role"]}
          </span>
        }
      </div>
    </div>
  );
}
  
