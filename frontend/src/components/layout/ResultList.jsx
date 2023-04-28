import { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import { Tooltip } from '@mui/material';
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
        {results.map((result, idx) => <Result entry={result} key={idx} />)}
        </div>
      }
    </div>
  );
}

/* Result component that shows information on a course */
function Result({ entry }) {
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
      entry.archived = true;
      resultElement.current.classList.add("archived");
      setIsBeingChanged(false);
    });
  }

  /* Function that is called when the user wants to restore the course {entry} */
  function handleRestore(e) {
    if (isBeingChanged) {
      return;
    }
    setIsBeingChanged(true);
    CourseLoader.restoreCourse(entry).then(() => {
      entry.archived = false;
      resultElement.current.classList.remove("archived");
      setIsBeingChanged(false);
    });
  }

  /* Function that is called when the user wants to delete the course {entry} */
  function handleDelete(e) {
    if (isBeingChanged) {
      return;
    }
    setIsBeingChanged(true);
    CourseLoader.deleteCourse(entry).then(() => {
      resultElement.current?.remove();
    });
  }

  /* Function that is called when the user wants to edit the course {entry} */
  function handleEdit() {
    overlay.openEdit(entry);
  }

  /* Result body */
  return (
    <div className={`result ${entry["archived"] ? "archived": ""}`} ref={resultElement}>
      <div className="result-head">
        <span className="result-tag">{entry["level"]}</span>
        <Link to="#" className="result-name">{entry["name"]} {entry["archived"] && <span>(gearchiveerd)</span>}</Link>

        { /* Edit button */
          (user.role >= userRoles.DATA_MANAGER) && !entry["archived"] && 
          <ToolTipButton title="Bewerk" buttonClass="edit-button" iconClass="edit-icon" onClick={handleEdit} iconName="edit"/>
        }
        { /* Archive button */
          (user.role >= userRoles.DATA_MANAGER) && !entry["archived"] && 
          <ToolTipButton title="Archiveer" buttonClass="archive-button" iconClass="archive-icon" onClick={handleArchive} iconName="archive" hasRedHover/>
        }
        { /* Restore button */
          (user.role >= userRoles.DATA_MANAGER) && entry["archived"] && 
          <ToolTipButton title="Terugzetten" buttonClass="unarchive-button" iconClass="unarchive-icon" onClick={handleRestore} iconName="unarchive"/>
        }
        { /* Delete button */
          (user.role >= userRoles.ADMIN) && entry["archived"] && 
          <ToolTipButton title="Verwijderen" buttonClass="delete-button" iconClass="delete-icon" onClick={handleDelete} iconName="delete" hasRedHover/>
        }
        
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

function ToolTipButton({ title, buttonClass, iconClass, onClick, hasRedHover, iconName }) {
  return (
    <Tooltip title={title}>
      <button className={`${buttonClass} ${hasRedHover ? "red-hover-button" : ""}`} onClick={onClick}><span className={`material-symbols-outlined ${iconClass}`}>{iconName}</span></button>
    </Tooltip>
  )
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