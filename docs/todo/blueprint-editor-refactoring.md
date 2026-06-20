# Blueprint Editor Refactoring Plan

A step-by-step plan to improve code quality and maintainability of `isma-ui/blueprint-editor`.

---

## Pre-conditions

- All existing functionality must remain intact at each step
- No breaking changes to `ITextEditorFactory`, `BlueprintModel`, `BlueprintStateModel`, `BlueprintTransactionModel`, `BlueprintLoopTransactionModel` (these are used by `isma-ui:app`)
- Build must pass after every step

---

## Step 1 — Centralize Constants and Magic Numbers

**Scope:** All files
**Effort:** Low
**Risk:** Very low

Replace hardcoded values with named constants.

### Changes

Create `constants/BlueprintEditorConstants.kt`:

```kotlin
package ru.isma.next.editor.blueprint.constants

// Existing
const val MAIN_STATE = "Main"
const val INIT_STATE = "init"

// State dimensions
const val DEFAULT_STATE_WIDTH = 110.0
const val DEFAULT_STATE_HEIGHT = 65.0
const val FIXED_STATE_HEIGHT = 60.0
const val CORNER_RADIUS = 20.0
const val STATE_NAME_FONT_SIZE = 16.0
const val STATE_INSET = 10.0

// Arrow geometry
const val ARROW_LINE_OFFSET = 10.0
const val ARROW_TEXT_X_OFFSET = 75.0
const val ARROW_TEXT_Y_OFFSET = 50.0
const val ARROW_LINE_STROKE = 3.0
const val ARROWHEAD_STROKE = 3.0
const val ARROWHEAD_WIDTH = 7.0
const val ARROW_LABEL_FONT_SIZE = 16.0
const val ARROW_LABEL_FIELD_WIDTH = 120.0

// Loop arrow
const val LOOP_CIRCLE_RADIUS = 40.0
const val LOOP_CIRCLE_CENTER_X = 60.0
const val LOOP_ARROWHEAD_X = 100.0
const val LOOP_LABEL_X = 120.0
const val LOOP_LABEL_Y_OFFSET = -10.0

// Interaction
const val CLICK_DELAY_MS = 200L

// PopOver
const val POPOVER_MIN_WIDTH = 300.0
const val POPOVER_PADDING = 10.0
const val POPOVER_CORNER_RADIUS = 5.0
const val POPOVER_SHADOW_RADIUS = 20.0
```

Update all references in:
- `StateBox.kt` — `squareWidthProperty`, `squareHeightProperty`, `arcWidth`, `arcHeight`, `Font("Arial", 16.0)`, `translateX += 10.0`, `translateY += 10.0`
- `TransactionArrow.kt` — `Polygon(7.0, -7.0, ...)``, `strokeWidth = 3.0`, `translateY = -10.0`, `translateX = -TextFieldLength / 2.0`, `TextFieldLength`, geometry offsets (`10.0`, `75.0`, `50.0`)
- `LoopTransactionArrow.kt` — `Circle(40.0, ...)`, `strokeWidth = 3.0`, `layoutX = 100.0`, `translateY = -10.0`, `translateX = 120.0`, `Polygon(0.0, -7.0, 7.0, 0.0, -7.0, 0.0)`
- `EditArrowPopOver.kt` — `minWidth = 300.0`, `Insets(10.0)`, `CornerRadii(5.0)`, `DropShadow(20.0, ...)`
- `IsmaBlueprintEditor.kt` — `instantiateStateBox(10.0, 200.0)`, `squareHeight = 60.0`, `layoutXProperty().value += 10` (x2)

**Verification:** `./gradlew :isma-ui:blueprint-editor:build` — visual output must be identical.

---

## Step 2 — Replace Boolean Mode Flags with `EditorMode` Sealed Class

**Scope:** `IsmaBlueprintEditor.kt`
**Effort:** Low
**Risk:** Low

### Changes

Replace:
```kotlin
private val isRemoveStateModeProperty = SimpleBooleanProperty(false)
private val isRemoveTransactionModeProperty = SimpleBooleanProperty(false)
private val isAddTransactionModeProperty = SimpleBooleanProperty(false)
private val addTransactionStateCounterProperty = SimpleIntegerProperty(0)
```

With:
```kotlin
sealed class EditorMode {
    object Idle : EditorMode()
    data class AddTransition(val selectedStates: MutableList<StateBox> = mutableListOf()) : EditorMode()
    object RemoveState : EditorMode()
    object RemoveTransition : EditorMode()
}

