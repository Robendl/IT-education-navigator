import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';
import { useContext } from 'react';
import { OverlayContext } from './PageOverlay/PageOverlay';
import { UserContext, userRoles } from 'services/AuthService';
import { useRef } from 'react';

/* ResultList component that shows all courses that fit the user's search and order preferences */
export default function ResultList () {
  const [results, setResults] = useState([]);
  const [resultCount, setResultCount] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  const location = useLocation();

  useEffect(() => {
    /* Load courses on page navigate */
    setIsLoading(true);
    CourseLoader.loadCourses(location.search ? location.search : "").then((courses) => {
      setResults(courses);
      setResultCount(courses.length);
      setIsLoading(false);
    });
  }, [location]);

  /* ResultList body */
  return (
    <div className="result-list">
      <div className="result-list-header">
        <span><b>{resultCount}</b> Resultaten</span>
        <span className="material-symbols-outlined">sort</span>
      </div>
      {(isLoading &&
        <LoadingMessage />) ||
        <div className="result-list-entries">
        {results.map((result, idx) => <Result entry={result} key={idx} decrementCount={() => setResultCount(resultCount - 1)}/>)}
        </div>
      }
    </div>
  );
}

/* Result component that shows information on a course */
function Result({entry, decrementCount}) {
  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);
  const [isBeingChanged, setIsBeingChanged] = useState(false);

  const resultElement = useRef();

  /* Function that is called when the course {entry} should be archived */
  function handleArchive(e) {
    if (isBeingChanged) {
      return;
    }
    setIsBeingChanged(true);
    CourseLoader.archiveCourse(entry).then(() => {
      resultElement.current.classList.add("hide");
      decrementCount();
      setTimeout(() => {
        resultElement.current?.remove();
      }, 1500);
    });
  }

  /* Function that is called when the user wants to restore the course {entry} */
  function handleRestore(e) {
    if (isBeingChanged) {
      return;
    }
    setIsBeingChanged(true);
    CourseLoader.restoreCourse(entry).then(() => {
      resultElement.current.classList.add("hide");
      decrementCount();
      setTimeout(() => {
        resultElement.current?.remove();
      }, 1500);
    });
  }

  /* Function that is called when the user wants to delete the course {entry} */
  function handleDelete(e) {
    if (isBeingChanged) {
      return;
    }
    setIsBeingChanged(true);
    CourseLoader.deleteCourse(entry).then(() => {
      window.location.reload();
    });
  }

  /* Function that is called when the user wants to edit the course {entry} */
  function handleEdit() {
    overlay.openEdit(entry);
  }

  /* Result body */
  return (
    <div className="result" ref={resultElement}>
      <div className="result-head">
        <span className="result-tag">{entry["level"]}</span>
        <Link to="#" className="result-name">{entry["name"]} {entry["archived"] && <span>(gearchiveerd)</span>}</Link>
        {(user.role >= userRoles.DATA_MANAGER) && !entry["archived"] &&<button className="edit-button" onClick={handleEdit}><span className="material-symbols-outlined edit-icon">edit</span></button>}
        {(user.role >= userRoles.DATA_MANAGER) && !entry["archived"] &&<button className="archive-button red-hover-button" onClick={handleArchive}><span className="material-symbols-outlined archive-icon">archive</span></button>}
        {(user.role >= userRoles.DATA_MANAGER) && entry["archived"]  &&<button className="unarchive-button" onClick={handleRestore}><span className="material-symbols-outlined unarchive-icon">unarchive</span></button>}
        {(user.role >= userRoles.DATA_MANAGER) && entry["archived"]  &&<button className="delete-button red-hover-button" onClick={handleDelete}><span className="material-symbols-outlined delete-icon">delete</span></button>}
        
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