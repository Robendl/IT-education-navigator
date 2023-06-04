/* eslint-disable testing-library/no-unnecessary-act */
import TestContainer from "__tests__/_environments/TestContainer";
import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import ResultList from "components/layout/home/ResultList";
import CourseLoader from "services/CourseLoader";
import { testCourses } from "__tests__/_objects/testCourses";

const mockOpenEdit = jest.fn();

const mockLoadCoursesSingle = () => {
  return new Promise((resolve, reject) => {
    resolve(testCourses.slice(0, 1));
  });
};

const mockLoadCoursesMultiple = (count) => {
  return new Promise((resolve, reject) => {
    resolve(testCourses.slice(0, count));
  });
};

afterEach(() => {
  jest.restoreAllMocks();
});

/* 
 * REQ-M-F4: There must be a button that upon pressing opens the edit-record-form.
 */
describe("Edit Item Button", () => {
  it("should be visible", async () => {
    jest.spyOn(CourseLoader, "loadCourses").mockImplementation(mockLoadCoursesSingle);
    await act(async () => {
      render(<TestContainer><ResultList /></TestContainer>);
    });
    expect(screen.getByTestId("edit-button")).toBeInTheDocument();
  });

  it("should trigger the create-record-form to open", async () => {
    jest.spyOn(CourseLoader, "loadCourses").mockImplementation(mockLoadCoursesSingle);
    await act(async () => {
      render(<TestContainer overlay={{ openEdit: mockOpenEdit }}><ResultList /></TestContainer>);
    });
    expect(mockOpenEdit).not.toHaveBeenCalled();
    userEvent.click(screen.getByTestId("edit-button"));
    expect(mockOpenEdit).toHaveBeenCalled();
  })
})

/*
 * REQ-M-F6: There must be an interface to show the records.
 * REQ-M-F7: The interface must display the records as a list.
 * REQ-M-F8: All records must be displayed.
 */
describe("Result List", () => {
  it("should be visible", async () => {
    jest.spyOn(CourseLoader, "loadCourses").mockImplementation(mockLoadCoursesSingle);
    await act(async () => {
      render(<TestContainer><ResultList /></TestContainer>);
    });
    expect(screen.getByTestId("result-list")).toBeInTheDocument();
  });

  it.each([1, 2, 4])("should display all courses", async (count) => {
    jest.spyOn(CourseLoader, "loadCourses").mockImplementation(() => mockLoadCoursesMultiple(count));
    await act(async () => {
      render(<TestContainer><ResultList /></TestContainer>);
    });
    expect(screen.getAllByTestId("result")).toHaveLength(count);
  })
})