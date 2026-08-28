# UC-002: Create and Edit LISMA Text Project

## Description

This use case describes creating a new text-based LISMA project, editing the source code with syntax highlighting, and the lifecycle of the editor tab.

## Actors

- **User** — creates and edits LISMA source code

## Precondition

- Application is running (UC-001 completed)

## Main Flow

1. **User creates a new text project** — via toolbar button, keyboard shortcut, or File → New text menu.
2. **A new tab is created** labeled "New project" and becomes the active tab.
3. **An empty text editor opens** with line numbers displayed on the left margin.
4. **Syntax highlighting is active** — as the user types, keywords, comments, and numbers are automatically colored by the simulation engine.
5. **User edits source code** — standard text editing is available:
   a. Insert, delete, and select text
   b. Cut, copy, and paste via toolbar, menu, or keyboard shortcuts
6. **User saves the project** — see **UC-004** for the save flow.

## Alternative Flows

### A1: Large file performance

For very large files (thousands of lines), syntax highlighting may cause slight lag as each keystroke triggers a server-side analysis. The UI remains responsive but highlighting updates may not be instantaneous.

### A2: New project without existing projects

At step 2, if no projects are currently open, the new project becomes the first and only tab.

## Postcondition

- A new tab labeled "New project" is open and active
- The text editor displays an empty document with line numbers and syntax highlighting
- The project has not been saved to disk yet

## Related Use Cases

- **UC-001** — Application startup (prerequisite)
- **UC-004** — Saving the project to disk
- **UC-005** — Verifying the model
- **UC-006** — Running a simulation on the project
