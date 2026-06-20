# Task: Replace Boolean Mode Flags with EditorMode Sealed Class

## Problem

Three independent `SimpleBooleanProperty` flags (`isRemoveStateMode`, `isRemoveTransactionMode`, `isAddTransactionMode`) plus a separate `addTransactionStateCounter` manage the editor's interaction mode. This approach has several issues:

1. **No mutual exclusion enforcement** — theoretically all three could be `true` simultaneously
2. **State is fragmented** — mode state is split across 4 properties
3. **Fragile reset logic** — `resetEditorMode()` must remember to clear all three flags
4. **Counter is decoupled** — `addTransactionStateCounter` is unrelated to the mode property
5. **Button text logic is duplicated** — each button has its own `addListener` to update text

## Requirements

1. Replace the three boolean flags plus counter with a single `EditorMode` sealed class
2. All existing UI behavior must be preserved (button toggles, mode transitions, click handling)
3. The `addTransactionStateCounter` logic must be absorbed into the mode data class
4. Button text bindings must derive from the mode, not from separate listeners
5. `isEditable` binding for state boxes must continue to work

## Implementation

### Create `EditorMode.kt`

```kotlin
package ru.isma.next.editor.blueprint

sealed class EditorMode {
    object Idle : EditorMode()
    data class AddTransition(val selectedStates: MutableList<StateBox> = mutableListOf()) : EditorMode()
    object RemoveState : EditorMode()
    object RemoveTransition : EditorMode()
}
```

### Replace properties in `IsmaBlueprintEditor.kt`

**Before:**
```kotlin
private val isRemoveStateModeProperty = SimpleBooleanProperty(false)
private val isRemoveTransactionModeProperty = SimpleBooleanProperty(false)
private val isAddTransactionModeProperty = SimpleBooleanProperty(false)
private val addTransactionStateCounterProperty = SimpleIntegerProperty(0)
private var statesToLink = arrayOf<StateBox?>(null, null)

private var isRemoveStateMode by isRemoveStateModeProperty
private var isRemoveTransactionMode by isRemoveTransactionModeProperty
private var isAddTransactionMode by isAddTransactionModeProperty
private var addTransactionStateCounter by addTransactionStateCounterProperty
```

**After:**
```kotlin
private val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
private var editorMode by editorModeProperty
```

### Replace all usages

| Old expression | New expression |
|---|---|
| `isRemoveStateMode` | `editorMode is EditorMode.RemoveState` |
| `isRemoveTransactionMode` | `editorMode is EditorMode.RemoveTransition` |
| `isAddTransactionMode` | `editorMode is EditorMode.AddTransition` |
| `addTransactionStateCounter` | `editorMode.selectedStates.size` (when in AddTransition) |
| `statesToLink[0]` | `editorMode.selectedStates[0]` |
| `statesToLink[1]` | `editorMode.selectedStates[1]` |
| `statesToLink[0] = source` | `editorMode.selectedStates.add(source)` |
| `statesToLink[1] = source` | `editorMode.selectedStates.add(source)` |
| `addTransactionStateCounter++` | implicit via `add()` |

### Update `resetEditorMode()`

```kotlin
private fun resetEditorMode() {
    editorMode = EditorMode.Idle
}
```

### Update button text bindings

**"New transition" button:**
```kotlin
text = when (editorMode) {
    is EditorMode.AddTransition -> "Stop adding transaction"
    else -> "New transition"
}
// Remove the addListener block
```

**"Remove state" button:**
```kotlin
text = when (editorMode) {
    is EditorMode.RemoveState -> "Stop remove state"
    else -> "Remove state"
}
// Remove the addListener block
```

**"Remove transition" button:**
```kotlin
text = when (editorMode) {
    is EditorMode.RemoveTransition -> "Stop remove transition"
    else -> "Remove transition"
}
// Remove the addListener block
```

### Update `mouseLinkTransactionEventHandler`

```kotlin
private fun mouseLinkTransactionEventHandler(source: StateBox) {
    if (editorMode is EditorMode.AddTransition && editorMode !is EditorMode.RemoveState) {
        (editorMode as EditorMode.AddTransition).selectedStates.add(source)

        if ((editorMode as EditorMode.AddTransition).selectedStates.size < 2) {
            return
        }

        val states = (editorMode as EditorMode.AddTransition).selectedStates
        val state1 = states[0]
        val state2 = states[1]

        if (state1 === state2) {
            addLoopTransactionArrow(state1)
        } else {
            addTransactionArrow(state1, state2)
        }

        editorMode = EditorMode.Idle
    }
}
```

### Update `isEditable` binding

Add a helper:
```kotlin
fun EditorMode.isNotEditingMode(): Boolean = this !is EditorMode.RemoveState && this !is EditorMode.AddTransition
```

Update StateBox creation:
```kotlin
isEditableProperty.bind(editorModeProperty.map { it.isNotEditingMode() })
```

Remove the old complex binding:
```kotlin
// Delete: isEditableProperty.bind((isRemoveStateModeProperty).or(isAddTransactionModeProperty).not())
```

### Remove dead code

- Delete `isRemoveStateModeProperty()`, `isRemoveTransactionModeProperty()`, `isAddTransactionModeProperty()` accessor functions (lines 34-36)
- Delete `private var statesToLink = arrayOf<StateBox?>(null, null)`
- Delete `private val addTransactionStateCounterProperty = SimpleIntegerProperty(0)`

## Acceptance Criteria

- [ ] `EditorMode.kt` exists with sealed class hierarchy
- [ ] No references to `isRemoveStateMode`, `isRemoveTransactionMode`, `isAddTransactionMode`, `addTransactionStateCounter`, or `statesToLink` remain
- [ ] All three toggle buttons toggle correctly and display correct text
- [ ] Creating a transition (click two states) still works, including self-loops
- [ ] `resetEditorMode()` clears all modes in one assignment
- [ ] State boxes correctly become non-editable during AddTransition and RemoveState modes
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] No mode can have two variants active simultaneously (enforced by type system)
