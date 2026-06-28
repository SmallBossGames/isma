# Blueprint Editor — UX Specification

## State Boxes

### Visual Structure

```
┌────────────────────────────────────┐  ← rounded rectangle, arc = 20px
│                                    │
│        [ State Name Label ]        │  ← Arial 16pt, centered
│                                    │
└────────────────────────────────────┘
```

| Property | Default | Notes |
|----------|---------|-------|
| `squareWidth` | 110 | Fixed for user states; Main/Init override to same |
| `squareHeight` | 65 | Main and Init states set this to **60** |
| `arcWidth` / `arcHeight` | 20 | Corner rounding |
| Font | Arial 16pt | State name text |

### State Types

| Type | Color | Position (x, y) | Editable | Edit Button Visible | Draggable |
|------|-------|------------------|----------|---------------------|-----------|
| **Main** | `LIGHTGREEN` | (20, 10) | No | No | Yes |
| **Init** | `LIGHTBLUE` | (10, 100) | No | No | Yes |
| **User** | `CORAL` | (10, 200) | Yes | Yes | Yes |

**Main state**: name = `"Main"`, `layoutX` incremented by 10 twice (20 total from 10).
**Init state**: name = `"init"`, `layoutY` incremented by 100 (from 10 to 110 — but serialized as 10, 100 as default empty model).

> **Note:** The `BlueprintModel.empty` default serializes init at (10.0, 100.0). During construction, the init state box applies `layoutY += 10` on top of the base 10, resulting in effective position (10, 20+100=110). However, the initial `layoutYProperty().value += 100` is applied after the constructor's default `layoutY = 10`, so the visual start position is y ≈ 110. On load from a saved model, `layoutY` is set directly to `canvasPositionY = 100.0`.

### Inline Name Editing

When a user single-clicks an editable (user) state box:

1. A 200ms delay begins (coroutine)
2. If the mouse was **not** dragged during that period:
   - A `TextArea` appears inside the state box (replacing the `Label`)
   - The text area is populated with the current `name`
   - Focus is requested on the text area
3. When the text area loses focus:
   - The `name` property is updated from the text area's content
   - The `TextArea` hides, the `Label` reappears
4. The new name is validated against `NameChangingMonitor`:
   - If the name is **already taken**, the old name is **restored**
   - If unique, the monitor updates its registry

### Double-Click Behavior

Double-clicking any state box (Main, Init, or User) opens a **text editor tab** in the main TabPane:

- Tab name: bound to the state's `nameProperty`
- Tab content: the state's `text` property (the LISMA body of that state)
- Changes in the text editor write back to `state.text` via `onTextChanged` callback
- Tab close: disposes the text editor instance

### Drag Interaction

| Event | Handler |
|-------|---------|
| `MOUSE_PRESSED` | Sets `activeStateBox = source`, records `xOffset = -event.x`, `yOffset = -event.y`. Resets `isDragged = false`. |
| `MOUSE_DRAGGED` | Sets `isDragged = true`. |
| `MOUSE_RELEASED` | Calls `onRelease` callback (sets `activeStateBox = null`). |
| `MOUSE_DRAGGED` on canvas (global) | If `activeStateBox != null` and not in remove-mode: calls `moveStateBox(stateBox, x + xOffset, y + yOffset)` with position clamped to `max(pos, 0.0)`. |

**Drag is only active when**: primary button is down AND `activeStateBox != null` AND NOT in `isRemoveStateMode`.

### Editability Bindings

User state `isEditable` is dynamically bound:

```
isEditable = !(isRemoveStateMode OR isAddTransactionMode)
```

When in add-transition or remove-state mode, inline name editing is disabled.

## Transition Arrows (Inter-State)

### Visual Structure

```
StateBox A ────────────► StateBox B
              [Predicate]
```

A straight line from the center of the source state to the center of the target state, with an arrowhead pointing at the target. The line is offset to avoid overlapping the state box borders.

### Arrowhead

- **Shape**: `Polygon(7.0, -7.0, -7.0, 0.0, 7.0, 7.0)` — a 14×14 isosceles triangle
- **Stroke width**: 3.0
- **Rotation**: dynamically rotated to match line angle
- **Click target**: the arrowhead polygon has its own `MOUSE_CLICKED` handler
  - **Single-click on arrowhead**: opens `EditArrowPopOver`
  - **Double-click on arrowhead**: no handler (inter-state arrows have no text content)