private val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
private var editorMode by editorModeProperty
```

Update all usages:

| Old Pattern | New Pattern |
|---|---|
| `isRemoveStateMode` | `editorMode is EditorMode.RemoveState` |
| `isRemoveTransactionMode` | `editorMode is EditorMode.RemoveTransition` |
| `isAddTransactionMode` | `editorMode is EditorMode.AddTransition` |
| `addTransactionStateCounter` | `editorMode.selectedStates.size` |
| `statesToLink[0]`, `statesToLink[1]` | `editorMode.selectedStates[0]`, `[1]` |
| `resetEditorMode()` | `editorMode = EditorMode.Idle` |
| `isEditableProperty.bind(...)` | `isEditable.bind(editorMode.isNotEditingMode())` |

Add helper:
```kotlin
fun EditorMode.isNotEditingMode() = this !is EditorMode.RemoveState && this !is EditorMode.AddTransition
```

Update button text bindings to derive from `editorMode`:
```kotlin
text = when (editorMode) {
    is EditorMode.AddTransition -> "Stop adding transaction"
    else -> "New transition"
}
```

Update `mouseLinkTransactionEventHandler` to use `selectedStates` list instead of array indexing.

**Verification:** Build passes. All button toggles and transition creation behavior is identical.

---

## Step 3 — Extract Click Disambiguator

**Scope:** New file `utilities/ClickDisambiguator.kt`
**Effort:** Medium
**Risk:** Low

### Changes

Create a reusable class that handles the 200ms single-click vs double-click disambiguation:

```kotlin
class ClickDisambiguator(
    private val singleClick: () -> Unit,
    private val doubleClick: () -> Unit,
    private val delay: Long = CLICK_DELAY_MS
) {
    private var pendingSingleClick: Job? = null

    fun onKeyPress(event: MouseEvent) {
        // handled by parent component's MOUSE_PRESSED
    }

    fun onDragged(event: MouseEvent) {
        pendingSingleClick?.cancel()
        pendingSingleClick = null
    }

    fun onReleased(event: MouseEvent) {
        // handled by parent component's MOUSE_RELEASED
    }

    fun onClick(event: MouseEvent) {
        when (event.clickCount) {
            1 -> {
                if (pendingSingleClick == null) {
                    pendingSingleClick = coroutineScope.launch {
                        delay(delay)
                        pendingSingleClick = null
                        if (!isDragged) singleClick()
                    }
                }
            }
            2 -> {
                pendingSingleClick?.cancel()
                pendingSingleClick = null
                doubleClick()
            }
        }
    }

    fun cancel() {
        pendingSingleClick?.cancel()
        pendingSingleClick = null
    }

    companion object {
        private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    }
}
```

**Refactor `StateBox.kt`:**
- Remove the inline single-click coroutine logic (lines 92-122)
- Instantiate `ClickDisambiguator` in `init`
- Replace `addEventHandler(MouseEvent.MOUSE_CLICKED)` with delegating to the disambiguator
- Track `isDragged` state in the disambiguator (or pass a mutable flag)

**Refactor `LoopTransactionArrow.kt`:**
- Remove the inline single-click coroutine logic (lines 78-103)
- Replace `handleMouseClick` with delegating to the disambiguator
- Remove the static `coroutineScope` companion object

**Fix CoroutineScope leak:** The `ClickDisambiguator` should accept a `CoroutineScope` parameter or be tied to a lifecycle. For now, add a `cancel()` method and call it when the parent control is detached or disposed.

**Verification:** Build passes. Click/drag/double-click behavior is identical.

---

## Step 4 — Extract Arrow Geometry Calculation

**Scope:** New file `utilities/ArrowGeometry.kt`
**Effort:** Medium
**Risk:** Low

### Changes

Extract the pure math from `TransactionArrow.updateGeometry()` into a testable function:

```kotlin
data class ArrowGeometry(
    val lineStartX: Double,
    val lineStartY: Double,
    val lineEndX: Double,
    val lineEndY: Double,
    val arrowheadTranslateX: Double,
    val arrowheadTranslateY: Double,
    val arrowheadRotation: Double,
    val labelTextTranslateX: Double,
    val labelTextTranslateY: Double
)

