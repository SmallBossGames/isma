# UC-007: Export Simulation Results to CSV

## Description

This use case describes exporting simulation results from the internal binary format to a human-readable CSV file that can be opened in spreadsheet applications or processed programmatically.

## Actors

- **User** — exports simulation data for external analysis

## Precondition

- A simulation has been run and completed (UC-006)
- The result appears in the "Completed" section of the Tasks panel
- The result was saved to a file (not memory-only) during the simulation

## Main Flow

1. **User clicks "Export"** on a completed task in the Tasks panel.
2. **A file chooser dialog opens** filtered for `.csv` files.
3. **User selects a destination file path.**
4. **The export begins in the background:**
   a. A header row is written with column names: time, all differential equation variables, all algebraic equation variables, and right-hand-side values
   b. Each simulation data point is written as a row: time value, variable values, and RHS values
   c. The data is streamed from the cached binary result file
5. **The export completes** — the CSV file is written to disk. No confirmation dialog is shown.

## Alternative Flows

### A1: Large result files

For simulations with many data points (e.g., long time ranges with small step sizes), the export may take a noticeable amount of time. There is no progress indicator, but the UI remains responsive.

### A2: No completed results

At step 1, if there are no completed simulations in the Tasks panel, there is nothing to export. The user must run a simulation first.

## Postcondition

- A CSV file is written to the selected location
- The CSV contains a header row followed by one row per simulation time step
- The CSV can be opened in spreadsheet applications (Excel, LibreOffice Calc) or processed programmatically

## CSV Format Example

The CSV format has a header row like `x,DE_var1,DE_var2,AE_var1,f0,f1,f2` followed by data rows like `0.0,1.0,0.5,3.0,0.5,0.25,1.5` and `0.1,1.05,0.48,3.01,0.52,0.24,1.51`. Each row represents one simulation time step with the time value, variable values, and RHS values.

## Related Use Cases

- **UC-006** — Running a simulation (prerequisite)
