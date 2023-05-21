import { useEffect, useState } from 'react';
import { Link, useLocation, useSearchParams } from 'react-router-dom';
import { CircularProgress, ClickAwayListener, IconButton } from '@mui/material';
import { Tooltip } from '@mui/material';
import CourseLoader, { errorCodes } from 'services/CourseLoader';
import { useContext } from 'react';
import { OverlayContext } from './PageOverlay/PageOverlay';
import { UserContext, userRoles } from 'services/AuthService';
import { useRef } from 'react';
import { propertyTranslations } from 'config/translations';
import CloseIcon from '@mui/icons-material/Close';
import SortIcon from '@mui/icons-material/Sort';

/* ResultList component that shows all courses that fit the user's search and order preferences */
export default function ResultList () {
  const [results, setResults] = useState([]);
  const [resultCount, setResultCount] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [searchParams, setSearchParams] = useSearchParams();
  const [sortByOpen, setSortByOpen] = useState(false);

  const location = useLocation();

  function handleRemoveSearch() {
    setSearchParams(s => {
      s.delete("search");
      return s;
    });
  }

  useEffect(() => {
    /* Load courses on page navigate */
    setIsLoading(true);
    setResults([]);
    CourseLoader.loadCourses(location.search ? location.search : "").then((response) => {
      setResults(response.content);
      setResultCount(response.totalElements);
      setIsLoading(false);
    }, (error) => {
      switch (error) {
        case errorCodes.ERR_NETWORK:
          console.error("Kon niet verbinden met server.");
          break;
        case errorCodes.ERR_OTHER:
          console.error("Kon opleidingen niet ophalen.");
          break;
        default:
          break;
      }
    });
  }, [location]);

  function handleSortByOpen() {
    if (sortByOpen) {
      handleSortByClose();
      return;
    }
    setSortByOpen(true);
  }

  function handleSortByClose() {
    setSortByOpen(false);
  }

  /* ResultList body */
  return (
    <div className="result-list">
      <div className="result-list-header">
        <span><b>{resultCount}</b> {getResultText(resultCount)}</span>
        {searchParams.has("search") &&
          <button className="result-list-search-tag" onClick={handleRemoveSearch}>Zoekterm: {searchParams.get("search")} <CloseIcon className="close-icon"/></button>
        }
        <ClickAwayListener onClickAway={handleSortByClose} >
          <div className="sort-button">
            <Tooltip title={<SortBy />}
              PopperProps={{disablePortal: false}}
              componentsProps={{
                tooltip: {
                  sx: {
                    padding: 0,
                    background: "none"
                  }
                }
              }}
              onClose={handleSortByClose}
              open={sortByOpen}
              placement="bottom-end"
              disableHoverListener
              disableFocusListener
              disableTouchListener
              >
              <IconButton onClick={handleSortByOpen}><SortIcon className="sort-icon" /></IconButton>
            </Tooltip>
          </div>
        </ClickAwayListener>
        
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

function getResultText(resultCount) {
  if(resultCount === 1) {
    return "Resultaat"
  } else {
    return "Resultaten"
  }
}

function SortBy() {
  const [sortIndex, setSortIndex] = useState(null);

  return (
    <div className="sort-by">
      <h3 className="sort-by-title">Sorteren op:</h3>
      <SortByEntry keyName="name" name="Naam" sort={sortIndex === 0} sortIndex={0} setSortIndex={setSortIndex} />
      <SortByEntry keyName="location" name="Locatie" sort={sortIndex === 1} sortIndex={1} setSortIndex={setSortIndex} />
      <SortByEntry keyName="institution" name="Instelling" sort={sortIndex === 2} sortIndex={2} setSortIndex={setSortIndex} />
    </div>
  )
}

function SortByEntry({ keyName, name, sort, sortIndex, setSortIndex }) {
  const [sortMode, setSortMode] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams();

  useEffect(() => {
    if (searchParams.get("order-by") === keyName && searchParams.has("direction")) {
      setSortIndex(sortIndex);
      setSortMode(searchParams.get("direction"));
    }
  }, [sortIndex, searchParams, keyName, setSortIndex])

  function updateOrderByUrl(dir) {
    setSearchParams(prevParams => {
      if (dir === null) {
        prevParams.delete("order-by");
        prevParams.delete("direction");
      } else {
        prevParams.set("order-by", keyName);
        prevParams.set("direction", dir);
      }
      return prevParams;
    })
  }

  function handleSort() {
    if (!sort) {
      setSortIndex(sortIndex);
      setSortMode("ASC");
      updateOrderByUrl("ASC");
      return;
    }
    switch (sortMode) {
      case null:
        setSortMode("ASC");
        updateOrderByUrl("ASC");
        break;
      case "ASC":
        setSortMode("DESC");
        updateOrderByUrl("DESC");
        break;
      default:
        setSortMode(null);
        setSortIndex(null);
        updateOrderByUrl(null);
        break;
    }
  }

  return (
    <button className={`sort-by-entry ${sort ? "selected" : ""}`} onClick={handleSort}>
      <span>{name}</span>
      {sort && (
        ((sortMode === "ASC") && <span>A-Z</span>)
        ||
        ((sortMode === "DESC") && <span>Z-A</span>)
      )}
    </button>
  )
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
        <ResultProperty keyName="courseType" value={entry["courseType"]}/>
        <ResultProperty keyName="location" value={entry["location"] + ((entry["province"].id < 12) ? `, ${entry["province"].name}` : "")}/>
        <ResultProperty keyName="institution" value={entry["institution"]}/>
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
      <><b>{propertyTranslations[keyName]}: </b>{value["name"]}</>) ||
      <><b>{propertyTranslations[keyName]}: </b>{value}</>
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