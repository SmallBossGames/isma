# UC-003: Create and Edit Blueprint Statechart

## Description

This use case describes the complete lifecycle of a visual statechart project: creation, adding states and transitions, editing state content and transition predicates, and the automatic conversion to LISMA text for simulation.

## Actors

- **User** — designs a finite-state machine visually

## Precondition

- Application is running (UC-001 completed)

## Main Flow

### Part A: Creating the Statechart

1. **User creates a new statechart** — via toolbar button, keyboard shortcut, or File → New Statechart menu.
2. **A new tab is created** labeled "New statechart" showing a visual canvas.
3. **The canvas starts with two fixed states:**
   - **Main** (green, top-left) — non-editable, represents the entry point
   - **init** (blue, below Main) — non-editable, represents the initialization state

### Part B: Adding States

4. **User clicks "New state"** on the toolbar.
5. **A new state box appears** on the canvas with:
   - Coral color
   - An auto-generated name (e.g., "New state 1")
   - A fixed size of 110×65 pixels with rounded corners
6. **The state is draggable** — the user can reposition it anywhere on the canvas.
7. **The user can repeat** steps 4-6 to add more states. Each receives an incrementing name.

### Part C: Renaming States

8. **User single-clicks** a user-created state box (not Main or init).
9. **An inline text field appears** inside the state box.
10. **User types the new name** and clicks away or presses Enter.
11. **The name is validated** — if it is already used by another state, the rename is rejected and the old name is restored. The user must choose a unique name.

### Part D: Moving States

12. **User clicks and drags** a state box to reposition it.
13. **Connected transitions update automatically** — arrows between states follow the moved state so connections remain correct.

### Part E: Adding Transitions

14. **User clicks "New transition"** on the toolbar.
15. **User clicks a source state** (the state the transition originates from).
16. **User clicks a target state** (the state the transition leads to).
17. **An arrow is drawn** from the source to the target state with an arrowhead pointing at the target.
18. **If the same state is clicked twice**, a loop (self-transition) is created instead — a circular arrow that starts and ends at the same state.
19. **Duplicate transitions are prevented** — if a transition already exists between two states, no new arrow is created.

### Part F: Editing Transition Predicate/Alias

20. **User clicks on a transition arrow.**
21. **A floating editor appears** with two fields:
    - **Alias (optional)** — a display label for the transition
    - **Predicate** — the condition that triggers the transition
22. **User edits the fields** — changes are reflected on the arrow in real-time.
23. **The editor closes automatically** when the user moves the mouse away.

### Part G: Editing State Content

24. **User double-clicks** a state box.
25. **A text editor tab opens** in the main window, named after the state.
26. **User edits the state's body content** — this is the LISMA code that runs while the system is in this state.
27. **User closes the tab** — the content is saved to the state.

### Part H: Editing Loop Content

28. **User double-clicks** a loop arrow's arrowhead.
29. **A text editor tab opens** named "{stateName} (loop)".
30. **User edits the loop body content** — this code runs during the self-transition.
31. **User closes the tab** — the content is saved to the loop.

### Part I: Removing Elements

32. **User clicks "Remove state"** on the toolbar, then clicks a state to delete it along with all its connected arrows.
33. **User clicks "Remove transition"** on the toolbar, then clicks an arrow to delete it.

### Part J: Automatic LISMA Conversion

34. **When the statechart is verified or simulated**, the visual statechart is automatically converted to LISMA text:
    a. Main state text appears first
    b. Transitions are grouped by target state and predicate, with multiple source states merged into a single clause
    c. Loop transitions are expanded into a pair of pseudo-states to represent the loop logic
    d. Line numbers are tracked so errors can be mapped back to the generated text

## Alternative Flows

### A1: Duplicate name during rename

At step 11, if the new name is already taken, the rename is rejected and the original name is restored. The user must choose a different name.

### A2: Duplicate transition prevention

At step 19, if a transition already exists between the two selected states, the creation is silently skipped.

### A3: Main/init states can be removed

At step 32, clicking Main or init in remove mode will delete them from the canvas. This is a known limitation — these states should ideally be protected from deletion.

### A4: Mode switching

At step 32-33, toolbar buttons are toggles. Clicking any toolbar button resets all modes first, ensuring only one mode (add state, add transition, remove state, remove transition) is active at a time.

## Postcondition

- A visual statechart project is open with user-defined states, transitions, and loop transitions
- The statechart can be verified and simulated (converted to LISMA text automatically)
- State content and loop content are editable as separate text editor tabs

## Related Use Cases

- **UC-001** — Application startup (prerequisite)
- **UC-004** — Saving the statechart to disk
- **UC-005** — Verifying the converted LISMA output
- **UC-006** — Running a simulation on the statechart
