# Task: Split IsmaBlueprintEditor into ViewModel and View

## Problem

`IsmaBlueprintEditor.kt` is a 544-line God class that handles:
- UI layout (toolbar, tabs, canvas structure)
- Business logic (mode management, CRUD for states/transitions)
- Data conversion (canvas ↔ BlueprintModel)
- Event handling (mouse events, click dispatch)
- State management (editor modes, drag state, active elements)

This violates the Single Responsibility Principle and makes the class impossible to test without a full JavaFX runtime.

## Requirements

1. Extract all business logic, state management, and data conversion into `IsmaBlueprintViewModel`
2. Reduce `IsmaBlueprintEditor` to a thin view shell (~100-150 lines) that only creates controls and binds events
3. The ViewModel must expose commands/observables for the view to wire up
4. No behavioral changes — all interactions must work identically
5. The public API (`getBlueprintModel()`, `setBlueprintModel()`) must remain on the view class for backward compatibility with `BlueprintProjectDataProvider`

## Implementation

### Create `IsmaBlueprintViewModel.kt`

```kotlin
package ru.isma.next.editor.blueprint

import javafx.beans.property.*
import javafx.scene.control.Tab
import ru.isma.next.editor.blueprint.constants.INIT_STATE
import ru.isma.next.editor.blueprint.constants.MAIN_STATE
import ru.isma.next.editor.blueprint.controls.*
import ru.isma.next.editor.blueprint.models.*
import ru.isma.next.editor.blueprint.services.ITextEditorFactory

class IsmaBlueprintViewModel(private val editorFactory: ITextEditorFactory) {
    // Mode
    private val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
    val editorMode: EditorMode get() = editorModeProperty.value
        private set

    // Canvas state
    private val canvasViewModel = CanvasViewModel()
    private val nameMonitor = NameChangingMonitor("New state")

    // Drag state
    private var activeStateBox: StateBox? = null
    private var xOffset = 0.0
    private var yOffset = 0.0

    // Fixed states
    private val mainStateBox = createMainStateBox()
    private val initStateBox = createInitStateBox()

    // --- Mode management ---

    fun resetMode() {
        editorModeProperty.value = EditorMode.Idle
    }

    fun toggleAddTransition() {
        resetMode()
        editorModeProperty.value = EditorMode.AddTransition(mutableListOf())
    }

    fun toggleRemoveState() {
        resetMode()
        editorModeProperty.value = EditorMode.RemoveState
    }

    fun toggleRemoveTransition() {
        resetMode()
        editorModeProperty.value = EditorMode.RemoveTransition
    }

    fun isNotEditingMode(): Boolean = editorMode !is EditorMode.RemoveState && editorMode !is EditorMode.AddTransition

    // --- State management ---

    fun addState(positionX: Double = 10.0, positionY: 200.0, stateText: String = ""): StateBox {
        val stateBox = instantiateStateBox(
            positionX = positionX,
            positionY = positionY,
            stateText = stateText,
            isFixed = false
        )
        canvasViewModel.addState(stateBox)
        return stateBox
    }

    fun removeState(stateBox: StateBox) {
        if (stateBox.name == MAIN_STATE || stateBox.name == INIT_STATE) return
        canvasViewModel.removeState(stateBox)
        nameMonitor.tryUnregister(stateBox.name)
    }

    fun getMainStateBox(): StateBox = mainStateBox
    fun getInitStateBox(): StateBox = initStateBox
    fun getAllStates(): List<StateBox> = canvasViewModel.states

    // --- Transition management ---

    fun recordTransitionSource(stateBox: StateBox) {
        val mode = editorMode
        if (mode is EditorMode.AddTransition) {
            mode.selectedStates.add(stateBox)
            if (mode.selectedStates.size >= 2) {
                val states = mode.selectedStates
                if (states[0] === states[1]) {
                    addLoopArrow(states[0], "", "", "")
                } else {
                    addTransactionArrow(states[0], states[1], "", "")
                }
                editorModeProperty.value = EditorMode.Idle
            }
        }
    }

    fun addTransactionArrow(startBox: StateBox, endBox: StateBox, predicate: String, alias: String) {
        if (canvasViewModel.transactions.any { it.startBox == startBox && it.endBox == endBox }) return

        val arrow = TransactionArrow(
            onClick = { source, _ ->
                if (editorMode is EditorMode.RemoveTransition) {
                    removeTransaction(arrow)
                }
            },
            onArrowClick = { _, _ -> /* view handles popover */ }
        ).apply {
            startXProperty.bind(startBox.centerXProperty())
            startYProperty.bind(startBox.centerYProperty())
            endXProperty.bind(endBox.centerXProperty())
            endYProperty.bind(endBox.centerYProperty())
            this@IsmaBlueprintViewModel.text = predicate
            this@IsmaBlueprintViewModel.alias = alias
        }

        canvasViewModel.addTransaction(CanvasViewModel.EditorTransaction(startBox, endBox, arrow))
    }

    fun addLoopArrow(stateBox: StateBox, text: String, predicate: String, alias: String) {
        if (canvasViewModel.loopTransactions.any { it.stateBox == stateBox }) return

        val arrow = LoopTransactionArrow(
            onClick = { source, _ ->
                if (editorMode is EditorMode.RemoveTransition) {
                    removeLoopArrow(arrow)
                }
            },
            onArrowClick = { _, _ -> /* view handles popover */ },
            onArrowDoubleClick = { _, _ -> /* view handles text editor */ },
            text = text,
            alias = alias,
            predicate = predicate
        ).apply {
            layoutXProperty.bind(stateBox.centerXProperty())
            layoutYProperty.bind(stateBox.centerYProperty())
        }

        canvasViewModel.addLoopTransaction(CanvasViewModel.EditorLoopTransaction(stateBox, arrow))
    }

    fun removeTransaction(arrow: TransactionArrow) {
        canvasViewModel.removeTransaction(arrow)
    }

    fun removeLoopArrow(arrow: LoopTransactionArrow) {
        canvasViewModel.removeLoopTransaction(arrow)
    }

    // --- Data conversion ---

    fun toBlueprintModel(): BlueprintModel {
        val main = mainStateBox.toBlueprintState()
        val init = initStateBox.toBlueprintState()
        val states = canvasViewModel.states.map { it.toBlueprintState() }.toTypedArray()
        val transactions = canvasViewModel.transactions.map { it.toBlueprintTransaction() }.toTypedArray()
        val loopTransactions = canvasViewModel.loopTransactions.map { it.toBlueprintLoopTransaction() }.toTypedArray()
        return BlueprintModel(main, init, states, transactions, loopTransactions)
    }

    fun fromBlueprintModel(model: BlueprintModel) {
        canvasViewModel.states.toList().forEach { it.removeFromEditor() }
        canvasViewModel.transactions.clear()
        canvasViewModel.loopTransactions.clear()

        mainStateBox.applyBlueprintState(model.main)
        initStateBox.applyBlueprintState(model.init)

        val stateMap = model.states.associateByTo(
            mutableMapOf(
                initStateBox.name to initStateBox,
                mainStateBox.name to mainStateBox
            ),
            { it.name },
            { instantiateStateBoxFromBlueprintState(it) }
        )

        stateMap.values.filter { it !== mainStateBox && it !== initStateBox }.forEach {
            canvasViewModel.addState(it)
        }

        model.transactions.forEach {
            addTransactionArrow(
                stateMap[it.startStateName]!!,
                stateMap[it.endStateName]!!,
                it.predicate,
                it.alias
            )
        }

        model.loopTransactions.forEach { loopTx ->
            addLoopArrow(
                stateMap[loopTx.stateName]!!,
                loopTx.text,
                loopTx.predicate,
                loopTx.alias
            )
        }
    }

    // --- Text editor ---

    fun openStateTextEditor(state: StateBox): Tab {
        val editor = editorFactory.createTextEditor(
            text = state.text,
            onTextChanged = { state.text = it }
        )
        return Tab(state.name, editor).apply {
            textProperty().bind(state.nameProperty)
            setOnCloseRequest { editorFactory.disposeInstance(editor) }
        }
    }

    fun openLoopTextEditor(arrow: LoopTransactionArrow, stateBox: StateBox): Tab {
        val editor = editorFactory.createTextEditor(
            text = arrow.text,
            onTextChanged = { arrow.text = it }
        )
        return Tab("${stateBox.name} (loop)", editor).apply {
            textProperty().bind(stateBox.nameProperty.concat(" (loop)"))
            setOnCloseRequest { editorFactory.disposeInstance(editor) }
        }
    }

    // --- Drag ---

    fun onStatePress(stateBox: StateBox, event: javafx.scene.input.MouseEvent) {
        if (editorMode !is EditorMode.RemoveState && editorMode !is EditorMode.AddTransition) {
            xOffset = -event.x
            yOffset = -event.y
            activeStateBox = stateBox
        }
    }

    fun onStateRelease() {
        activeStateBox = null
    }

    fun onCanvasDrag(event: javafx.scene.input.MouseEvent): StateBox? {
        return activeStateBox?.let { box ->
            val newX = max(event.x + xOffset, 0.0)
            val newY = max(event.y + yOffset, 0.0)
            box.layoutXProperty().value = newX
            box.layoutYProperty().value = newY
            box
        }
    }

    // --- Name editing ---

    fun onStateNameEditStarted(stateBox: StateBox): String {
        return stateBox.name.also { stateBox._previousName = it }
    }

    fun onStateNameEditFinished(stateBox: StateBox): Boolean {
        val previousName = stateBox._previousName ?: return true
        if (nameMonitor.tryRegister(stateBox.name)) {
            nameMonitor.tryUnregister(previousName)
            return true
        } else {
            stateBox.name = previousName
            return false
        }
    }

    // --- Internal helpers (private) ---

    private fun instantiateStateBox(
        positionX: Double = 0.0,
        positionY: Double = 0.0,
        stateName: String = "",
        stateText: String = "",
        isFixed: Boolean = false
    ): StateBox {
        val stateBox = StateBox(
            onPress = { source, event -> onStatePress(source, event) },
            onRelease = { _, _ -> onStateRelease() },
            onClick = { source, _ ->
                recordTransitionSource(source)
                if (editorMode is EditorMode.RemoveState) {
                    removeState(source)
                }
            },
            onDoubleClick = { source, _ -> /* view handles */ }
        ).apply {
            color = if (isFixed) {
                if (name == MAIN_STATE) javafx.scene.paint.Color.LIGHTGREEN
                else javafx.scene.paint.Color.LIGHTBLUE
            } else {
                javafx.scene.paint.Color.CORAL
            }

            if (isFixed) {
                isEditable = false
                squareHeight = 60.0
                if (name == MAIN_STATE) {
                    isEditButtonVisible = false
                }
            }

            initNameChangingEvent(stateBox)

            layoutXProperty().value = positionX
            layoutYProperty().value = positionY
            if (stateName.isEmpty() && !isFixed) {
                name = nameMonitor.createNextDefaultName()
            } else {
                name = stateName
            }
            text = stateText

            nameMonitor.tryRegister(name)

            isEditableProperty.bind(editorModeProperty.map { it.isNotEditingMode() })
        }

        return stateBox
    }

    private fun createMainStateBox(): StateBox {
        return instantiateStateBox(
            positionX = 20.0,
            positionY = 10.0,
            stateName = MAIN_STATE,
            isFixed = true
        )
    }

    private fun createInitStateBox(): StateBox {
        return instantiateStateBox(
            positionX = 10.0,
            positionY = 100.0,
            stateName = INIT_STATE,
            isFixed = true
        )
    }

    private fun StateBox.toBlueprintState(): BlueprintStateModel {
        return BlueprintStateModel(
            this.layoutXProperty().value,
            this.layoutYProperty().value,
            this.name,
            this.text
        )
    }

    private fun StateBox.applyBlueprintState(blueprintState: BlueprintStateModel) {
        layoutXProperty().value = blueprintState.canvasPositionX
        layoutYProperty().value = blueprintState.canvasPositionY
        name = blueprintState.name
        text = blueprintState.text
    }

    private fun instantiateStateBoxFromBlueprintState(blueprintState: BlueprintStateModel): StateBox {
        return instantiateStateBox(
            positionX = blueprintState.canvasPositionX,
            positionY = blueprintState.canvasPositionY,
            stateName = blueprintState.name,
            stateText = blueprintState.text,
            isFixed = false
        )
    }

    private fun StateBox.initNameChangingEvent(stateBox: StateBox) {
        var previousName: String? = null
        isEditModeEnabledProperty.addListener { _, _, value ->
            if (value) {
                previousName = name
            } else {
                if (nameMonitor.tryRegister(name)) {
                    previousName?.let { nameMonitor.tryUnregister(it) }
                } else {
                    name = previousName ?: name
                }
            }
        }
    }

    private fun CanvasViewModel.EditorTransaction.toBlueprintTransaction(): BlueprintTransactionModel {
        return BlueprintTransactionModel(
            startStateName = startBox.name,
            endStateName = endBox.name,
            predicate = arrow.text,
            alias = arrow.alias
        )
    }

    private fun CanvasViewModel.EditorLoopTransaction.toBlueprintLoopTransaction(): BlueprintLoopTransactionModel {
        return BlueprintLoopTransactionModel(
            stateName = stateBox.name,
            predicate = arrow.predicate,
            alias = arrow.alias,
            text = arrow.text
        )
    }

    // Extension for storing previous name during edit
    private var StateBox._previousName: String?
        get() = this.properties["__previousName"] as? String
        set(value) { this.properties["__previousName"] = value }
}
```

