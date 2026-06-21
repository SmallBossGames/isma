# UX Specification — Blueprint / Statechart Editor

## Purpose

This document provides a detailed specification of the Blueprint (Statechart) Editor in ISMA. It covers the visual canvas, state boxes, transition arrows, loop arrows, edit popover, toolbar, interaction modes, data model, and the conversion to LISMA text. Use this as the ground truth when implementing the equivalent in Avalonia — preserve every interaction, layout rule, and data transformation.

---

## 1. Overview

### 1.1 Module Structure

```
blueprint-editor/src/main/kotlin/ru/isma/next/editor/blueprint/
├── IsmaBlueprintEditor.kt          # UI only (113 lines)
├── IsmaBlueprintViewModel.kt       # All logic (421 lines)
├── EditorMode.kt                   # Sealed class for editor modes (12 lines)
├── NameChangingMonitor.kt          # Unique name enforcement (33 lines)
├── utilities/
│   ├── ClickDisambiguator.kt       # 200ms single/double click disambiguation
│   └── JavaFxExtensions.kt         # JavaFX utility extensions
├── constants/
│   ├── BlueprintEditorConstants.kt # All magic numbers (35 lines)
│   └── StateNames.kt               # MAIN_STATE, INIT_STATE constants
├── services/
│   └── ITextEditorFactory.kt       # SPI for text editor creation
├── models/
│   ├── BlueprintModel.kt           # Serializable JSON model
│   ├── BlueprintStateModel.kt      # State data model
│   ├── BlueprintTransactionModel.kt # Inter-state transition model
│   ├── BlueprintLoopTransactionModel.kt # Loop transition model
│   └── CanvasViewModel.kt          # Observable lists + EditorTransaction/EditorLoopTransaction
├── controls/
│   ├── StateBox.kt                 # Draggable state box control
│   ├── TransactionArrow.kt         # Inter-state transition arrow
│   ├── LoopTransactionArrow.kt     # Self-loop arrow
│   ├── EditArrowPopOver.kt         # Floating edit popover
│   └── CoroutineScopeProvider.kt   # Shared CoroutineScope(Dispatchers.JavaFx)
└── utilities/
    └── ArrowGeometry.kt            # Arrow geometry calculations
```

The Blueprint Editor follows an MVVM pattern: `IsmaBlueprintEditor` is the View (UI only), `IsmaBlueprintViewModel` is the ViewModel (all logic), and `BlueprintModel` / `CanvasViewModel` are the Models.

The Blueprint Editor is a **visual finite-state machine editor**. Users create states as draggable boxes on an infinite canvas, draw transitions between them, and define transition predicates (conditions). The visual statechart is compiled into LISMA text at build time via `convertToLisma()`.

### 1.2 Container Hierarchy

```
IsmaBlueprintEditor (BorderPane) — View
├── center: TabPane
│   └── Tab "Diagram" (non-closable)
│       └── ScrollPane
│           └── Pane (canvas)
│               ├── mainStateBox (fixed, from ViewModel)
│               ├── initStateBox (fixed, from ViewModel)
│               ├── userStateBox[] (from CanvasViewModel.states)
│               ├── transactionArrow[] (from CanvasViewModel.transactions)
│               ├── loopTransactionArrow[] (from CanvasViewModel.loopTransactions)
│               └── EditArrowPopOver (floating, transient)
└── bottom: ToolBar
    ├── "New state" button → viewModel.addState()
    ├── "New transition" / "Stop adding transaction" → viewModel.toggleAddTransition()
    ├── Separator
    ├── "Remove state" / "Stop remove state" → viewModel.toggleRemoveState()
    └── "Remove transition" / "Stop remove transition" → viewModel.toggleRemoveTransition()
```

The toolbar is bound to the Diagram tab's visibility: `visibleProperty().bind(visible)` and `managedProperty().bind(visible)`. When the Diagram tab is not active, the toolbar is invisible and unmanaged.

---

## 2. Canvas

### 2.1 Properties

| Property | Value |
|----------|-------|
| Root type | `Pane` (no layout constraints) |
| Scrolling | Via `ScrollPane` wrapper |
| Scroll constraints | No min/max viewport size — scrolls to content |
| Coordinate origin | (0, 0) at top-left of canvas pane |
| Position clamping | All state positions: `max(position, 0.0)` — negative coordinates forbidden |

