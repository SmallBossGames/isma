# Task: Extract Reusable Click Disambiguator

## Problem

`StateBox.kt` (lines 92-122) and `LoopTransactionArrow.kt` (lines 78-103) both implement identical 200ms delayed coroutine logic to disambiguate single-click from double-click. This is ~50 lines of duplicated code.

Additionally, both classes use a **static** `CoroutineScope` in their companion objects:
```kotlin
companion object {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
}
```
This is a resource leak — the scope is never cancelled, so pending coroutines continue running after the control is disposed.

## Requirements

1. Extract the click disambiguation logic into a reusable `ClickDisambiguator` class
2. The disambiguator must accept callbacks for single-click and double-click actions
3. Fix the coroutine leak by requiring an externally-provided `CoroutineScope`
4. Both `StateBox` and `LoopTransactionArrow` must delegate to the disambiguator
5. No behavioral changes — click, drag, and double-click must work identically

## Implementation

### Create `utilities/ClickDisambiguator.kt`

```kotlin
package ru.isma.next.editor.blueprint.utilities

import kotlinx.coroutines.*
import javafx.scene.input.MouseEvent

class ClickDisambiguator(
    private val coroutineScope: CoroutineScope,
    private val singleClick: () -> Unit,
    private val doubleClick: () -> Unit,
    private val clickDelay: Long = 200L
) {
    private var pendingSingleClick: Job? = null
    private var isDragged = false

    fun onKeyPress() {
        isDragged = false
    }

    fun onDragged() {
        isDragged = true
    }

    fun onClick(event: MouseEvent) {
        when (event.clickCount) {
            1 -> handleSingleClick()
            2 -> handleDoubleClick()
        }
    }

    fun cancel() {
        pendingSingleClick?.cancel()
        pendingSingleClick = null
    }

    private fun handleSingleClick() {
        if (pendingSingleClick == null) {
            pendingSingleClick = coroutineScope.launch {
                delay(clickDelay)
                pendingSingleClick = null
                if (!isDragged) singleClick()
            }
        }
    }

    private fun handleDoubleClick() {
        pendingSingleClick?.cancel()
        pendingSingleClick = null
        doubleClick()
    }
}
```

### Refactor `StateBox.kt`

**Remove:**
- Lines 92-122 (the inline `singleClickAction` coroutine logic)
- The `isDragged` property (line 39) — move it into the disambiguator

**Add:**
```kotlin
private val clickDisambiguator = ClickDisambiguator(
    coroutineScope = parentCoroutineScope,  // see note below
    singleClick = {
        if (isEditable) {
            isEditModeEnabled = true
            nameTextArea.requestFocus()
        }
        onClick(this@StateBox, it)  // event is captured from handler
    },
    doubleClick = { onDoubleClick(this@StateBox, it) }
)
```

**Update event handlers:**
```kotlin
// MOUSE_PRESSED
addEventHandler(MouseEvent.MOUSE_PRESSED) {
    clickDisambiguator.onKeyPress()
    onPress(this@StateBox, it)
}

// MOUSE_DRAGGED
addEventHandler(MouseEvent.MOUSE_DRAGGED) {
    clickDisambiguator.onDragged()
}

// MOUSE_CLICKED — replace the entire when block
addEventHandler(MouseEvent.MOUSE_CLICKED) {
    clickDisambiguator.onClick(it)
}
```

**CoroutineScope note:** The static `coroutineScope` companion object should be removed. Instead, the `StateBox` should accept a `CoroutineScope` parameter or use `javafx.coroutines` lifecycle. For minimal risk, keep a static scope but make it cancelable:

```kotlin
// Option A (minimal change): keep static scope but add cancel()
companion object {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    fun cancelAllScopes() { coroutineScope.cancel() }
}

// Option B (cleaner): pass scope from parent
class StateBox(
    ...
    private val coroutineScope: CoroutineScope = MainScope()  // or from parent
) : Group() {
    private val clickDisambiguator = ClickDisambiguator(
        coroutineScope = coroutineScope,
        ...
    )
    
    fun dispose() {
        clickDisambiguator.cancel()
        coroutineScope.cancel()
    }
}
```

For this task, use **Option A** (keep static scope, add `cancel()` method) to minimize risk. The proper fix can be a follow-up task.

### Refactor `LoopTransactionArrow.kt`

**Remove:**
- Lines 78-103 (`singleClickAction` coroutine logic and `handleMouseClick`)
- Lines 105-107 (static `coroutineScope` companion object)

**Add:**
```kotlin
private val clickDisambiguator = ClickDisambiguator(
    coroutineScope = ru.isma.next.editor.blueprint.controls.CoroutineScopeProvider.scope,
    singleClick = { onArrowClick(this@LoopTransactionArrow, event) },
    doubleClick = { onArrowDoubleClick(this@LoopTransactionArrow, event) }
)
```

**Update event handler:**
```kotlin
// Replace handleMouseClick() call with:
addEventHandler(MouseEvent.MOUSE_CLICKED) {
    clickDisambiguator.onClick(it)
}
```

**Create `controls/CoroutineScopeProvider.kt`** (temporary shared scope, replaces both static scopes):
```kotlin
package ru.isma.next.editor.blueprint.controls

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

object CoroutineScopeProvider {
    val scope = CoroutineScope(Dispatchers.JavaFx)
    
    fun cancelAll() { scope.cancel() }
}
```

Update both `StateBox` and `LoopTransactionArrow` to use `CoroutineScopeProvider.scope`.

## Acceptance Criteria

- [ ] `ClickDisambiguator.kt` exists and is non-empty
- [ ] `StateBox.kt` no longer contains inline coroutine delay logic
- [ ] `LoopTransactionArrow.kt` no longer contains inline coroutine delay logic
- [ ] No static `CoroutineScope` companion objects remain in either file
- [ ] A single shared scope provider exists (`CoroutineScopeProvider`)
- [ ] Single-click triggers inline name edit in StateBox (200ms after press, if not dragged)
- [ ] Double-click cancels pending single-click and triggers double-click handler
- [ ] Dragging a state box does not trigger single-click
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] Both `StateBox` and `LoopTransactionArrow` delegate to the same `ClickDisambiguator` class
