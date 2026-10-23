# UC-004: Open, Save, and Close Projects

## Description

This use case describes file I/O operations for ISMA projects: opening files of different types, saving (both normal and "save as"), saving all open projects, and closing individual or all projects.

## Actors

- **User** — manages project files on disk

## Precondition

- Application is running (UC-001 completed)
- At least one project is open (for save/close operations)

## Main Flow

### Part A: Opening a Project File

1. **User triggers open** — via toolbar button, keyboard shortcut (`Ctrl+O`), or File → Open menu.
2. **A file chooser dialog opens** with filters for ISMA file types:
   - `.iscm2` — LISMA text projects
   - `.scisma` — Blueprint statechart projects
   - `.im2` — Legacy ISMA projects
3. **User selects one or more files** (multi-select is supported).
4. **Each file is opened as a new tab:**
   - Text files (`.iscm2`, `.im2`) open as LISMA text editor tabs
   - Statechart files (`.scisma`) open as visual blueprint editor tabs, with the canvas rebuilt from the saved layout
5. **Tab titles** are derived from the filename (without extension).

### Part B: Saving a Project

6. **User triggers save** — via toolbar button, keyboard shortcut (`Ctrl+S`), or File → Save menu.
7. **If the project has been saved before** (has a file path):
   a. LISMA text projects — the current source code is written to the file
   b. Blueprint projects — the visual statechart is serialized to JSON and written to the file
8. **If the project is new** (never saved): a "Save as" dialog opens (see Part C).

### Part C: Save As

9. **User triggers save as** — clicking Save on an unsaved project, or File → Save as menu.
10. **A file chooser dialog opens** with a filter appropriate to the project type.
11. **User selects a location and filename.**
12. **The project is saved** to the selected file.
13. **The tab title updates** to the new filename.

### Part D: Save All

14. **User triggers save all** — via toolbar button or File → Save all menu.
15. **Each open project is saved:**
    a. Projects with a known file path are saved directly
    b. New (unsaved) projects prompt a "Save as" dialog
16. **If the user cancels any "Save as" dialog**, the remaining unsaved projects are not saved.

### Part E: Closing a Project

17. **User triggers close** — clicking the tab's close button (X), File → Close menu, or a close command.
18. **The project tab is removed** from the editor area.
19. **All resources associated with the project are released** — editor instances, cached data, and temporary files.
20. **If no projects remain**, the editor area is empty.

### Part F: Closing All Projects

21. **User triggers close all** — via File → Close all menu.
22. **All open project tabs are removed** and their resources are released.
23. **The editor area is empty.**

## Alternative Flows

### A1: File no longer exists

At step 4, if a file path from the last session no longer exists on disk, the file is silently skipped. Only existing files are restored.

### A2: Legacy file format

At step 4, `.im2` files are opened as LISMA text projects. The legacy `.im` format has limited backward compatibility — some features may not be fully supported.

### A3: Save as cancellation

At step 11, if the user cancels the file chooser, the save operation is aborted. The project remains unsaved with no file path.

### A4: Close with no active project

At step 17, if no project is active, the close operation has no effect.

### A5: Save all with multiple unsaved projects

At step 15b, if multiple projects need "Save as", the user is presented with a series of file chooser dialogs. Canceling any dialog stops the remaining saves.

## Postcondition

- Opened projects appear as tabs with correct content
- Saved projects are written to disk in the appropriate format
- Closed projects are fully cleaned up

## Related Use Cases

- **UC-001** — Restoring last-opened projects at startup
- **UC-002** — Creating a new text project (unsaved initially)
- **UC-003** — Creating a new statechart (unsaved initially)
- **UC-005** — Verifying a project after opening
- **UC-009** — Managing multiple open projects
