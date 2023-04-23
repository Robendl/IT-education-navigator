import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';
import { useContext } from 'react';
import { OverlayContext } from './PageOverlay/PageOverlay';
import { UserContext, userRoles } from 'services/AuthService';

/* ResultList component that shows all courses that fit the user's search and order preferences */
export default function ResultList () {
  const [results, setResults] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const location = useLocation();

  useEffect(() => {
    /* Load courses on page navigate */
    setIsLoading(true);
    CourseLoader.loadCourses(location.search ? location.search : "").then((courses) => {
      setResults(courses);
      setIsLoading(false);
    });
  }, [location]);

  /* ResultList body */
  return (
    <div className="result-list">
      <div className="result-list-header">
        <span><b>{results.length}</b> Resultaten</span>
        <span className="material-symbols-outlined">sort</span>
      </div>
      {isLoading &&
        <LoadingMessage />
      }
      <div className="result-list-entries">
        {results.map((result, idx) => <Result entry={result} key={idx}/>)}
      </div>
    </div>
  );
}

/* Result component that shows information on a course */
function Result({entry}) {
  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);

  /* Function that is called when the course {entry} should be archived */
  function handleArchive(e) {
    CourseLoader.archiveCourse(entry).then(() => {
      window.location.reload();
    });
  }

  /* Function that is called when the user wants to edit the course {entry} */
  function handleEdit() {
    overlay.openEdit(entry);
  }

  /* Result body */
  return (
    <div className="result">
      <div className="result-head">
        <span className="result-tag">{entry["level"]}</span>
        <Link to="#" className="result-name">{entry["name"]} {entry["archived"] && <span>(gearchiveerd)</span>}</Link>
        {(user.role >= userRoles.DATA_MANAGER) && !entry["archived"] &&<button className="edit-button" onClick={handleEdit}><span className="material-symbols-outlined edit-icon">edit</span></button>}
        {(user.role >= userRoles.DATA_MANAGER) && !entry["archived"] &&<button className="archive-button" onClick={handleArchive}><span className="material-symbols-outlined archive-icon">archive</span></button>}
        
      </div>
      <div className="result-body">
        {
          // Return a span for the remaining tags
          Object.keys(entry)
            .filter(key => !["level", "name"].includes(key))
            .map(key => <ResultProperty key={key} keyName={key} value={entry[key]}/>)
        }
      </div>
    </div>
  );
}

/* Property component that shows information about the course for a specific property */
function ResultProperty({ keyName, value }) {
  return (
    <span className="result-property">
    {(keyName === "province" &&
      <><b>{keyName}: </b>{value["name"]}</>) ||
      <><b>{keyName}: </b>{value}</>
    }
    </span>
  )
}

/* Message component that the user can see when the course list is loading */
function LoadingMessage() {
  return (
    <div className="loading-message">
      <CircularProgress />
      <span>Opleidingen worden geladen...</span>
    </div>
  )
}