fun calculateArrowGeometry(
    startX: Double,
    startY: Double,
    endX: Double,
    endY: Double,
    layoutX: Double,
    layoutY: Double,
    lineOffset: Double = ARROW_LINE_OFFSET,
    textXOffset: Double = ARROW_TEXT_X_OFFSET,
    textYOffset: Double = ARROW_TEXT_Y_OFFSET
): ArrowGeometry {
    val dx = endX - startX
    val dy = endY - startY
    val angle = atan2(dx, dy) + PI / 2

    val offsetX = lineOffset * sin(angle)
    val offsetY = lineOffset * cos(angle)

    return ArrowGeometry(
        lineStartX = startX - layoutX + offsetX,
        lineStartY = startY - layoutY + offsetY,
        lineEndX = endX - layoutX + offsetX,
        lineEndY = endY - layoutY + offsetY,
        arrowheadTranslateX = offsetX,
        arrowheadTranslateY = offsetY,
        arrowheadRotation = -angle / PI * 180.0,
        labelTextTranslateX = textXOffset * sin(angle),
        labelTextTranslateY = textYOffset * cos(angle)
    )
}
```

**Refactor `TransactionArrow.kt`:**
- Replace the inline `updateGeometry()` with a call to `calculateArrowGeometry()`
- Properties are set from the returned `ArrowGeometry`

### Add Tests

Create `src/test/kotlin/ru/isma/next/editor/blueprint/utilities/ArrowGeometryTest.kt`:

```kotlin
class ArrowGeometryTest {
    @Test
    fun `horizontal arrow right`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 100.0, 0.0, 0.0, 0.0)
        assertEquals(0.0, geo.lineStartX, 0.001)
        assertEquals(-ARROW_LINE_OFFSET, geo.lineStartY, 0.001)
        assertEquals(100.0, geo.lineEndX, 0.001)
        assertEquals(-ARROW_LINE_OFFSET, geo.lineEndY, 0.001)
        assertEquals(0.0, geo.arrowheadRotation, 0.001)
    }

    @Test
    fun `horizontal arrow left`() {
        val geo = calculateArrowGeometry(100.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        assertEquals(0.0, geo.lineStartX, 0.001)
        assertEquals(ARROW_LINE_OFFSET, geo.lineStartY, 0.001)
    }

    @Test
    fun `vertical arrow down`() {
        val geo = calculateArrowGeometry(0.0, 0.0, 0.0, 100.0, 0.0, 0.0)
        assertEquals(-ARROW_LINE_OFFSET, geo.lineStartX, 0.001)
        assertEquals(0.0, geo.lineStartY, 0.001)
    }
}
```

**Verification:** Build passes with tests. Visual output is identical.

---

## Step 5 — Decouple Editor Models from JavaFX Nodes

**Scope:** `models/BlueprintEditorTransactionModel.kt`, `models/BlueprintEditorLoopTransactionModel.kt`, `IsmaBlueprintEditor.kt`
**Effort:** Medium
**Risk:** Medium

### Problem

`BlueprintEditorTransactionModel` and `BlueprintEditorLoopTransactionModel` embed JavaFX node references (`StateBox`, `TransactionArrow`), coupling editor state to the UI toolkit.

### Changes

Introduce a `CanvasViewModel` that manages the mapping between models and UI nodes:

```kotlin
class CanvasViewModel {
    private val _states = ObservableList<StateBox>(observableListOf())
    val states: ObservableList<StateBox> = unmodifiableObservableList(_states)

