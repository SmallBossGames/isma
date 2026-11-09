# UC-001: Application Startup and Default Workspace

## Description

This use case describes the complete application startup sequence: from launching ISMA through background server initialization to restoring the user's previous session state.

## Actors

- **User** — launches the application

## Precondition

- Java runtime is available
- Server and chart viewer scripts are configured

## Main Flow

1. **User launches the application.**
2. **Application initializes its internal services** — the simulation engine and chart viewer subsystems are prepared in the background.
3. **Simulation server starts** — a separate background process is launched to handle compilation, validation, and simulation execution. The application waits for the server to become reachable.
4. **Main window is created** and positioned according to saved preferences.
5. **Previous session is restored** — all projects that were open when the application was last closed are reopened as tabs. Only files that still exist on disk are restored.
6. **Default settings are loaded** — the right-side settings panel shows default simulation parameters (start time, end time, integration method, etc.).
7. **Error list is cleared** — no errors are shown until the user verifies or runs a model.
8. **Application is ready for use** — the user can see the restored workspace and begin working.

## Alternative Flows

### A1: First-time startup (no previous session)

At step 5, if no preferences file exists (first launch), the application opens with an empty editor area and no tabs. All settings show their default values.

### A2: Server startup failure

At step 3, if the simulation server fails to start, the application cannot proceed. The user must check that the server script is correctly configured and try again.

### A3: Partial project restore

At step 5, if some previously opened files have been moved or deleted, only the existing files are restored. Missing files are silently skipped without notification.

## Postcondition

- The main window is displayed with correct geometry and position
- The simulation server is running and ready to process requests
- All previously open projects are restored as tabs
- The settings panel shows default simulation parameters
- The error list is empty

## Related Use Cases

- **UC-002** — Creating a new text project after startup
- **UC-004** — Manual file open/save operations
- **UC-009** — Multi-project workflow with multiple tabs