### 2.2 Rendering Order (z-index via `viewOrder`)

| Element | viewOrder | Layer |
|---------|-----------|-------|
| Rectangle (state body) | 3.0 | Background |
| HBox (name label + text area) | 2.0 (default) | Foreground content |
| StateBox group | 3.0 (default) | Above rectangle |
| TransactionArrow / LoopTransactionArrow | 4.0 | Transitions |
| Line (arrow shaft) | 6.0 | Arrow geometry |
| Polygon (arrowhead) | 6.0 | Arrow geometry |

Higher `viewOrder` renders on top.

---

## 3. State Boxes

### 3.1 Visual Structure

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

### 3.2 State Types

| Type | Color | Position (x, y) | Editable | Edit Button Visible | Draggable |
|------|-------|------------------|----------|---------------------|-----------|
| **Main** | `LIGHTGREEN` | (20, 10) | No | No | Yes |
| **Init** | `LIGHTBLUE` | (10, 100) | No | No | Yes |
| **User** | `CORAL` | (10, 200) | Yes | Yes | Yes |

**Main state**: name = `"Main"`, `layoutX` incremented by 10 twice (20 total from 10).
**Init state**: name = `"init"`, `layoutY` incremented by 100 (from 10 to 110 — but serialized as 10, 100 as default empty model).

> **Note:** The `BlueprintModel.empty` default serializes init at (10.0, 100.0). During construction, the init state box applies `layoutY += 10` on top of the base 10, resulting in effective position (10, 20+100=110). However, the initial `layoutYProperty().value += 100` is applied after the constructor's default `layoutY = 10`, so the visual start position is y ≈ 110. On load from a saved model, `layoutY` is set directly to `canvasPositionY = 100.0`.

### 3.3 Inline Name Editing

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

### 3.4 Double-Click Behavior

Double-clicking any state box (Main, Init, or User) opens a **text editor tab** in the main TabPane:

- Tab name: bound to the state's `nameProperty`
- Tab content: the state's `text` property (the LISMA body of that state)
- Changes in the text editor write back to `state.text` via `onTextChanged` callback
- Tab close: disposes the text editor instance

### 3.5 Drag Interaction

| Event | Handler |
|-------|---------|
| `MOUSE_PRESSED` | Sets `activeStateBox = source`, records `xOffset = -event.x`, `yOffset = -event.y`. Resets `isDragged = false`. |
| `MOUSE_DRAGGED` | Sets `isDragged = true`. |
| `MOUSE_RELEASED` | Calls `onRelease` callback (sets `activeStateBox = null`). |
| `MOUSE_DRAGGED` on canvas (global) | If `activeStateBox != null` and not in remove-mode: calls `moveStateBox(stateBox, x + xOffset, y + yOffset)` with position clamped to `max(pos, 0.0)`. |

**Drag is only active when**: primary button is down AND `activeStateBox != null` AND NOT in `isRemoveStateMode`.

### 3.6 Single-Click vs. Drag Disambiguation

The `StateBox` uses a **200ms delayed coroutine** approach:

```
MOUSE_PRESSED → reset isDragged = false, schedule 200ms check
  ↓
MOUSE_DRAGGED → set isDragged = true
  ↓
200ms elapsed → if !isDragged → trigger single-click (inline name edit)
                if isDragged  → skip single-click (drag already handled)
MOUSE_CLICKED with clickCount == 2 → cancel pending single-click, trigger double-click
```

### 3.7 Editability Bindings

User state `isEditable` is dynamically bound:

```
isEditable = !(isRemoveStateMode OR isAddTransactionMode)
```

When in add-transition or remove-state mode, inline name editing is disabled.

---

## 4. Transition Arrows (Inter-State)

### 4.1 Visual Structure

```
StateBox A ────────────► StateBox B
              [Predicate]
```

A straight line from the center of the source state to the center of the target state, with an arrowhead pointing at the target. The line is offset to avoid overlapping the state box borders.

### 4.2 Geometry Calculation

The line endpoints are computed in the `updateGeometry()` callback, triggered whenever any of `startXProperty`, `startYProperty`, `endXProperty`, `endYProperty` changes:

