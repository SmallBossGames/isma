# Blueprint Editor — Algorithms

## NameChangingMonitor

### Purpose

Ensures all state names are unique within the blueprint. Tracks registered names in a `HashSet` and auto-increments default name counters. Source: `NameChangingMonitor.kt` (33 lines).

### Fields

| Field | Type | Purpose |
|-------|------|---------|
| `itemDefaultName` | String | Base name prefix (e.g. "New state") |
| `defaultNameRegex` | Regex | Pattern to extract digits from default names: `^${itemDefaultName} (\d+)$` |
| `existedNames` | HashSet<String> | Registry of all registered names |
| `nextNameCounter` | Int | Auto-increment counter (starts at 1) |

### `tryRegister(name)`

1. If `existedNames` contains `name` → return `false` (duplicate)
2. If `name` matches `defaultNameRegex`, extract the digit → set `nextNameCounter = max(nextNameCounter, digit + 1)`
3. Add `name` to `existedNames` → return `true`

**Counter advancement**: When registering a name like "New state 5", the counter advances to at least 6, even if names 1–4 were never registered. This prevents generating names that are already in use.

### `tryUnregister(name)`

1. If `existedNames` contains `name` → remove it → return `true`
2. Otherwise → return `false`

Unregistering does **not** adjust `nextNameCounter` — the counter only moves forward.

### `createNextDefaultName()`

Returns `"$itemDefaultName $nextNameCounter"` and does not advance the counter (advancement happens on the next `tryRegister` call).

### Name Edit Rollback

When a user edits a state name via inline editing (bound to `isEditModeEnabledProperty` in `IsmaBlueprintViewModel.initNameChangingEvent()`):

1. The current name is saved as `previousName` when `isEditModeEnabled` becomes `true`
2. On focus loss (`isEditModeEnabled` becomes `false`), `tryRegister(newName)` is called
3. If registration fails (duplicate), `name = previousName` restores the old name
4. If registration succeeds, `tryUnregister(previousName)` updates the registry

The rollback is silent — no error dialog is shown. The name simply reverts to its previous value.

## ArrowGeometry

### atan2-Based Perpendicular Offset

The line endpoints are computed in the `updateGeometry()` callback, triggered whenever any of `startXProperty`, `startYProperty`, `endXProperty`, `endYProperty` changes.

The algorithm offsets the arrow line perpendicular to the direction between state centers, avoiding overlap with the state box borders. The function `calculateArrowGeometry(startX, startY, endX, endY, layoutX, layoutY, lineOffset = 10.0, textXOffset = 75.0, textYOffset = 50.0)` returns an `ArrowGeometry` object. See `ArrowGeometry.kt` for the full implementation.

### Calculation Steps

1. `dx = endX - startX` (delta X between state centers), `dy = endY - startY` (delta Y between state centers)
2. `angle = atan2(dx, dy) + PI / 2` (perpendicular angle with swapped args + 90 degrees)
3. `offsetDistance = 10.0`, `offsetX = offsetDistance * sin(angle)`, `offsetY = offsetDistance * cos(angle)`
4. Line endpoints offset from state centers and converted to local coordinates: `lineStartX = startX - layoutX + offsetX`, `lineStartY = startY - layoutY + offsetY`, `lineEndX = endX - layoutX + offsetX`, `lineEndY = endY - layoutY + offsetY`
5. Arrowhead position and rotation: `arrowhead.translateX = offsetX`, `arrowhead.translateY = offsetY`, `arrowhead.rotate = -angle / PI * 180.0` (radians to degrees, negated)
6. Label offset (perpendicular, further out from the line): `labelTextTranslateX = 75.0 * sin(angle)`, `labelTextTranslateY = 50.0 * cos(angle)`

### Why `atan2(dx, dy)` Instead of `atan2(dy, dx)`?

