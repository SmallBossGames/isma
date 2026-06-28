# Blueprint Editor — Algorithms

## NameChangingMonitor

### Purpose

Ensures all state names are unique within the blueprint. Tracks registered names and auto-increments default name counters. Source: `NameChangingMonitor.kt` (33 lines).

### Fields

| Field | Type | Purpose |
|-------|------|---------|
| `itemDefaultName` | String | Base name prefix (e.g. "New state") |
| `defaultNameRegex` | Regex | Pattern to extract digits from default names |
| `existedNames` | HashSet<String> | Registry of all registered names |
| `nextNameCounter` | Int | Auto-increment counter (starts at 1) |

### `tryRegister(name)`

1. If `existedNames` contains `name` → return `false` (duplicate)
2. If `name` matches `defaultNameRegex`, extract the digit → set `nextNameCounter = max(nextNameCounter, digit + 1)`
3. Add `name` to `existedNames` → return `true`

### `tryUnregister(name)`

1. If `existedNames` contains `name` → remove it → return `true`
2. Otherwise → return `false`

### `createNextDefaultName()`

1. Return `"$itemDefaultName $nextNameCounter"`

### Name Edit Rollback

When a user edits a state name via inline editing (bound to `isEditModeEnabledProperty` in `IsmaBlueprintViewModel.initNameChangingEvent()`):

1. The current name is saved as `previousName` when `isEditModeEnabled` becomes `true`
2. On focus loss (`isEditModeEnabled` becomes `false`), `tryRegister(newName)` is called
3. If registration fails (duplicate), `name = previousName` restores the old name
4. If registration succeeds, `tryUnregister(previousName)` updates the registry

## ArrowGeometry

### atan2-Based Calculation

The line endpoints are computed in the `updateGeometry()` callback, triggered whenever any of `startXProperty`, `startYProperty`, `endXProperty`, `endYProperty` changes.

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

## ClickDisambiguator

### 200ms Delayed Coroutine Approach

The `StateBox` uses a 200ms delayed coroutine to distinguish single-clicks from drag operations. Source: `utilities/ClickDisambiguator.kt`.

```
MOUSE_PRESSED → reset isDragged = false, schedule 200ms check
  ↓
MOUSE_DRAGGED → set isDragged = true
  ↓
200ms elapsed → if !isDragged → trigger single-click (inline name edit)
                 if isDragged  → skip single-click (drag already handled)
MOUSE_CLICKED with clickCount == 2 → cancel pending, trigger double-click
```

## LISMA Conversion

### Overview

`BlueprintModel.convertToLisma()` (in app module) transforms the visual statechart into LISMA text. This runs at compile/snapshot time, not during editing.

### Output Format — Regular Transactions

Each group of transitions targeting the same state with the same predicate produces a single `StateBlock`:

```
state "key" {
    <state text>
} from <startState1>,<startState2>,...;
```

Where `key` = `"{targetStateName} ({predicate})"` (trimmed).

Multiple transitions from different states to the same target state with the same predicate are **merged** into a single `from` clause: `from StateA,StateB,StateC;`

### Output Format — Loop Transactions

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

### Output Order

1. Main state text (first, as top-level content)
2. All state blocks from regular transactions (grouped by target + predicate)
3. All loop transaction expansions (one pseudo-state pair per loop)

Each section is followed by a blank line.

### Line Number Tracking

For each generated fragment, start and end line numbers are tracked and returned in `CodeRegion` objects, used for error highlighting in the text editor.

## Editability Bindings

User state `isEditable` is dynamically bound:

```
isEditable = !(isRemoveStateMode OR isAddTransactionMode)
```

When in add-transition or remove-state mode, inline name editing is disabled.
