import { useRef } from "react";
import { useSearchParams } from "react-router-dom";

/* Searchbar component */
export default function SearchBar() {

  const setSearchParams = useSearchParams()[1];
  const searchInput = useRef();

  function handleSearch(e) {
    e.preventDefault();
    if (!searchInput.current) {
      return;
    }
    searchInput.current.value = searchInput.current.value.replace(/\s+/, "");
    if (searchInput.current.value === "") {
      setSearchParams(prevParams => {
        prevParams.delete("search");
        return prevParams;
      });
    } else {
      setSearchParams(prevParams => {
        prevParams.set("search", searchInput.current.value);
        return prevParams;
      });
    }
    
  }

  return (
    <form className="search-bar" onSubmit={handleSearch}>
      <input type="search" placeholder="Zoek op opleidingstitel, institutienaam, beschrijving..." ref={searchInput}/>
      <button type="submit"><span className="material-symbols-outlined">search</span></button>
    </form>
  ); 
}