The argument order is swapped compared to the standard polar angle convention. This produces an angle measured from the Y-axis (vertical) rather than the X-axis (horizontal). Adding `PI / 2` then rotates this to get the perpendicular direction. The result is that the arrow line is offset to the "right" of the direction from start to end (when viewing from start toward end).

## ClickDisambiguator

### 200ms Delayed JavaFX Timeline Approach

Distinguishes single-clicks from drag operations using a JavaFX `Timeline`. Source: `ClickDisambiguator.kt` (57 lines).

The flow is: `MOUSE_PRESSED` sets `isDragged = false` and stores the event. `MOUSE_DRAGGED` sets `isDragged = true`. A 200ms `Timeline` is scheduled; if `!isDragged` after the delay, the `singleClick` callback fires; if `isDragged`, the check is skipped (drag already handled). `MOUSE_CLICKED` with `clickCount == 2` cancels the pending `Timeline` and triggers `doubleClick`.

### Implementation Details

The `ClickDisambiguator` class takes `singleClick` ((MouseEvent) -> Unit), `doubleClick` ((MouseEvent) -> Unit), and `clickDelay` (Long, default 200L) in its constructor. It holds `pendingTimeline` (Timeline?), `isDragged` (Boolean), and `lastEvent` (MouseEvent?). The `coroutineScope` parameter was removed — all timing is handled by JavaFX `Timeline` instead of coroutines. See `ClickDisambiguator.kt` for the full implementation.

### Lifecycle

| Event | Action |
|-------|--------|
| `onKeyPress()` | Reset `isDragged = false` |
| `onDragged()` | Set `isDragged = true` |
| `onClick(event)` | Store event, dispatch based on `clickCount` |
| `handleSingleClick()` | Stop any pending timeline, launch 200ms `Timeline`; if `!isDragged` after delay → call `singleClick` |
| `handleDoubleClick()` | Cancel pending timeline → call `doubleClick` |
| `cancel()` | Cancel pending timeline |

### Usage in StateBox

The `StateBox` constructor creates a `ClickDisambiguator` with:
- **singleClick**: Enables inline name editing (`isEditModeEnabled = true`) + calls `onClick` callback
- **doubleClick**: Calls `onDoubleClick` callback (opens text editor tab)
- **clickDelay**: 200L (passed directly to constructor, no shared scope needed)

Event handlers route JavaFX mouse events to the disambiguator: `MOUSE_PRESSED` calls `clickDisambiguator.onKeyPress()`, `MOUSE_DRAGGED` calls `clickDisambiguator.onDragged()`, and `MOUSE_CLICKED` calls `clickDisambiguator.onClick(it)`. See `StateBox.kt` for the full implementation.

### Usage in LoopTransactionArrow

The `LoopTransactionArrow` uses its own `ClickDisambiguator` for arrowhead clicks:
- **singleClick**: Opens `EditArrowPopOver`
- **doubleClick**: Opens text editor tab for loop content

The arrow body click (for removal in RemoveTransition mode) is handled separately via `setOnMouseClicked { onClick(...) }`.

## LISMA Conversion

### Overview

`BlueprintModel.toLismaText()` (in `models/BlueprintModel.kt`) transforms the visual statechart into LISMA text. This runs at **compile/snapshot time**, not during editing. The output is a `LismaTextModel` containing the generated LISMA text and `CodeRegion` line mappings.

The function signature is `fun BlueprintModel.toLismaText(): LismaTextModel`. The algorithm processes the model in three phases: Phase 1 (Main state text, top-level content), Phase 2 (Regular transactions, grouped by target state + predicate), Phase 3 (Loop transactions, expanded into pseudo-state pairs).

### Phase 1 — Main State Text

`mainTextFragment = this.main.text`, appended to `resultStringBuilder` via `appendLine()`. `linesCounter = mainTextFragment.lines().count() + 1`. The main state's text is output first as top-level content (not inside a `state` block). The line counter starts after this content.

### Phase 2 — Regular Transactions (StateBlock Grouping)

