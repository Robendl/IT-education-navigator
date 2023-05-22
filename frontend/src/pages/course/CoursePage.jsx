import { OverlayContext } from "components/layout/PageOverlay/PageOverlay";
import { propertyTranslations } from "config/translations";
import { useContext, useEffect } from "react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CourseLoader from "services/CourseLoader";

import "./CoursePage.css";
import { UserContext, userRoles } from "services/AuthService";

/* CoursePage component that show information on a single course */
export default function CoursePage() {
  const [course, setCourse] = useState({});
  const { courseId } = useParams();
  const overlay = useContext(OverlayContext);
  const user = useContext(UserContext);

  const navigate = useNavigate();

  /* Get the course information on page load */
  useEffect(() => {
    CourseLoader.loadCourse(courseId).then((response) => {
      setCourse(response);
    });
  }, [courseId]);

  /* Function that is called when the course should be edited */
  function handleEdit() {
    overlay.openEdit(course);
  }

  /* Function that is called when the course should be deleted */
  function handleDelete() {
    overlay.openDeleteCourse(course);
  }

  /* CoursePage body */
  return (
    <div className="course-page">
      <div className="course-page-header">
        <div className="page-wide">
          <h2>{course.name}</h2>
          {course.archived && <span className="archived-tag">(Gearchiveerd)</span>}
        </div>
      </div>
      <div className="course-page-body page-wide">
        <h3>Gegevens:</h3>
        {Object.keys(course).filter(key => !(["name", "id", "archived", "province"].includes(key)))
          .map(key => <CourseProperty keyName={key} key={key} course={course} value={course[key]} />)}
        <div className="course-actions">
          {user.role >= userRoles.DATA_MANAGER &&
            <button className="edit-course-button" onClick={handleEdit}>Bewerk</button>
          }
          {user.role >= userRoles.ADMIN &&
            <button className="delete-course-button" onClick={handleDelete}>Verwijder</button>
          }
        </div>
      </div>
    </div>
  );
}

/* Property component that shows information about the course for a specific property */
function CourseProperty({ keyName, value, course }) {
  return (
    <span className="course-property">
      {(keyName === "province" &&
        <><b>{propertyTranslations[keyName]}: </b>{value["name"]}</>)
        ||
        (keyName === "location" &&
          <><b>{propertyTranslations[keyName]}: </b>{value}, {course["province"].name}</>)
        ||
        (["housekeepingRelated", "collaboration"].includes(keyName) &&
          <><b>{propertyTranslations[keyName]}: </b>{value ? "ja" : "nee"}</>)
        ||
        <><b>{propertyTranslations[keyName]}: </b>{value}</>
      }
    </span>
  )
}