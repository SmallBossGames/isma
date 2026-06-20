# Task: Add Guards for Main/Init State Removal

## Problem

When the user enters "Remove state" mode and clicks on the Main or Init state box, the state is removed from the canvas along with all its transitions. This is documented as a known bug in `07-blueprint-editor-ux.md` section 16:

> **Main/Init can be removed** — In remove-state mode, clicking Main or Init will delete them (potential bug)

The code has no guard: `mouseRemoveStateEventHandler` calls `source.removeFromEditor()` unconditionally when in remove mode.

## Requirements

1. Prevent removal of Main and Init states in all code paths
2. The guard must be enforced at the ViewModel level (not just the view)
3. No error messages or dialogs — silently ignore the removal attempt
4. No visual changes — the states remain rendered and behave identically in all other modes

## Implementation

### If Task 6 (VM/View split) has been completed

In `IsmaBlueprintViewModel.kt`, update `removeState()`:

```kotlin
fun removeState(stateBox: StateBox) {
    if (stateBox.name == MAIN_STATE || stateBox.name == INIT_STATE) {
        return  // Cannot remove built-in states
    }
    canvasViewModel.removeState(stateBox)
    nameMonitor.tryUnregister(stateBox.name)
}
```

### If Task 6 has NOT been completed (working on the monolithic class)

In `IsmaBlueprintEditor.kt`, update `mouseRemoveStateEventHandler`:

```kotlin
private fun mouseRemoveStateEventHandler(source: StateBox) {
    if (isRemoveStateMode) {
        if (source.name != MAIN_STATE && source.name != INIT_STATE) {
            source.removeFromEditor()
        }
    }
}
```

Also update `StateBox.removeFromEditor()` extension for defense in depth:

```kotlin
private fun StateBox.removeFromEditor() {
    // Guard: cannot remove built-in states
    if (this.name == MAIN_STATE || this.name == INIT_STATE) return

    transactions.toList()
        .filter { it.startStateBox == this || it.endStateBox == this }
        .forEach { removeTransaction(it) }

    stateBoxes.remove(this)
    canvas.children.remove(this)
}
```

## Acceptance Criteria

- [ ] Entering "Remove state" mode and clicking Main state has no effect
- [ ] Entering "Remove state" mode and clicking Init state has no effect
- [ ] Entering "Remove state" mode and clicking a user state still removes it correctly
- [ ] All associated transitions and loop arrows are still removed when a user state is removed
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] No error messages, dialogs, or user-facing feedback is shown when attempting to remove Main/Init