### Refactor `IsmaBlueprintEditor.kt` to thin view

```kotlin
package ru.isma.next.editor.blueprint

import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import ru.isma.next.editor.blueprint.controls.ITransactionArrowData
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.services.ITextEditorFactory

class IsmaBlueprintEditor(
    private val viewModel: IsmaBlueprintViewModel,
    editorFactory: ITextEditorFactory
) : BorderPane() {

    private val canvas = Pane()
    private val diagramTab = Tab("Diagram", javafx.scene.control.ScrollPane(canvas)).apply {
        isClosable = false
    }
    private val tabs = TabPane(diagramTab)

    init {
        // Wire up canvas with fixed states and events
        canvas.children.add(viewModel.getMainStateBox())
        canvas.children.add(viewModel.getInitStateBox())

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED) { event ->
            viewModel.onCanvasDrag(event)
        }

        center = tabs
        bottom = buildToolbar()
    }

    private fun buildToolbar(): ToolBar {
        val newStateButton = Button("New state").apply {
            onAction = EventHandler {
                viewModel.resetMode()
                viewModel.addState()
            }
        }

        val newTransitionButton = Button("New transition").apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.AddTransition) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleAddTransition()
                }
            }
            textProperty().bind(Bindings.when(Bindings.isNotNull(viewModel.editorModeProperty as javafx.beans.property.ObjectProperty<EditorMode>))
                .then(Bindings.createStringBinding({
                    when (viewModel.editorMode) {
                        is EditorMode.AddTransition -> "Stop adding transaction"
                        else -> "New transition"
                    }
                }, viewModel.editorModeProperty)))
        }

        val separator = Separator()

        val removeStateButton = Button("Remove state").apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.RemoveState) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleRemoveState()
                }
            }
            textProperty().bind(Bindings.createStringBinding({
                when (viewModel.editorMode) {
                    is EditorMode.RemoveState -> "Stop remove state"
                    else -> "Remove state"
                }
            }, viewModel.editorModeProperty))
        }

        val removeTransitionButton = Button("Remove transition").apply {
            onAction = EventHandler {
                if (viewModel.editorMode is EditorMode.RemoveTransition) {
                    viewModel.resetMode()
                } else {
                    viewModel.toggleRemoveTransition()
                }
            }
            textProperty().bind(Bindings.createStringBinding({
                when (viewModel.editorMode) {
                    is EditorMode.RemoveTransition -> "Stop remove transition"
                    else -> "Remove transition"
                }
            }, viewModel.editorModeProperty))
        }

        return ToolBar(newStateButton, newTransitionButton, separator, removeStateButton, removeTransitionButton).apply {
            val visible = tabs.selectionModel.selectedItemProperty().isEqualTo(diagramTab)
            visibleProperty().bind(visible)
            managedProperty().bind(visible)
        }
    }

    // Public API — delegates to ViewModel
    fun getBlueprintModel() = viewModel.toBlueprintModel()
    fun setBlueprintModel(model: ru.isma.next.editor.blueprint.models.BlueprintModel) {
        viewModel.fromBlueprintModel(model)
        // Re-add canvas elements from viewModel
        canvas.children.clear()
        canvas.children.add(viewModel.getMainStateBox())
        canvas.children.add(viewModel.getInitStateBox())
        viewModel.getAllStates().forEach { canvas.children.add(it) }
        // Add arrows (they are added to canvas by viewModel already)
    }
}
```