### Label Display

The label shows the arrow's alias if present, otherwise the predicate:

```
displayedText = alias.ifBlank { predicate }
```

- **Font**: Arial 16pt
- **Width**: 120px (fixed `TextFieldLength`)
- **Position**: centered, offset perpendicular from line midpoint

### Click Handlers

| Target | Action |
|--------|--------|
| Arrow body (line area) | If in `isRemoveTransactionMode` → remove the arrow |
| Arrowhead | Single-click: open `EditArrowPopOver`; no double-click handler |
| Arrow label | No specific handler — part of the arrow group's click handling |

### Duplication Prevention

`addTransactionArrow()` checks: `transactions.any { it.startStateBox == start && it.endStateBox == end }`. If a transition already exists between the two states, it is silently skipped.

## Loop Transition Arrows

### Visual Structure

```
          ┌──────────┐
    ┌─────►│  Circle   │─────┐
    │      │ (r=40,    │     │
    │      └──────────┘     │
    │                       ▼
    └──────────► StateBox ◄──┘
```

A loop arrow draws a **circle** above the state, with an arrowhead pointing back into the state. The circle has radius 40.

### Properties

| Property | Default | Notes |
|----------|---------|-------|
| Circle radius | 40 | Transparent fill, black stroke, strokeWidth 3 |
| Circle center X | 60 | Offset within the arrow's local coordinate space |
| Arrowhead X position | 100 | At the right side of the circle |
| Arrowhead shape | `Polygon(0,-7 / 7,0 / -7,0)` | Points right |
| Arrowhead stroke | 3 | |
| Label X position | 120 | To the right of the circle |
| Label Y offset | -10 | Slightly above center line |
| Layout X | Bound to `stateBox.centerXProperty()` | Centered horizontally on the state |
| Layout Y | Bound to `stateBox.centerYProperty()` | Centered vertically on the state |

### Label Display

Same alias-or-predicate logic as inter-state arrows:

```
displayedText = alias.ifBlank { predicate }
```

### Click Handlers

| Target | Action |
|--------|--------|
| Arrow body (circle + line) | If in `isRemoveTransactionMode` → remove the arrow |
| Arrowhead (single-click) | Open `EditArrowPopOver` |
| Arrowhead (double-click) | Open text editor tab for loop content, tab name = `"{stateName} (loop)"` |

### Loop Content Text

Unlike inter-state transitions (which have no text content), loop arrows carry a `text` property:

- This is the LISMA body text for the loop pseudo-state
- Double-clicking the arrowhead opens a text editor tab named `"{stateName} (loop)"`
- Changes in the editor write back to `arrow.text`

### Duplication Prevention

`addLoopTransactionArrow()` checks: `loopTransactions.any { it.stateBox == stateBox }`. Only one loop arrow per state is allowed.

## Edit Arrow PopOver

### Visual Structure

```
┌──────────────────────────────────────────────────┐
│  Alias (optional)                                │
│  ┌────────────────────────────────────────────┐  │  ← TextField, minWidth 300
│  └────────────────────────────────────────────┘  │
│  Predicate                                       │
│  ┌────────────────────────────────────────────┐  │  ← TextField, minWidth 300
│  └────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────┘
```

| Property | Value |
|----------|-------|
| Layout | `VBox` |
| Min width | 300 |
| Padding | 10 (all sides) |
| Background | White fill, CornerRadii 5 |
| Effect | DropShadow, radius 20, color LIGHTGRAY |
| Horizontal position | `translateX = width / -2 + x` (centered on click x) |
| Vertical position | `translateY = y - 2` (2px above cursor) |

### Data Binding

Both text fields use **bidirectional binding**:

```
TextField.textProperty() ↔ ITransactionArrowData.aliasProperty
TextField.textProperty() ↔ ITransactionArrowData.predicateProperty
```

Changes in either direction propagate immediately.

### Dismissal

The PopOver is added to `canvas.children` and auto-removed when `MOUSE_EXITED` fires on it (defined in `EditArrowPopOver.kt`). This creates a "click-away" behavior: moving the mouse outside the PopOver dismisses it.

