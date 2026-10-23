# UC-008: Configure and Load/Store Simulation Parameters

## Description

This use case describes setting up simulation parameters in the right-side settings panel and persisting them as preset files for later reuse. This allows users to save complex parameter configurations and reload them for different simulation runs.

## Actors

- **User** — configures simulation parameters, saves and loads presets

## Precondition

- Application is running (UC-001 completed)
- The right-side settings panel is visible

## Main Flow

### Part A: Configuring Parameters

1. **User adjusts parameters** in the right-side settings panel:
   a. **Time range** (Cauchy Initials):
      - Start time — when the simulation begins (default: 0.0)
      - End time — when the simulation ends (default: 10.0)
      - Initial step — the starting step size for integration (default: 0.1)
   b. **Integration method:**
      - Method — selected from the server's available algorithms (e.g., Euler, Runge-Kutta variants)
      - Accurate — enables adaptive step-size control (adjusts step size dynamically to maintain precision)
      - Accuracy — the tolerance for adaptive integration (only editable when "Accurate" is checked, default: 0.1)
      - Stable — enables stability control to prevent numerical divergence
      - Parallel — enables remote cluster execution
      - Server / Port — network address for parallel execution (only editable when "Parallel" is checked)
   c. **Event detection** (zero-crossing detection):
      - In use — enables detection of when variables cross zero (default: unchecked)
      - Gamma — sensitivity threshold for event detection (only editable when "In use" is checked, default: 0.8)
      - Step limit — constrains step size during event detection searches
      - Low border — minimum step size during event detection (only editable when "Step limit" is checked, default: 0.001)
   d. **Result storage:**
      - MEMORY — results kept in memory only (not persisted to disk)
      - FILE — results saved to a binary cache file for later use

2. **Parameters are applied immediately** to the settings panel — changes are reflected as the user adjusts them.
3. **Parameters are not sent to the server** until the user clicks "Run". At that point, the current values are captured and used for the simulation.

### Part B: Storing Parameters

4. **User triggers store settings** — via toolbar button or Simulation → Store Settings menu.
5. **A file chooser dialog opens** filtered for parameter files (`.params.json`).
6. **User selects a location and filename.**
7. **Current parameters are saved** to the file, including:
   - Time range (start, end, step)
   - Integration method settings (method, accuracy, stability, parallel config)
   - Event detection settings (enabled/disabled, gamma, step limit, low border)
   - Result storage preference
8. **Note:** Result processing settings (simplify method, tolerance) are **not** saved — they are not included in the stored preset.

### Part C: Loading Parameters

9. **User triggers load settings** — via toolbar button or Simulation → Load Settings menu.
10. **A file chooser dialog opens** filtered for parameter files (`.params.json`).
11. **User selects a previously saved parameter file.**
12. **The settings panel is updated** with the loaded values:
    - Time range fields show the saved start, end, and step
    - Integration method dropdown shows the saved method
    - Checkboxes reflect the saved boolean settings
    - Event detection fields show the saved values
13. **Result processing settings are not affected** — they retain their current values since they were never saved.

## Alternative Flows

### A1: No integration methods available

At step 1b, if the server was unavailable at startup and no methods were loaded, the integration method dropdown is empty. The user must restart the application after the server becomes available.

### A2: Conditional field availability

At step 1, several fields are conditionally enabled:
- Accuracy field is disabled when "Accurate" is unchecked
- Server/Port fields are disabled when "Parallel" is unchecked
- Gamma field is disabled when "In use" is unchecked
- Low border field is disabled when "Step limit" is unchecked

Disabled fields cannot be edited. Their values are still sent to the server at simulation time but are ignored.

### A3: Corrupted parameter file

At step 11, if the parameter file is malformed or contains unrecognized data, the load operation fails silently. No error message is shown to the user.

### A4: Result Processing controls

The Result Processing section currently shows only the "Save result" control. The planned "Simplify" method selector and "Tolerance" field are not yet active in the UI.

## Postcondition

- Parameters are either saved to a preset file or loaded from one
- The settings panel reflects the current (saved or loaded) values
- The next simulation run will use the current parameter values

## Related Use Cases

- **UC-006** — Running a simulation (uses the configured parameters)
- **UC-001** — Application startup (integration methods loaded from server)