```
x = endX - startX    // delta X between state centers
y = endY - startY    // delta Y between state centers
angle = atan2(x, y) + PI / 2  // perpendicular angle

offsetDistance = 10.0
offsetX = offsetDistance * sin(angle)
offsetY = offsetDistance * cos(angle)

// Line endpoints (offset from state centers, in local coordinates)
lineStartX = startX - layoutX + offsetX
lineStartY = startY - layoutY + offsetY
lineEndX   = endX - layoutX + offsetX
lineEndY   = endY - layoutY + offsetY

// Arrowhead position and rotation
arrowhead.translateX = offsetX
arrowhead.translateY = offsetY
arrowhead.rotate = -angle / PI * 180.0

// Label offset (perpendicular, further out)
textOffsetX = 75.0 * sin(angle)
textOffsetY = 50.0 * cos(angle)
predicateTextWrapped.translateX = textOffsetX
predicateTextWrapped.translateY = textOffsetY
```

### 4.3 Arrowhead

- **Shape**: `Polygon(7.0, -7.0, -7.0, 0.0, 7.0, 7.0)` — a 14×14 isosceles triangle
- **Stroke width**: 3.0
- **Rotation**: dynamically rotated to match line angle
- **Click target**: the arrowhead polygon has its own `MOUSE_CLICKED` handler
  - **Single-click on arrowhead**: opens `EditArrowPopOver`
  - **Double-click on arrowhead**: opens text editor tab for the loop's content (not applicable to inter-state arrows — no text content)

### 4.4 Label Display

The label shows the arrow's alias if present, otherwise the predicate:

```
displayedText = alias.ifBlank { predicate }
```

- **Font**: Arial 16pt
- **Width**: 120px (fixed `TextFieldLength`)
- **Position**: centered, offset perpendicular from line midpoint

### 4.5 Click Handlers

| Target | Action |
|--------|--------|
| Arrow body (line area) | If in `isRemoveTransactionMode` → remove the arrow |
| Arrowhead | Single-click: open `EditArrowPopOver`; no double-click handler |
| Arrow label | No specific handler — part of the arrow group's click handling |

### 4.6 Duplication Prevention

`addTransactionArrow()` checks: `transactions.any { it.startStateBox == start && it.endStateBox == end }`. If a transition already exists between the two states, it is silently skipped.

---

## 5. Loop Transition Arrows (Self-Transitions)

### 5.1 Visual Structure

```
          ┌──────────┐
    ┌─────►│  Circle   │─────┐
    │      │ (r=40,    │     │
    │      └──────────┘     │
    │                       ▼
    └──────────► StateBox ◄──┘
```

A loop arrow draws a **circle** above the state, with an arrowhead pointing back into the state. The circle has radius 40.

### 5.2 Properties

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

### 5.3 Label Display

Same alias-or-predicate logic as inter-state arrows:

```
displayedText = alias.ifBlank { predicate }
```

### 5.4 Click Handlers

| Target | Action |
|--------|--------|
| Arrow body (circle + line) | If in `isRemoveTransactionMode` → remove the arrow |
| Arrowhead (single-click) | Open `EditArrowPopOver` |
| Arrowhead (double-click) | Open text editor tab for loop content, tab name = `"{stateName} (loop)"` |

### 5.5 Loop Content Text

Unlike inter-state transitions (which have no text content), loop arrows carry a `text` property:

- This is the LISMA body text for the loop pseudo-state
- Double-clicking the arrowhead opens a text editor tab named `"{stateName} (loop)"`
- Changes in the editor write back to `arrow.text`

### 5.6 Duplication Prevention

`addLoopTransactionArrow()` checks: `loopTransactions.any { it.stateBox == stateBox }`. Only one loop arrow per state is allowed.

---

## 6. Edit Arrow PopOver

### 6.1 Visual Structure

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

### 6.2 Data Binding

Both text fields use **bidirectional binding**:

```
TextField.textProperty() ↔ ITransactionArrowData.aliasProperty
TextField.textProperty() ↔ ITransactionArrowData.predicateProperty
```

Changes in either direction propagate immediately.

### 6.3 Dismissal

The PopOver is added to `canvas.children` and auto-removed when `MOUSE_EXITED` fires on it:

```kotlin
setOnMouseExited { canvas.children.remove(this) }
```

