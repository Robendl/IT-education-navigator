import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import CourseLoader from 'services/CourseLoader';

export default function ResultList () {
  const [results, setResults] = useState([]);

  useEffect(() => {
    CourseLoader.loadCourses().then(setResults);
  }, []);

  return (
    <div className="result-list">
      <div className="result-list-header">
        <span><b>{results.length}</b> Resultaten</span>
        <span className="material-symbols-outlined">sort</span>
      </div>
      <div className="result-list-entries">
        {results.map((result, idx) => <Result entry={result} key={idx}/>)}
      </div>
    </div>
  );
}

function Result({entry}) {
  return (
    <div className="result">
      <div className="result-head">
        <span className="result-tag">{entry["level"]}</span>
        <Link to="#" className="result-name">{entry["name"]}</Link>
      </div>
      <div className="result-body">
        {
          console.log(entry) ||
          // Return a span for the remaining tags
          Object.keys(entry)
            .filter(key => !["level", "name", "province"].includes(key))
            .map(key => <span className="result-property" key={key}><b>{key}: </b>{entry[key]}</span>)
        }
      </div>
    </div>
  );
}