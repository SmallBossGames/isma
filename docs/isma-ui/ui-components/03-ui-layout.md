# UI Layout

## Main Window

The main window is a `BorderPane` with the following layout:

- **top:** A `VBox` containing the menu bar (File, Edit, Simulation menus) and the main toolbar (New, Blueprint, Open, Save, SaveAll, Cut, Copy, Paste, Verify, Store, Load buttons)
- **center:** The editor area containing a `TabPane` with tabs for each open project (text editor or blueprint canvas)
- **right:** The settings panel with sections for Initials, Integration, Event detection, and Result saving
- **bottom:** A nested `BorderPane` with the error list drawer (collapsible table with Row, Position, Fragment, Message columns) above the simulation process bar (Play button and Tasks button)

The left drawer (`Drawer`) is commented out. The error list is a dedicated `ErrorListDrawer` in the bottom area (above the simulation process bar).

## Toolbar Layout

The toolbar contains buttons arranged left to right: New (`add_circle_outline`), Blueprint (`add_box`), Open (`folder_open`), Save (`save`), SaveAll (`save_alt`), separator, Cut (`content_cut`), Copy (`content_copy`), Paste (`content_paste`), separator, Verify (`check_circle`), separator, Store Settings (`bookmark`), Load Settings (`bookmark_border`). Icons use Material Design glyphs.

| # | Icon Glyph | Tooltip | Action |
|---|-----------|---------|--------|
| 1 | `add_circle_outline` | "New model" | Same as File → New text |
| 2 | `add_box` | "New statechart" | Same as File → New Statechart |
| 3 | `folder_open` | "Open model" | Same as File → Open |
| 4 | `save` | "Save current model" | Same as File → Save |
| 5 | `save_alt` | "Save all models" | Same as File → Save all |
| — | *(separator)* | — | — |
| 6 | `content_cut` | "Cut" | Same as Edit → Cut |
| 7 | `content_copy` | "Copy" | Same as Edit → Copy |
| 8 | `content_paste` | "Paste" | Same as Edit → Paste |
| — | *(separator)* | — | — |
| 9 | `check_circle` | "Verify" | Same as Simulation → Verify |
| — | *(separator)* | — | — |
| 10 | `bookmark` | "Store Settings" | Same as Simulation → Store Settings |
| 11 | `bookmark_border` | "Load Settings" | Same as Simulation → Load Settings |

Icons use Material Design glyphs.

## Settings Panel Accordion

The settings panel is a `PropertiesAccordion` with 4 expandable sections:

1. **Initials:** Start (Number field), End (Number field), Step (Number field)
2. **Integration:** Method (ComboBox), Accurate (Checkbox), Accuracy (Number field, disabled when Accurate unchecked), Stable (Checkbox), Parallel (Checkbox), Server (Text field, disabled when Parallel unchecked), Port (Number field, disabled when Parallel unchecked)
3. **Event Detection:** In use (Checkbox), Gamma (Number field, disabled when In use unchecked), Step limit (Checkbox), Low border (Number field, disabled when Step limit unchecked)
4. **Result Saving:** Save result (ComboBox, MEMORY/FILE)

The settings panel extends `PropertiesAccordion` (from toolkit) with 4 sub-views wrapped in `VBox` with `styleClass = "settings-box"` and `prefWidth = 240.0`. Each sub-view contains a `ScrollPane` with a `propertiesGrid` layout (label on the left, control on the right).

## Tab Lifecycle

The tab lifecycle follows this sequence: `ProjectService` adds a project → `IsmaEditorTabPane` observes `addedAsFlow()` which emits → a `Tab(project.name, project.editor)` is created → the tab sets `activeProject = project` → `Tab.textProperty()` is bound to `project.nameProperty()`.

On tab close: the tab fires a `closeRequest` → `ProjectService` calls `dispose()` on the project → `projects.remove(project)` → `IsmaEditorTabPane` removes the tab from `tabs`.

`IsmaEditorTabPane` observes `ProjectService.projects` via coroutine flow. Creates a `Tab` for each project with `Tab(it.name, it.editor)`. On tab close, calls `projectController.close(project)` which removes from the set and disposes the project scope. Tab text is bound to `project.nameProperty()`. Tab selection sets `projectController.activeProject = project`.