This creates a "click-away" behavior: moving the mouse outside the PopOver dismisses it.

### 6.4 Position Coordinate Conversion

The PopOver's x/y coordinates are converted from scene to local before use:

```kotlin
val converted = canvas.sceneToLocal(event.sceneX, event.sceneY)
val popover = createEditPopOver(arrow, converted.x, converted.y)
```

---

## 7. Toolbar

### 7.1 Layout

```
┌────────────────────────────────────────────────────────────────────┐
│ [New state] [New transition ▼] | [Remove state ▼] [Remove trans ▼]│
└────────────────────────────────────────────────────────────────────┘
```

The toolbar appears at the **bottom** of the `BorderPane`. It contains 4 buttons separated by a `Separator` between the "add" group and "remove" group.

### 7.2 Button Behaviors

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

### 7.3 Mode Reset

`viewModel.resetMode()` sets `editorMode = EditorMode.Idle`. Every toolbar button action calls `resetMode()` before setting or toggling its own mode.

---

## 8. Interaction Modes Summary

| Mode | `EditorMode` type | Arrow Body Click | Arrowhead Click | State Box Single-Click | State Box Drag |
|------|-------------------|------------------|-----------------|----------------------|----------------|
| **Default** | `EditorMode.Idle` | No effect | Open PopOver | Inline name edit | Yes |
| **Add Transition** | `EditorMode.AddTransition` | No effect | Open PopOver | Record as source/target | No |
| **Remove State** | `EditorMode.RemoveState` | No effect | Open PopOver | Remove state + arrows (protects Main/Init) | No |
| **Remove Transition** | `EditorMode.RemoveTransition` | Remove arrow | Open PopOver | Inline name edit | Yes |

---

## 9. Data Model

### 9.1 Serializable Model (JSON persistence)

**File:** `blueprint-editor/src/main/kotlin/.../models/BlueprintModel.kt`

```
BlueprintModel
├── main: BlueprintStateModel        // Main state
├── init: BlueprintStateModel        // Init state
├── states: Array<BlueprintStateModel>    // User-created states
├── transactions: Array<BlueprintTransactionModel>  // Inter-state transitions
└── loopTransactions: Array<BlueprintLoopTransactionModel>  // Self-loops

BlueprintStateModel
├── canvasPositionX: Double
├── canvasPositionY: Double
├── name: String
└── text: String

BlueprintTransactionModel
├── startStateName: String       // Resolved by name at load time
├── endStateName: String
├── predicate: String
└── alias: String = ""

BlueprintLoopTransactionModel
├── stateName: String
├── predicate: String
├── alias: String = ""
└── text: String                 // Loop body content
```

**Constants file:** `blueprint-editor/src/main/kotlin/.../constants/StateNames.kt`

```kotlin
const val MAIN_STATE = "Main"
const val INIT_STATE = "init"
```

**Editor-only data classes (not serializable):**
- `BlueprintEditorTransactionModel` — `blueprint-editor/src/main/kotlin/.../models/BlueprintEditorTransactionModel.kt`
- `BlueprintEditorLoopTransactionModel` — `blueprint-editor/src/main/kotlin/.../models/BlueprintEditorLoopTransactionModel.kt`

### 9.2 Default Empty Model

```kotlin
BlueprintModel(
    main      = BlueprintStateModel(10.0, 10.0, "Main", ""),
    init      = BlueprintStateModel(10.0, 100.0, "init", ""),
    states    = emptyArray(),
    transactions = emptyArray(),
    loopTransactions = emptyArray()
)
```

### 9.3 Editor-Only Data Classes (runtime, not serializable)

These are now inner data classes of `CanvasViewModel` (in `models/CanvasViewModel.kt`):

```
CanvasViewModel.EditorTransaction
├── startBox: StateBox          // Direct reference to canvas node
├── endBox: StateBox
└── arrow: TransactionArrow

CanvasViewModel.EditorLoopTransaction
├── stateBox: StateBox
└── arrow: LoopTransactionArrow
```

The old separate files `BlueprintEditorTransactionModel.kt` and `BlueprintEditorLoopTransactionModel.kt` no longer exist.

### 9.4 Data Flow: Save (Canvas → Model → JSON)