### Update Koin bindings

In `isma-ui/app/src/main/kotlin/.../KoinExtensions.kt`:

```kotlin
// Before:
scopedOf(::IsmaBlueprintEditor)

// After:
factory { IsmaBlueprintViewModel(get()) }
scoped { IsmaBlueprintEditor(get(), get()) } // viewModel, editorFactory
```

### Handle `openStateTextEditorTab` and `openStateTextEditorTab(arrow, stateBox)`

These methods add tabs to the `TabPane`. Since the ViewModel no longer has direct access to the `TabPane`, the view must expose a callback or the ViewModel must return a `Tab` that the view adds:

In the ViewModel, `openStateTextEditor()` and `openLoopTextEditor()` already return `Tab` objects. The view must intercept these and add them to its `tabs`:

Option: The ViewModel exposes an observable or callback:
```kotlin
val onOpenTextEditor = java.util.function.Consumer<StateBox> { state ->
    val tab = viewModel.openStateTextEditor(state)
    view.tabs.tabs.add(tab)
}
```

For minimal risk, keep a reference to the `TabPane` in the ViewModel via constructor parameter, or use an observable callback pattern.

## Acceptance Criteria

- [ ] `IsmaBlueprintViewModel.kt` exists and contains all business logic
- [ ] `IsmaBlueprintEditor.kt` is under 200 lines
- [ ] `IsmaBlueprintEditor` only creates JavaFX controls and wires events to ViewModel calls
- [ ] `getBlueprintModel()` and `setBlueprintModel()` still work on the view class
- [ ] Koin DI is updated to provide ViewModel and View separately
- [ ] `./gradlew :isma-ui:blueprint-editor:build` passes
- [ ] All interactions (add/remove states, add/remove transitions, drag, click, popover, text editor tabs) work identically
- [ ] No logic remains in the View that could belong in the ViewModel