### Position Coordinate Conversion

The PopOver's x/y coordinates are converted from scene to local before use (defined in `EditArrowPopOver.kt`).

## Toolbar

### Layout

```
┌────────────────────────────────────────────────────────────────────┐
│ [New state] [New transition ▼] | [Remove state ▼] [Remove trans ▼]│
└────────────────────────────────────────────────────────────────────┘
```

The toolbar appears at the **bottom** of the `BorderPane`. It contains 4 buttons separated by a `Separator` between the "add" group and "remove" group.

### Button Behaviors

#### "New state"

| Action | Effect |
|--------|--------|
| Click | 1. `viewModel.resetMode()` |
| | 2. `viewModel.addState()` — creates new `StateBox` at (10, 200) |
| | 3. Auto-generate name via `NameChangingMonitor.createNextDefaultName()` |
| | 4. Register name with monitor |
| | 5. Add to `CanvasViewModel.states` |

**New state properties**:
- Color: `CORAL`
- Name: `"New state N"` where N is the next available integer
- Position: layoutX=10, layoutY=200
- Editable: yes (bound to `editorModeProperty.map { it.isNotEditingMode() }`)
- Edit button visible: yes
- Initial text: `""`

#### "New transition" / "Stop adding transaction"

Toggle button. Text changes based on `EditorMode`:

| Mode | Text |
|------|------|
| `EditorMode.Idle` | "New transition" |
| `EditorMode.AddTransition` | "Stop adding transaction" |

**When turned ON**:
1. `viewModel.toggleAddTransition()` → `resetMode()` then `editorMode = EditorMode.AddTransition(mutableListOf())`
2. First click on a state → adds to `selectedStates` list
3. Second click on a state → if same state → `addLoopArrow()`, else → `addTransactionArrow()`
4. Mode auto-resets to `Idle` after creating the transition

**During add-transaction mode**:
- User states become non-editable (`isEditable` bound to `editorMode.isNotEditingMode()`)
- Clicking states triggers `recordTransitionSource()` (not name editing)

#### "Remove state" / "Stop remove state"

Toggle button.

| Mode | Text |
|------|------|
| `EditorMode.Idle` | "Remove state" |
| `EditorMode.RemoveState` | "Stop remove state" |

**When turned ON**: `viewModel.toggleRemoveState()` → sets `editorMode = EditorMode.RemoveState`

**When turned OFF**: `viewModel.resetMode()` → sets `editorMode = EditorMode.Idle`

**During remove-state mode**:
- Clicking any state box triggers `onClick` handler in `IsmaBlueprintViewModel`
- `removeState(box)` checks `box.name == MAIN_STATE || box.name == INIT_STATE` → returns early (Main/Init protected)
- Otherwise: `canvasViewModel.removeState(box)` removes state + all associated transactions/loops

**`CanvasViewModel.removeState(box)`**:
1. Removes box from `_states`
2. Removes all transactions where `startBox == box || endBox == box`
3. Removes all loop transactions where `stateBox == box`

#### "Remove transition" / "Stop remove transition"

Toggle button.

| Mode | Text |
|------|------|
| `EditorMode.Idle` | "Remove transition" |
| `EditorMode.RemoveTransition` | "Stop remove transition" |

**When ON**: clicking the body of any arrow (inter-state or loop) removes it via `canvasViewModel.removeTransaction()` or `canvasViewModel.removeLoopTransaction()`.

**When ON**: clicking an arrowhead does **not** open the PopOver — the arrow body click handler fires first.

### Mode Reset

`viewModel.resetMode()` sets `editorMode = EditorMode.Idle`. Every toolbar button action calls `resetMode()` before setting or toggling its own mode.

## Interaction Modes Summary

| Mode | `EditorMode` type | Arrow Body Click | Arrowhead Click | State Box Single-Click | State Box Drag |
|------|-------------------|------------------|-----------------|----------------------|----------------|
| **Default** | `EditorMode.Idle` | No effect | Open PopOver | Inline name edit | Yes |
| **Add Transition** | `EditorMode.AddTransition` | No effect | Open PopOver | Record as source/target | No |
| **Remove State** | `EditorMode.RemoveState` | No effect | Open PopOver | Remove state + arrows (protects Main/Init) | No |
| **Remove Transition** | `EditorMode.RemoveTransition` | Remove arrow | Open PopOver | Inline name edit | Yes |

