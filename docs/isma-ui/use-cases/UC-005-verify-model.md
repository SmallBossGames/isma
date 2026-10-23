# UC-005: Verify Model (Syntax/Semantic Validation)

## Description

This use case describes validating a LISMA model's source code without executing it. Verification checks the model for syntax and semantic errors and displays the results in the Error List panel.

## Actors

- **User** — validates a model before running simulation

## Precondition

- Application is running (UC-001 completed)
- A LISMA text project or blueprint project is open and active
- The simulation server is running

## Main Flow

1. **User triggers verification** — via toolbar button, keyboard shortcut (`Ctrl+F4`), or Simulation → Verify menu.
2. **The active project's source code is sent** to the simulation engine for analysis.
   - For text projects: the current LISMA source code is validated directly
   - For blueprint projects: the visual statechart is first converted to LISMA text, then validated
3. **The simulation engine checks for errors:**
   - Syntax errors (invalid LISMA grammar, malformed statements)
   - Semantic errors (undefined variables, type mismatches, structural issues)
4. **Results are returned** to the application.
5. **Errors are displayed** in the Error List panel at the bottom of the window:
   - Each error shows the line number, position, code fragment name, and a human-readable description
   - The error list is **replaced** (not appended) — each verification shows only the current results
6. **If no errors exist**, the Error List panel is cleared.

## Alternative Flows

### A1: No active project

At step 2, if no project is open, verification does nothing. The user must open or create a project first.

### A2: Blueprint project conversion

At step 2 (blueprint), the visual statechart is converted to LISMA text before validation. Any reported errors reference line numbers in the generated text, not positions on the canvas.

### A3: Server unavailable

At step 2, if the simulation server is not reachable, verification fails silently. The user sees no result and should check that the server is running.

### A4: Warnings only

If the server returns warnings but no errors, the error list is cleared. Warnings are advisory messages that do not prevent simulation but are not currently displayed in the UI.

## Postcondition

- The Error List panel shows validation results (errors or empty)
- Previous errors are replaced with the latest results
- The user can proceed to run simulation if no errors exist

## Related Use Cases

- **UC-002** — Editing a LISMA text project (prerequisite)
- **UC-003** — Editing a blueprint statechart (prerequisite)
- **UC-006** — Running simulation (verification typically precedes simulation)