Transactions targeting the **same state** with the **same predicate** are merged into a single `StateBlock`. A `HashMap<String, StateBlockModel>` is created keyed by `createTransactionKey(targetStateName, predicate.ifBlank { LISMA_TRUE })` where the key format is `"${targetStateName.trim()} (${predicate.trim()})"`. The `statesMap` associates state names to `BlueprintStateModel` objects. For each transaction, if no block exists for the key, a new `StateBlockModel` is created with the target state name, key, and text, and the start state name is added to `inputStates`. If a block already exists, the start state name is added to the existing block's `inputStates`.

**Key insight**: The transaction key combines the target state name and predicate. Transitions from different source states to the same target with the same predicate are merged into output like: `state "TargetState (predicate)" { <target state text> } from StartState1,StartState2,StartState3;`

Empty predicates default to `LISMA_TRUE` = `"1 > 0"` (always true).

### Phase 2 Output Format

`StateBlockModel.toString()` outputs: `"state $transactionKey {\n$text\n} from $startState1,$startState2,...;"`

### Phase 3 — Loop Transaction Expansion

Each loop transaction is expanded into **two pseudo-states** that form a cycle. The `toLisma(states)` function creates `pseudoStateName = "${stateName}_pseudo_1"` and returns a string containing: `state $pseudoStateName (${predicate.trim()}) { $text } from ${stateName};` followed by `state $stateName ($LISMA_TRUE) { ${states[stateName]!!.text} } from ${pseudoStateName};`

**Transformation logic**:
1. Create pseudo-state `<stateName>_pseudo_1` with the loop's predicate and text
2. The pseudo-state transitions **from** the original state
3. The original state gets predicate `1 > 0` (always true) and transitions **from** the pseudo-state
4. This creates a cycle: `State → pseudo_state → State`

**Example**: A loop on state "Work" with predicate `"x > 5"` and loop text `"process()"` generates: `state Work_pseudo_1 (x > 5) { process() } from Work;` followed by `state Work (1 > 0) { <original Work body text> } from Work_pseudo_1;`

### Output Order

1. Main state text, followed by a blank line
2. State blocks from regular transactions (grouped by target + predicate), with a blank line after each block
3. Loop transaction expansions (one pseudo-state pair per loop), with a blank line after each pair

### Line Number Tracking

For each generated fragment, `fragmentLinesCount = fragmentText.lines().count()`, `startLineNumber = linesCounter`, `endLineNumber = linesCounter + fragmentLinesCount + 1`. The fragment is appended to `resultStringBuilder` with a trailing blank line, and a `CodeRegion` is added with the state name, start line, and end line. `linesCounter = endLineNumber`. The `+1` accounts for the trailing blank line. `CodeRegion` objects are used by the text editor for error highlighting — mapping LISMA compilation errors back to specific fragments.

### fragmentNameByIndex

`LismaTextModel` provides `fragmentNameByIndex(index: Int)` which returns `regions.firstOrNull { index > it.startLine && index <= it.endLine }` or `DefaultFragment` if no region matches. The companion object defines `DefaultFragment = CodeRegion(name = "Main", startLine = 0, endLine = 0)`. If no region matches, returns `DefaultFragment` (named "Main").

## Editability Bindings

User state `isEditable` is dynamically bound to the editor mode: `isEditableProperty.bind(editorModeProperty.map { it.isNotEditingMode() })`.

The `isNotEditingMode()` extension function returns `true` when `this !is EditorMode.RemoveState && this !is EditorMode.AddTransition`.

| Mode | `isNotEditingMode()` | Inline name edit |
|------|---------------------|-----------------|
| `EditorMode.Idle` | `true` | Enabled |
| `EditorMode.AddTransition` | `false` | Disabled |
| `EditorMode.RemoveState` | `false` | Disabled |
| `EditorMode.RemoveTransition` | `true` | Enabled |