    private val _transactions = ObservableList<EditorTransaction>()
    val transactions: ObservableList<EditorTransaction> = unmodifiableObservableList(_transactions)

    private val _loopTransactions = ObservableList<EditorLoopTransaction>()
    val loopTransactions: ObservableList<EditorLoopTransaction> = unmodifiableObservableList(_loopTransactions)

    data class EditorTransaction(
        val startBox: StateBox,
        val endBox: StateBox,
        val arrow: TransactionArrow
    )

    data class EditorLoopTransaction(
        val stateBox: StateBox,
        val arrow: LoopTransactionArrow
    )

    fun addState(box: StateBox) { _states.add(box) }
    fun removeState(box: StateBox) {
        _states.remove(box)
        _transactions.removeAll { it.startBox == box || it.endBox == box }
        _loopTransactions.removeAll { it.stateBox == box }
    }
    fun addTransaction(tx: EditorTransaction) { _transactions.add(tx) }
    fun removeTransaction(arrow: TransactionArrow) {
        _transactions.removeAll { it.arrow == arrow }
    }
    fun addLoopTransaction(loop: EditorLoopTransaction) { _loopTransactions.add(loop) }
    fun removeLoopTransaction(arrow: LoopTransactionArrow) {
        _loopTransactions.removeAll { it.arrow == arrow }
    }
}
```

**Refactor `IsmaBlueprintEditor.kt`:**
- Replace `private val transactions = ArrayList<BlueprintEditorTransactionModel>()` with `canvasViewModel.transactions`
- Replace `private val loopTransactions = ArrayList<BlueprintEditorLoopTransactionModel>()` with `canvasViewModel.loopTransactions`
- Replace `private val stateBoxes = ArrayList<StateBox>()` with `canvasViewModel.states`
- Update `addTransactionArrow`, `addLoopTransactionArrow`, `removeTransaction`, `removeFromEditor` to use `canvasViewModel`
- Update `getBlueprintModel()` and `setBlueprintModel()` to iterate over `canvasViewModel` collections
- Remove the now-redundant `BlueprintEditorTransactionModel` and `BlueprintEditorLoopTransactionModel` data classes (or keep them as thin aliases if other code references them)

**Refactor `removeFromEditor` extension functions:**
- Remove the three separate extension functions
- Replace with `canvasViewModel.removeState(box)`, `canvasViewModel.removeTransaction(arrow)`, `canvasViewModel.removeLoopTransaction(arrow)`

**Verification:** Build passes. All CRUD operations produce identical results.

---

## Step 6 — Split `IsmaBlueprintEditor` into ViewModel and View

**Scope:** New files `IsmaBlueprintViewModel.kt`, `IsmaBlueprintView.kt` (or keep as inner classes)
**Effort:** High
**Risk:** Medium

### Changes

**`IsmaBlueprintViewModel.kt`:**

```kotlin
class IsmaBlueprintViewModel(private val editorFactory: ITextEditorFactory) {
    val editorMode: EditorMode = EditorMode.Idle
        private set

    private val canvasViewModel = CanvasViewModel()
    private val nameMonitor = NameChangingMonitor("New state")

