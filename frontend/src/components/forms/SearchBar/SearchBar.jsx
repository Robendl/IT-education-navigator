export default function SearchBar() {
  return (
    <div className="search-bar">
      <input type="search" placeholder="Zoek op opleidingstitel..."/>
      <button><span className="material-symbols-outlined">search</span></button>
    </div>
  ); 
}