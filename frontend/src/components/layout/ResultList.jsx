import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { CircularProgress } from '@mui/material';
import CourseLoader from 'services/CourseLoader';

export default function ResultList () {
  const [results, setResults] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    CourseLoader.loadCourses().then((courses) => {
      setResults(courses);
      setIsLoading(false);
    });
  }, []);

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

function Result({entry}) {

  function handleArchive(e) {
    CourseLoader.archiveCourse(entry).then(() => {
      window.location.reload();
    });
    
  }

  return (
    <div className="result">
      <div className="result-head">
        <span className="result-tag">{entry["level"]}</span>
        <Link to="#" className="result-name">{entry["name"]} {entry["archived"] && <span>(gearchiveerd)</span>}</Link>
        <button className="archive-button" onClick={handleArchive}><span className="material-symbols-outlined archive-icon">archive</span></button>
        
      </div>
      <div className="result-body">
        {
          // Return a span for the remaining tags
          Object.keys(entry)
            .filter(key => !["level", "name", "province"].includes(key))
            .map(key => <span className="result-property" key={key}><b>{key}: </b>{entry[key]}</span>)
        }
      </div>
    </div>
  );
}

function LoadingMessage() {
  return (
    <div className="loading-message">
      <CircularProgress />
      <span>Opleidingen worden geladen...</span>
    </div>
  )
}