1. `getBlueprintModel()` is called on `IsmaBlueprintEditor`
2. Each `StateBox` is converted via `toBlueprintState()`: extracts `layoutX`, `layoutY`, `name`, `text`
3. Each `BlueprintEditorTransactionModel` is converted via `toBlueprintTransaction()`: extracts state **names** (not references), predicate, alias
4. Each `BlueprintEditorLoopTransactionModel` is converted via `toBlueprintLoopTransaction()`: extracts state name, predicate, alias, text
5. A `BlueprintModel` is assembled and returned
6. `BlueprintProjectModel.snapshot()` calls `convertToLisma()` on the model at compile time

### 9.5 Data Flow: Load (JSON → Model → Canvas)

1. `setBlueprintModel(model)` is called
2. All existing user state boxes are removed from the canvas
3. Main and Init state data is applied onto the fixed state boxes
4. New `StateBox` instances are created from `model.states` via `instantiateStateBoxFromBlueprintState`
5. A name→StateBox map is built including Main, Init, and new states
6. For each `BlueprintTransactionModel`, the start/end states are looked up by name and a `TransactionArrow` is created with the stored predicate and alias
7. For each `BlueprintLoopTransactionModel`, a `LoopTransactionArrow` is created
8. All arrows bind their geometry to the state box centers

---

## 10. Name Changing Monitor

### 10.1 Purpose

Ensures all state names are unique within the blueprint. Tracks registered names and auto-increments default name counters. Takes `itemDefaultName` as constructor parameter.

### 10.2 Algorithm

**File:** `NameChangingMonitor.kt` (33 lines)

```
itemDefaultName: String          // e.g. "New state"
defaultNameRegex: Regex          // "^New state (\\d+)$"
existedNames: HashSet<String>
nextNameCounter: Int = 1
```

**`tryRegister(name)`:**
1. If `existedNames` contains `name` → return `false` (duplicate)
2. If `name` matches `defaultNameRegex`, extract the digit → set `nextNameCounter = max(nextNameCounter, digit + 1)`
3. Add `name` to `existedNames` → return `true`

**`tryUnregister(name)`:**
1. If `existedNames` contains `name` → remove it → return `true`
2. Otherwise → return `false`

**`createNextDefaultName()`:**
1. Return `"$itemDefaultName $nextNameCounter"`

### 10.3 Name Edit Rollback

When a user edits a state name via inline editing (bound to `isEditModeEnabledProperty` in `IsmaBlueprintViewModel.initNameChangingEvent()`):
1. The current name is saved as `previousName` when `isEditModeEnabled` becomes `true`
2. On focus loss (`isEditModeEnabled` becomes `false`), `tryRegister(newName)` is called
3. If registration fails (duplicate), `name = previousName` restores the old name
4. If registration succeeds, `tryUnregister(previousName)` updates the registry

---

## 11. LISMA Conversion

### 11.1 Overview

`BlueprintModel.convertToLisma()` (in app module) transforms the visual statechart into LISMA text. This runs at compile/snapshot time, not during editing.

### 11.2 Output Format — Regular Transactions

Each group of transitions targeting the same state with the same predicate produces a single `StateBlock`:

```
state "key" {
    <state text>
} from <startState1>,<startState2>,...;
```

Where `key` = `"{targetStateName} ({predicate})"` (trimmed).

Multiple transitions from different states to the same target state with the same predicate are **merged** into a single `from` clause: `from StateA,StateB,StateC;`

### 11.3 Output Format — Loop Transactions

Each loop transaction is expanded into **two pseudo-states**:

```
state <stateName>_pseudo_1 (<predicate>) {
    <loop text>
} from <stateName>;

state <stateName> (1 > 0) {
    <original state text>
} from <stateName>_pseudo_1;
```

The original state's predicate is replaced with `1 > 0` (always true), and a new pseudo-state is inserted between the state and itself to represent the loop.

### 11.4 Output Order

1. Main state text (first, as top-level content)
2. All state blocks from regular transactions (grouped by target + predicate)
3. All loop transaction expansions (one pseudo-state pair per loop)

Each section is followed by a blank line.

### 11.5 Line Number Tracking

For each generated fragment, start and end line numbers are tracked and returned in `CodeRegion` objects, used for error highlighting in the text editor.

### 11.6 Constants

**File:** `BlueprintEditorConstants.kt` (35 lines)