## Known Limitations

| Limitation | Description |
|------------|-------------|
| **No snap-to-grid** | States can be placed at arbitrary pixel coordinates |
| **No auto-routing** | Arrows are straight lines from center to center, may pass through other states |
| **No zoom/pan** | Scrolling only; no magnification |
| **No undo/redo** | All edits are permanent once committed |
| **No arrow label editing** | Only via PopOver; no inline editing on canvas |
| **No color customization** | All colors are hardcoded (LIGHTGREEN, LIGHTBLUE, CORAL, WHITE, BLACK) |
| **No keyboard shortcuts** | The blueprint editor itself has no keyboard shortcuts; all navigation is mouse-driven |
| **No context menu** | Right-click has no handler |
| **PopOver click-away** | Moving mouse out of the PopOver dismisses it — may accidentally dismiss if cursor slips |
| **No duplicate arrow prevention for loops** | Only checks by state box reference, not by state name — could theoretically create duplicates if state boxes are somehow duplicated |
| **No arrow snapping** | Arrow endpoints don't snap to state borders — they connect to center points |
| **No multi-select** | No ability to select and move multiple states simultaneously |

## Color Palette

| Element | Color | JavaFX Name |
|---------|-------|-------------|
| Main state | #90EE90 | `LIGHTGREEN` |
| Init state | #ADD8E6 | `LIGHTBLUE` |
| User states | #F08080 | `CORAL` |
| Arrow lines | #000000 | `BLACK` |
| Arrowhead | #000000 | `BLACK` |
| PopOver background | #FFFFFF | `WHITE` |
| PopOver shadow | #D3D3D3 | `LIGHTGRAY` |
| PopOver arrow text | #000000 | Default (black) |

## Dimensions Reference

All constants are defined in `BlueprintEditorConstants.kt`.

| Element | Width | Height | Notes |
|---------|-------|--------|-------|
| User state box | 110 | 65 | `DEFAULT_STATE_WIDTH` / `DEFAULT_STATE_HEIGHT` |
| Main/Init state box | 110 | 60 | `DEFAULT_STATE_WIDTH` / `FIXED_STATE_HEIGHT` |
| State box corner radius | 20 (arc) | 20 (arc) | `CORNER_RADIUS` |
| State name font size | 16 | — | `STATE_NAME_FONT_SIZE` |
| Arrow label font size | 16 | — | `ARROW_LABEL_FONT_SIZE` |
| Arrow line stroke width | 3 | — | `ARROW_LINE_STROKE` |
| Arrowhead stroke width | 3 | — | `ARROWHEAD_STROKE` |
| Loop circle radius | 40 | 40 | `LOOP_CIRCLE_RADIUS` |
| Loop circle center offset | 60 | — | `LOOP_CIRCLE_CENTER_X` |
| Arrowhead polygon width | 14 | 7 | `ARROWHEAD_WIDTH` = 7 |
| Loop arrowhead X pos | 100 | — | `LOOP_ARROWHEAD_X` |
| Loop label X pos | 120 | — | `LOOP_LABEL_X` |
| PopOver min width | 300 | — | `POPOVER_MIN_WIDTH` |
| PopOver padding | 10 | — | `POPOVER_PADDING` |
| PopOver corner radius | 5 | — | `POPOVER_CORNER_RADIUS` |
| PopOver shadow radius | 20 | — | `POPOVER_SHADOW_RADIUS` |
| Line perpendicular offset | 10 | — | `ARROW_LINE_OFFSET` |
| Arrow text X offset | 75 | — | `ARROW_TEXT_X_OFFSET` |
| Arrow text Y offset | 50 | — | `ARROW_TEXT_Y_OFFSET` |
| Loop label Y offset | -10 | — | `LOOP_LABEL_Y_OFFSET` |
| Name label inset | 10, 10 | — | `STATE_INSET` |
| HBox size | stateWidth - 20 | stateHeight - 20 | Centered inside rectangle |
