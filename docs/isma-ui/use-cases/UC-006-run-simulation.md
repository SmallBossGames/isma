# UC-006: Run Simulation with Results Visualization

## Description

This use case describes the complete simulation pipeline: from configuring parameters and triggering execution, through real-time progress monitoring, to displaying the results as an interactive chart. This is the primary end-to-end workflow of the ISMA application.

## Actors

- **User** — configures and runs a simulation, views results
- **Simulation server** — executes the compilation and simulation

## Precondition

- Application is running (UC-001 completed)
- A LISMA text project or blueprint project is open and active
- The simulation server is running
- Simulation parameters are configured in the right-side settings panel

## Main Flow

### Part A: Parameter Configuration

1. **User configures simulation parameters** in the right-side settings panel:
   a. **Time range** — start time, end time, and initial step size for the integration
   b. **Integration method** — the numerical algorithm to use (e.g., Euler, Runge-Kutta variants), with options for adaptive accuracy and stability control
   c. **Event detection** — optional zero-crossing detection for hybrid systems with discrete state transitions
   d. **Result storage** — whether results are kept in memory only or saved to a file for later use

### Part B: Simulation Trigger

2. **User triggers simulation** — via the Play button, keyboard shortcut (`Ctrl+F5`), or Simulation → Run menu.

### Part C: Compilation

3. **The model is compiled** by the simulation server — the LISMA source code (or converted statechart) is checked and prepared for execution.
4. **If compilation fails**, errors are displayed in the Error List panel and the simulation does not run. The user must fix the errors and retry.
5. **If compilation succeeds**, the simulation proceeds.

### Part D: Execution and Progress Monitoring

6. **The simulation begins execution** on the server.
7. **A task entry appears** in the Tasks panel with:
   - A task number (Task #1, Task #2, etc.)
   - A progress bar showing completion percentage
   - An Abort button to cancel the simulation
8. **Progress updates are received** in real-time as the simulation runs. The progress bar reflects the current completion percentage, calculated from the simulation's current time relative to the configured time range.

### Part E: Result Download

9. **The simulation completes** — the server finishes execution and produces results.
10. **Results are downloaded** from the server to a local cache file in binary format.
11. **The task moves** from "In progress" to "Completed" in the Tasks panel, with action buttons:
    - **Show** — view results as a chart
    - **Export** — export results to CSV
    - **Remove** — remove the task from the list
    - **Details** — view simulation metadata (parameters, timing)

### Part F: Results Visualization

12. **User clicks "Show"** on the completed task.
13. **A variable selection dialog opens:**
    - X-axis: a dropdown to select the independent variable (time is pre-selected)
    - Y-axis: a checklist of all available output variables
    - "Select all" / "Unselect all" convenience buttons
14. **User selects the variables** to plot — one X-axis variable and one or more Y-axis variables.
15. **User confirms the selection.**
16. **The chart viewer launches** as a separate window, displaying an interactive chart of the simulation results with the selected variables.

## Alternative Flows

### A1: Compilation errors

At step 4, if compilation produces errors, the simulation is aborted. The user must fix the errors (see UC-005) and retry.

### A2: Simulation cancellation

At step 7, the user can click the Abort button at any time to stop the simulation. See **UC-010** for details.

### A3: Server error during simulation

At step 6, if the server encounters an error (e.g., internal failure), the simulation stops and an error is shown. There is no automatic retry.

### A4: Result saved to memory only

At step 1d, if the user selected in-memory storage, results are not written to a file on disk. The chart viewer can still display them, but they are not persisted.

### A5: User cancels variable selection

At step 15, if the user cancels the variable selection dialog, no chart is generated. The simulation result remains in the "Completed" section.

### A6: Chart viewer not configured

At step 16, if the chart viewer script is not configured or not found, the chart window does not open. The user should check the chart viewer configuration.

## Postcondition

- The simulation result appears in the "Completed" section of the Tasks panel
- The user can view the chart, export to CSV, or remove the result
- The result is cached locally (if file storage was selected)

## Related Use Cases

- **UC-001** — Application startup (prerequisite)
- **UC-005** — Verifying the model before running
- **UC-007** — Exporting results to CSV
- **UC-008** — Configuring simulation parameters
- **UC-010** — Canceling a running simulation