All magic numbers are centralized:

| Constant | Value | Used For |
|----------|-------|----------|
| `DEFAULT_STATE_WIDTH` | 110.0 | User state box width |
| `DEFAULT_STATE_HEIGHT` | 65.0 | User state box height |
| `FIXED_STATE_HEIGHT` | 60.0 | Main/Init state box height |
| `CORNER_RADIUS` | 20.0 | State box corner rounding |
| `STATE_NAME_FONT_SIZE` | 16.0 | State name text font |
| `STATE_INSET` | 10.0 | Main/Init x-position offset |
| `ARROW_LINE_OFFSET` | 10.0 | Line perpendicular offset from state center |
| `ARROW_TEXT_X_OFFSET` | 75.0 | Arrow text X offset |
| `ARROW_TEXT_Y_OFFSET` | 50.0 | Arrow text Y offset |
| `ARROW_LINE_STROKE` | 3.0 | Arrow line stroke width |
| `ARROWHEAD_STROKE` | 3.0 | Arrowhead stroke width |
| `ARROWHEAD_WIDTH` | 7.0 | Arrowhead polygon half-width |
| `ARROW_LABEL_FONT_SIZE` | 16.0 | Arrow label text font |
| `ARROW_LABEL_FIELD_WIDTH` | 120.0 | Arrow label text field width |
| `LOOP_CIRCLE_RADIUS` | 40.0 | Loop arrow circle radius |
| `LOOP_CIRCLE_CENTER_X` | 60.0 | Loop circle center X |
| `LOOP_ARROWHEAD_X` | 100.0 | Loop arrowhead position |
| `LOOP_LABEL_X` | 120.0 | Loop label X position |
| `LOOP_LABEL_Y_OFFSET` | -10.0 | Loop label Y offset |
| `CLICK_DELAY_MS` | 200L | Single/double click disambiguation delay |
| `POPOVER_MIN_WIDTH` | 300.0 | Edit PopOver minimum width |
| `POPOVER_PADDING` | 10.0 | Edit PopOver padding |
| `POPOVER_CORNER_RADIUS` | 5.0 | Edit PopOver corner radius |
| `POPOVER_SHADOW_RADIUS` | 20.0 | Edit PopOver shadow radius |

---

## 12. Canvas Coordinate System

### 12.1 Absolute Positioning

All elements use absolute `layoutX` and `layoutY` on the canvas `Pane`. There is no layout manager, no grid snapping, and no snapping to other elements.

### 12.2 Arrow Geometry Bindings

| Arrow Type | Layout X Binding | Layout Y Binding |
|------------|-----------------|-----------------|
| TransactionArrow | `(endX - startX) / 2 + startX` | `(endY - startY) / 2 + startY` |
| LoopTransactionArrow | `stateBox.centerXProperty()` | `stateBox.centerYProperty()` |

### 12.3 State Center Calculation

```
centerX = layoutX + squareWidth / 2   (= layoutX + 55)
centerY = layoutY + squareHeight / 2  (= layoutY + 32.5 for user, 30 for Main/Init)
```

These are exposed as `DoubleBinding` objects that update automatically when `layoutX`, `layoutY`, `squareWidth`, or `squareHeight` changes.

---

## 13. Text Editor Integration

### 13.1 Opening State Text Editor Tabs

Double-clicking a state box triggers `openStateTextEditorTab(state)`:

```
1. editorFactory.createTextEditor(state.text, onTextChanged = { state.text = it })
2. tabs.tabs.add(Tab(state.name, editor))
3. Tab.textProperty() binds to state.nameProperty
4. Tab.setOnCloseRequest { editorFactory.disposeInstance(editor) }
```

### 13.2 Opening Loop Content Editor Tabs

Double-clicking a loop arrow's arrowhead triggers `openStateTextEditorTab(arrow, stateBox)`:

```
1. editorFactory.createTextEditor(arrow.text, onTextChanged = { arrow.text = it })
2. tabs.tabs.add(Tab("${stateBox.name} (loop)", editor))
3. Tab.textProperty() binds to stateBox.nameProperty.concat(" (loop)")
4. Tab.setOnCloseRequest { editorFactory.disposeInstance(editor) }
```

### 13.3 Text Editor Factory SPI

