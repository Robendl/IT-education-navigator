import SearchBar from 'components/forms/SearchBar/SearchBar';

/* Search component that shows the user a box with a searchbar and other relevant information about the page */
export default function Search() {
  return (
    <div className="course-search">
      <div>
        <h2>Zoek in het register</h2>
        <SearchBar />
      </div>
    </div>
  );
}