    // Commands exposed as observable properties / callbacks
    val onAddState: ObservableUnit<Unit> = ...
    val onAddTransition: ObservableUnit<Unit> = ...
    val onToggleRemoveState: ObservableUnit<Unit> = ...
    val onToggleRemoveTransition: ObservableUnit<Unit> = ...
    val onStateDoubleClick: Observable<(StateBox) -> Unit> = ...
    val onLoopArrowDoubleClick: Observable<(LoopTransactionArrow, StateBox) -> Unit> = ...
    val onArrowClick: Observable<(ITransactionArrowData) -> Unit> = ...
    val onArrowRemove: Observable<(ITransactionArrowData) -> Unit> = ...
    val onStateClick: Observable<(StateBox) -> Unit> = ...
    val onStateDragStart: Observable<(StateBox, MouseEvent) -> Unit> = ...
    val onStateDragEnd: ObservableUnit<Unit> = ...

    // Mode management
    fun resetMode() { ... }
    fun toggleAddTransition() { ... }
    fun toggleRemoveState() { ... }
    fun toggleRemoveTransition() { ... }

    // State management
    fun addState(positionX: Double, positionY: Double) { ... }
    fun removeState(stateBox: StateBox) {
        canvasViewModel.removeState(stateBox)
        nameMonitor.tryUnregister(stateBox.name)
    }

    // Transition management
    fun recordTransitionSource(stateBox: StateBox) { ... }
    fun addTransactionArrow(startBox: StateBox, endBox: StateBox, predicate: String, alias: String) { ... }
    fun addLoopArrow(stateBox: StateBox, text: String, predicate: String, alias: String) { ... }
    fun removeTransaction(arrow: ITransactionArrowData) { ... }

    // Data conversion
    fun toBlueprintModel(): BlueprintModel { ... }
    fun fromBlueprintModel(model: BlueprintModel) { ... }

    // Text editor
    fun openStateTextEditor(state: StateBox): Tab
    fun openLoopTextEditor(arrow: LoopTransactionArrow, stateBox: StateBox): Tab
}
```

**`IsmaBlueprintEditor.kt` (view only):**

```kotlin
class IsmaBlueprintEditor(viewModel: IsmaBlueprintViewModel) : BorderPane() {
    private val vm = viewModel

    init {
        // Build UI
        center = buildCanvas()
        bottom = buildToolbar()

        // Bind UI events to VM commands
        // ... event handlers call vm.onAddState.fire(), vm.toggleRemoveState(), etc.
    }

    private fun buildCanvas(): TabPane { ... }
    private fun buildToolbar(): ToolBar { ... }
}
```

The view becomes a thin shell (~100-150 lines) that only:
- Creates JavaFX controls
- Binds properties
- Forwards events to the ViewModel

The ViewModel becomes the single source of truth (~300-400 lines) that:
- Manages all state
- Handles all business logic
- Performs data conversion
- Exposes commands/observables to the view

**Verification:** Build passes. All interactions produce identical results.

---

## Step 7 — Add Guards for Main/Init State Removal

**Scope:** `IsmaBlueprintViewModel.kt` (or `IsmaBlueprintEditor.kt` if split hasn't happened yet)
**Effort:** Low
**Risk:** Very low

### Changes

In `removeState()`:
```kotlin
fun removeState(stateBox: StateBox) {
    if (stateBox.name == MAIN_STATE || stateBox.name == INIT_STATE) {
        return  // Cannot remove built-in states
    }
    canvasViewModel.removeState(stateBox)
    nameMonitor.tryUnregister(stateBox.name)
}
```

**Verification:** Build passes. Attempting to remove Main/Init in remove-mode has no effect.

---

## Step 8 — Add `module-info.java`

**Scope:** New file `src/main/java/module-info.java`
**Effort:** Low
**Risk:** Low

### Changes

```java
module isma.ui.editor.blueprint {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlinx.serialization.json;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;

