import SearchBar from 'components/forms/SearchBar/SearchBar';
import { Link } from 'react-router-dom';

export default function Search() {
  return (
    <div className="course-search">
      <div>
        <h2>Zoek in het register</h2>
        <SearchBar />
        <Link to="/kaart">Of zoek op de kaart</Link>
      </div>
    </div>
  );
}