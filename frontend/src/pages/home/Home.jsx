import Search from "components/layout/Search";
import UserPanel from "components/layout/UserPanel";
import CourseLoader from "services/CourseLoader";
import "./Home.css";
import { useEffect } from "react";

function Home() {
  useEffect(() => {
    CourseLoader.loadCourses();
  })
  return (
    <div className="page-wrap">
      <Search />
      <UserPanel />
    </div>
  );
}

export default Home;