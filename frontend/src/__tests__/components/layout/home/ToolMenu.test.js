import { cleanup, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import ToolMenu from "components/layout/home/ToolMenu";
import TestContainer from "__tests__/_environments/TestContainer";

const mockOpenAdd = jest.fn();

beforeEach(() => {
  cleanup();
})

/*
 * REQ-M-F2: There must be a button that upon pressing opens the create-record-form. 
 */
describe("Add Item Button", () => {
  it("should be visible", () => {
    render(<TestContainer><ToolMenu /></TestContainer>);
    expect(screen.getByText("Nieuw Item")).toBeInTheDocument();
  });
  it("should trigger the create-record-form to open", () => {
    render(<TestContainer overlay={{ openAdd: mockOpenAdd }}><ToolMenu /></TestContainer>);
    expect(mockOpenAdd).not.toHaveBeenCalled();
    userEvent.click(screen.getByText("Nieuw Item"));
    expect(mockOpenAdd).toHaveBeenCalled();
  })
})