    exports ru.isma.next.editor.blueprint;
    exports ru.isma.next.editor.blueprint.constants;
    exports ru.isma.next.editor.blueprint.controls;
    exports ru.isma.next.editor.blueprint.models;
    exports ru.isma.next.editor.blueprint.services;
    exports ru.isma.next.editor.blueprint.utilities;
}
```

**Verification:** `./gradlew :isma-ui:blueprint-editor:compileJava` passes.

---

## Step 9 — Add Unit Tests

**Scope:** `src/test/kotlin/`
**Effort:** Medium
**Risk:** Low

### Test targets

| Test class | What it tests |
|---|---|
| `NameChangingMonitorTest` | `tryRegister`, `tryUnregister`, `createNextDefaultName`, counter increment on re-registration, duplicate rejection |
| `ArrowGeometryTest` | Horizontal, vertical, diagonal arrows; offset calculations; rotation values |
| `EditorModeTest` | Sealed class state transitions; `isNotEditingMode` logic |
| `BlueprintModelSerializationTest` | JSON round-trip of `BlueprintModel` with states, transactions, and loop transactions |
| `CanvasViewModelTest` | Add/remove state cascades to transactions; add/remove transaction; duplicate prevention |

### Example: `NameChangingMonitorTest.kt`

```kotlin
class NameChangingMonitorTest {
    private lateinit var monitor: NameChangingMonitor

    @BeforeEach
    fun setUp() {
        monitor = NameChangingMonitor("New state")
    }

    @Test
    fun `first registration succeeds`() {
        assertTrue(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `duplicate registration fails`() {
        monitor.tryRegister("New state 1")
        assertFalse(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unregister allows re-registration`() {
        monitor.tryRegister("New state 1")
        monitor.tryUnregister("New state 1")
        assertTrue(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `counter increments on high-numbered name`() {
        monitor.tryRegister("New state 5")
        assertEquals("New state 6", monitor.createNextDefaultName())
    }

    @Test
    fun `counter does not decrease`() {
        monitor.tryRegister("New state 3")
        monitor.tryUnregister("New state 3")
        monitor.tryRegister("New state 7")
        assertEquals("New state 8", monitor.createNextDefaultName())
    }

    @Test
    fun `non-default names do not affect counter`() {
        monitor.tryRegister("MyState")
        assertEquals("New state 1", monitor.createNextDefaultName())
    }
}
```

**Verification:** All tests pass. `./gradlew :isma-ui:blueprint-editor:test` succeeds.

---

## Step 10 — Update `build.gradle.kts`

**Scope:** `build.gradle.kts`
**Effort:** Low
**Risk:** Low

### Changes

Add test dependencies:

```kotlin
dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
```

---

## Execution Order Summary

```
Step 1 — Constants          (low effort, zero risk)
    ↓
Step 2 — EditorMode enum    (low effort, low risk)
    ↓
Step 3 — ClickDisambiguator (medium effort, low risk)
    ↓
Step 4 — Geometry extraction (medium effort, low risk)
    ↓
Step 5 — CanvasViewModel    (medium effort, medium risk)
    ↓
Step 6 — VM/View split      (high effort, medium risk)
    ↓
Step 7 — Main/Init guards   (low effort, zero risk)
    ↓
Step 8 — module-info.java   (low effort, zero risk)
    ↓
Step 9 — Unit tests         (medium effort, zero risk)
    ↓
Step 10 — build.gradle.kts  (low effort, zero risk)
```

Each step should be committed separately so that `git bisect` can identify which change introduced any regression.

---

## Out of Scope (Future)

These improvements are valuable but intentionally excluded from this plan:

- **MVVM framework integration** (e.g., Koin for DI, reactive bindings)
- **Undo/redo support** (requires a command pattern / command stack)
- **Snap-to-grid**
- **Auto-routing for arrows** (avoid passing through other states)
- **Zoom/pan**
- **Keyboard shortcuts**
- **Context menu**
- **Multi-select**
- **Avalonia migration** (tracked separately in `docs/dotnet-ui-migration/`)
