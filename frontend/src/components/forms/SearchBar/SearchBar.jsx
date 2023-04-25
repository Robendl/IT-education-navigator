import { useRef } from "react";
import { useSearchParams } from "react-router-dom";

/* Searchbar component */
export default function SearchBar() {

  const [searchParams, setSearchParams] = useSearchParams();
  const searchInput = useRef();

  function handleSearch() {
    setSearchParams(prevParams => {
      prevParams.set("search", searchInput.current.value);
      return prevParams
    });
  }

  return (
    <div className="search-bar">
      <input type="search" placeholder="Zoek op opleidingstitel, institutienaam, beschrijving..." ref={searchInput}/>
      <button onClick={handleSearch}><span className="material-symbols-outlined">search</span></button>
    </div>
  ); 
}