# UC-009: Multi-Project Workflow

## Description

This use case describes managing multiple concurrent projects — both LISMA text projects and blueprint statechart projects — simultaneously. The user can switch between tabs, edit different project types, and save or close individual projects without affecting others.

## Actors

- **User** — works on multiple projects in parallel

## Precondition

- Application is running (UC-001 completed)

## Main Flow

### Part A: Opening Multiple Projects

1. **User creates or opens projects** — each project becomes a separate tab in the editor area:
   a. Text projects (`.iscm2`) open as LISMA text editor tabs
   b. Blueprint projects (`.scisma`) open as visual statechart tabs
   c. Multiple files can be opened at once via the Open dialog
2. **All tabs are visible** in the tab bar. Each tab shows:
   - The project name (filename if saved, default name if new)
   - A close button (X)

### Part B: Switching Between Projects

3. **User clicks a tab** to switch to that project — it becomes the active project.
4. **All toolbar and menu actions** (Save, Verify, Run, Cut, Copy, Paste) now apply to the active project.
5. **Each project maintains its own state:**
   - Text content and cursor position
   - Scroll position in the editor
   - Canvas layout for blueprint projects
6. **User switches back and forth** between projects — all edits and layouts are preserved per project.

### Part C: State Content Editing

7. **In a blueprint project, the user double-clicks a state** — a separate text editor tab opens for editing that state's body content.
8. **These content editor tabs are independent** of the main project tabs — they can be opened and closed without affecting the blueprint project.
9. **Closing a content editor tab** disposes of that editor instance but keeps the blueprint project open.

### Part D: Saving and Closing

10. **User saves the active project** — only the currently selected tab is saved (`Ctrl+S`).
11. **User saves all projects** — every open project is saved (File → Save all). Unsaved projects prompt for a file location.
12. **User closes a single tab** — only that project is removed. All other projects remain open with their state preserved.
13. **User closes all projects** — all tabs are removed and all resources are released.

### Part E: Mixed Project Types

14. **Text and blueprint tabs can coexist** in the same editor area — there is no restriction on mixing project types.
15. **Clipboard operations** (Cut/Copy/Paste) are routed to the currently focused editor, whether it is a text editor or a blueprint's text fields.

## Alternative Flows

### A1: Closing the last project

At step 12, if the closed tab is the last remaining project, the editor area becomes empty. The user can create or open a new project.

### A2: Tab order

Tabs appear in the order they were created or opened. The user cannot reorder tabs manually.

## Postcondition

- Multiple projects are open simultaneously, each in its own tab
- The user can switch between projects freely
- Each project maintains its own state and editor instance independently
- Closing one project does not affect others

## Related Use Cases

- **UC-001** — Restoring multiple projects at startup
- **UC-002** — Editing a text project
- **UC-003** — Editing a blueprint project
- **UC-004** — Saving/closing individual projects