`ITextEditorFactory` is an interface injected via Koin:

```kotlin
interface ITextEditorFactory {
    fun createTextEditor(text: String, onTextChanged: (String) -> Unit): Node
    fun disposeInstance(node: Node)
}
```

The implementation (in app module) wraps `IsmaTextEditor` instances and provides per-project Koin scopes. Each blueprint project gets its own factory and editor pool.

---

## 14. Project Lifecycle

### 14.1 Blueprint Project Creation

1. User clicks "New statechart" toolbar button or uses File → New Statechart (Ctrl+B)
2. `ProjectService.createNewBlueprint("New statechart")` is called
3. A new `BlueprintProjectModel` is created with `BlueprintModel.empty`
4. A Koin scope is created for this project
5. The `IsmaBlueprintEditor` is instantiated within the scope
6. The project is added to `ProjectService.projects`
7. A new tab opens in the main TabPane showing the editor

### 14.2 Blueprint Project Save

1. User clicks Save (Ctrl+S) or Save All
2. `project.blueprint` triggers `fetchBlueprint()` → `dataProvider.blueprint` → `blueprintEditor.getBlueprintModel()`
3. The `BlueprintModel` is serialized to JSON via `kotlinx.serialization`
4. JSON is written to the `.scisma` file

### 14.3 Blueprint Project Load

1. User opens a `.scisma` file (File → Open, filter `*.scisma`)
2. JSON is parsed into `BlueprintModel`
3. `project.blueprint = model` triggers `pushBlueprint()` → `dataProvider.blueprint = model` → `blueprintEditor.setBlueprintModel(model)`
4. `setBlueprintModel` rebuilds the canvas from the model

### 14.4 Snapshot (Compile)

1. At compile/verification time, `project.snapshot()` is called
2. For blueprint projects, this calls `blueprint.convertToLisma()`
3. The result is a `LismaTextModel` containing the generated LISMA text and `CodeRegion` mappings

---

## 15. Integration with Main App Shell

### 15.1 Tab Display

The `IsmaBlueprintEditor` is injected as `Node` with qualifier `IsmaEditorQualifier` into the project model. The main app's `ProjectTab` wraps this node in a `Tab`:

```
Tab {
    text = project.nameProperty
    content = project.editor (IsmaBlueprintEditor instance)
    closable = true
}
```

### 15.2 Tab Rename

When a state name changes, the tab text updates automatically because:
- The tab's text is bound to the project's name (set at project creation time)
- State name changes do NOT affect the tab title — they only affect the state box label

### 15.3 Multiple Project Types Coexistence

The main TabPane can contain tabs from both LISMA text projects and Blueprint projects. The TabPane has no type restriction.

---

## 16. Known Limitations (to replicate or improve)

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

---

## 17. Color Palette

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

---

## 18. Dimensions Reference

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

---

## 19. Avalonia Migration Mapping

### 19.1 Component Mapping

| JavaFX | Avalonia | Notes |
|--------|----------|-------|
| `BorderPane` | `Grid` (3-row layout) or `BorderPane` via custom control | Canvas in center, toolbar at bottom |
| `Pane` (canvas) | `Panel` (custom) | Override `OnRender` for direct drawing, or use `Canvas` control |
| `ScrollPane` | `ScrollViewer` within `Border` or custom `Panel` with `ClipToBounds` | |
| `TabPane` | `TabControl` | |
| `Tab` | `Tab` | |
| `Group` | `Canvas` or `Panel` | Container for child shapes |
| `Rectangle` (rounded) | `Rectangel` with `CornerRadius` or `PathGeometry` | `arcWidth=20, arcHeight=20` |
| `Label` | `TextBlock` | |
| `TextArea` | `TextBox` (multi-line) | For inline name editing |
| `TextField` | `TextBox` (single-line) | For PopOver inputs |
| `Line` | `Path` (LineGeometry) or `Shape` | For arrow shafts |
| `Polygon` | `Path` (PathGeometry with PolygonGeometry) | For arrowheads |
| `Circle` | `Ellipse` (width=height=diameter) | For loop arrows |
| `VBox` | `StackPanel` or `Panel` (custom) | For PopOver layout |
| `ToolBar` | `StackPanel` with `Orientation=Horizontal` | Bottom toolbar |
| `Separator` | `Border` (1px line) or `Separator` control | |
| `DropShadow` | `BoxShadow` or `Effect` on `Border` | |

