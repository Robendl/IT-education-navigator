const mockLogin = (credentials) => {
  return new Promise((resolve, reject) => {
    if (credentials.username === "user@email.com" && credentials.password === "password") {
      localStorage.setItem("user", JSON.stringify({
        role: userRoles.LIM_DATA_CONSUMER,
        name: "user@email.com"
      }));
      resolve();
    } else {
      reject(errorCodes.ERR_LOGIN_INVALID);
    }
  });
};

jest.mock("services/CourseLoader");

const mockLoadCourses = (filters) => {
  if (filters === "?order-by=institution&direction=ASC") {
    return Promise.resolve(sortByProperty(testCourses, "institution", false));
  }
  if (filters === "?order-by=institution&direction=DESC") {
    return Promise.resolve(sortByProperty(testCourses, "institution", true));
  }
  if (filters === "?search=science") {
    return Promise.resolve(testCourses.filter((value) => value.name.toLowerCase().includes("science")));
  }
  return Promise.resolve(testCourses);
}

const { render, screen, act, waitFor, findByTestId, findAllByRole } = require("@testing-library/react");
const { default: userEvent } = require("@testing-library/user-event");
const { keyboard } = require("@testing-library/user-event/dist/keyboard");
const { default: App } = require("App");
const { BrowserRouter } = require("react-router-dom");
const { default: AuthService, userRoles } = require("services/AuthService");
const { default: CourseLoader, errorCodes } = require("services/CourseLoader");
const { testCourses } = require("./_objects/testCourses");

function sortByProperty(arr, property, reverse) {
  const reverseMult = reverse ? -1 : 1;
  return arr.sort((a, b) => (a[property] > b[property] ? 1 : -1) * reverseMult);
} 

beforeEach(() => {
  // Mock AuthService
  jest.spyOn(AuthService, "login");
  AuthService.login.mockImplementation(mockLogin);
  // Mock CourseLoader
  CourseLoader.loadCourses.mockImplementation(mockLoadCourses);
})

afterEach(() => {
  jest.restoreAllMocks();
});

test("AuthService is mocked", () => {
  expect(jest.isMockFunction(AuthService.login)).toBe(true);
  expect(AuthService.login({username: "wrongUser", password: "wrongPass"})).rejects.toBe(errorCodes.ERR_LOGIN_INVALID);
});

test("CourseLoader is mocked", () => {
  expect(jest.isMockFunction(CourseLoader.loadCourses)).toBe(true);
  expect(CourseLoader.loadCourses()).resolves.not.toHaveLength(0);
});

test("user logging in and viewing the records", async () => {
  const view = render(<BrowserRouter><App /></BrowserRouter>);
  
  // User not yet logged in
  expect(screen.getByText("Login")).toBeInTheDocument();
  // User inputs email
  userEvent.click(screen.getByLabelText("Emailadres"));
  userEvent.keyboard("user@email.com");
  expect(screen.getByLabelText("Emailadres")).toHaveValue("user@email.com");
  // User input password
  userEvent.click(screen.getByLabelText("Wachtwoord"));
  userEvent.keyboard("password");
  expect(screen.getByLabelText("Wachtwoord")).toHaveValue("password");
  // User submits login form
  act(() => {
    userEvent.click(screen.getByText("Login"));
    view.rerender(<BrowserRouter><App /></BrowserRouter>);
  });
  
  // User should have been logged in
  expect(AuthService.login).toHaveBeenCalledWith({username: "user@email.com", password: "password"});
  expect(AuthService.isLoggedIn()).toBe(true);
  expect(CourseLoader.loadCourses).toHaveBeenCalled();
  
  // User can see all the records
  expect(await screen.findAllByTestId("result")).toHaveLength(testCourses.length);
})

test("user ordering the records", async () => {
  let view;
  await act(async () => {
    view = render(<BrowserRouter><App /></BrowserRouter>);
  });
  await screen.queryAllByTestId("result");
  // User clicks the order button
  act(() => {
    userEvent.click(screen.getByTestId("sort-button"));
  });
  await screen.findByText("Sorteren op:");
  // User selects the institution button
  await act(async () => {
    userEvent.click(screen.getByText("Instelling"));
  });

  let results = screen.queryAllByTestId("result");
  let sortedCourses = sortByProperty(testCourses, "institution", false);
  
  // Courses should be ordered by institution (A-Z)
  results.forEach((result, index) => {
    expect(result).toHaveTextContent(sortedCourses[index].institution);
  });

  // User selects the institution button again
  await act(async () => {
    userEvent.click(screen.getByText("Instelling"));
  });
  
  await act(async () => {
    history.replaceState({}, 'reload', '/');
  });
  
  // Courses should now be ordered by institution (Z-A)
  results = screen.queryAllByTestId("result");
  sortedCourses = sortByProperty(testCourses, "institution", true);

  results.forEach((result, index) => {
    expect(result).toHaveTextContent(sortedCourses[index].institution);
  });
})

test("user searching for a course", async () => {
  await act(async () => {
    render(<BrowserRouter><App /></BrowserRouter>);
  });
  // User selects search input
  userEvent.click(screen.getByRole("searchbox"));

  // User input search term and presses enter
  await act(async () => {
    userEvent.keyboard("science{Enter}")
  });

  // User can see search results
  expect(screen.getAllByTestId("result")).toHaveLength(2);
})