### 19.2 Interaction Mapping

| JavaFX | Avalonia | Notes |
|--------|----------|-------|
| `MouseEvent.MOUSE_PRESSED` | `PointerPressed` | |
| `MouseEvent.MOUSE_DRAGGED` | `PointerMoved` (with button check) or custom drag | |
| `MouseEvent.MOUSE_RELEASED` | `PointerReleased` | |
| `MouseEvent.MOUSE_CLICKED` | `PointerPressed` + `PointerReleased` combo | 200ms delay for single/double disambiguation |
| `SimpleBooleanProperty` | `StyledProperty` or `DirectProperty<bool>` | For `isEditModeEnabled`, `isEditable`, etc. |
| `SimpleStringProperty` | `DirectProperty<string>` | For `name`, `text`, `alias`, `predicate` |
| `SimpleDoubleProperty` | `DirectProperty<double>` | For positions, dimensions |
| `DoubleBinding` (centerX/centerY) | `Binding` via `AvaloniaPropertyChangedCallback` or `ObservableAsPropertyHelper` | Reactive center calculation |
| `bindBidirectional` | `Binding` with `TwoWay` mode | For PopOver text fields |
| `ObservableValue.onChange` | `Observable.Bindable` or `AvaloniaPropertyChangedEventHandler` | Property change listeners |
| `CoroutineScope(Dispatchers.JavaFx)` | `DispatcherTimer` or `Dispatcher.UIThread.Post` with `Task.Delay` | 200ms single-click delay |
| `LayoutBounds` binding | `Avalonia.Layout` or manual layout override | |

### 19.3 Rendering Strategy

**Recommended approach: Custom `Panel` with `Draw` override**

Instead of using individual shape controls, render the entire canvas in a single `Panel.OnRender` method for performance and control:

```csharp
protected override void OnRender(DrawingContext dc)
{
    // 1. Render all StateBoxes (rounded rectangles + text)
    foreach (var state in StateBoxes)
    {
        dc.DrawRoundedRectangle(state.Fill, state.Pen, new Rect(state.X, state.Y, state.Width, state.Height), 20, 20);
        dc.DrawText(state.Font, new Point(state.X + 10, state.Y + 10), state.Name);
    }

    // 2. Render all TransactionArrows
    foreach (var arrow in Transactions)
    {
        var (start, end) = arrow.CalculateGeometry();
        dc.DrawLine(new Pen(arrow.Pen, 3), start, end);
        dc.DrawPolygon(arrow.Arrowhead);
        dc.DrawText(arrow.Font, arrow.LabelPosition, arrow.LabelText);
    }

    // 3. Render all LoopTransactionArrows
    foreach (var loop in Loops)
    {
        var circle = new EllipseGeometry(new Rect(loop.X - 40, loop.Y - 40, 80, 80));
        dc.DrawDrawing(new GeometryDrawing(Brushes.Transparent, new Pen(Brushes.Black, 3), circle));
        // arrowhead + label...
    }
}
```

This avoids the overhead of maintaining hundreds of child elements in the visual tree and gives direct control over draw order (which maps to `viewOrder`).

### 19.4 Data Persistence

| Aspect | JavaFX | Avalonia |
|--------|--------|----------|
| Serialization | `kotlinx.serialization` (JSON) | `System.Text.Json` or `Newtonsoft.Json` |
| File extension | `.scisma` | `.scisma` (unchanged) |
| Model classes | `@Serializable` data classes | `[JsonSerializable]` or `[JsonObject]` POCOs |

### 19.5 Text Editor Integration

The `ITextEditorFactory` SPI maps to an interface that creates `AvaloniaEdit` (`ICSharpCode.AvalonEdit`) text view instances:

```csharp
public interface ITextEditorFactory
{
    TextEditor CreateTextEditor(string text, Action<string> onTextChanged);
    void DisposeInstance(TextEditor editor);
}
```

AvaloniaEdit provides syntax highlighting, indentation, and line numbers out of the box. The existing `LismaHighlightingDefinition` (if any) should be ported from JavaFX's `RichTextFX` highlighting to AvaloniaEdit's `ISyntaxHighlighting`.
