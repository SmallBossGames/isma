package ru.isma.next.editor.blueprint

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Tab
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.controls.*
import ru.isma.next.editor.blueprint.models.*
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.utilities.getValue
import ru.isma.next.editor.blueprint.utilities.setValue
import javafx.scene.paint.Color
import kotlin.math.max

class IsmaBlueprintViewModel(
    private val editorFactory: ITextEditorFactory,
    private val canvasPane: Pane? = null
) {
    val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
    var editorMode: EditorMode
        get() = editorModeProperty.value
        private set(value) { editorModeProperty.value = value }

    private val canvasViewModel = CanvasViewModel()
    private val nameChangingMonitor = NameChangingMonitor("New state")

    private var activeStateBox: StateBox? = null
    private var xOffset = 0.0
    private var yOffset = 0.0

    private val mainStateBox: StateBox = createMainStateBox()
    private val initStateBox: StateBox = createInitStateBox()

    fun resetMode() {
        editorMode = EditorMode.Idle
    }

    fun toggleAddTransition() {
        resetMode()
        editorMode = EditorMode.AddTransition(mutableListOf())
    }

    fun toggleRemoveState() {
        resetMode()
        editorMode = EditorMode.RemoveState
    }

    fun toggleRemoveTransition() {
        resetMode()
        editorMode = EditorMode.RemoveTransition
    }

    fun isNotEditingMode(): Boolean = editorMode.isNotEditingMode()

    fun addState(positionX: Double = 10.0, positionY: Double = 200.0, stateText: String = ""): StateBox {
        val stateBox = instantiateStateBox(
            positionX = positionX,
            positionY = positionY,
            stateText = stateText,
        )
        canvasViewModel.addState(stateBox)
        return stateBox
    }

    fun removeState(stateBox: StateBox) {
        if (stateBox.name == MAIN_STATE || stateBox.name == INIT_STATE) return
        canvasViewModel.removeState(stateBox)
        nameChangingMonitor.tryUnregister(stateBox.name)
    }

    fun getMainStateBox(): StateBox = mainStateBox
    fun getInitStateBox(): StateBox = initStateBox
    fun getAllStates(): List<StateBox> = canvasViewModel.states

    fun recordTransitionSource(stateBox: StateBox) {
        if (editorMode is EditorMode.AddTransition) {
            (editorMode as EditorMode.AddTransition).selectedStates.add(stateBox)

            if ((editorMode as EditorMode.AddTransition).selectedStates.size < 2) {
                return
            }

            val states = (editorMode as EditorMode.AddTransition).selectedStates
            val state1 = states[0]!!
            val state2 = states[1]!!

            if (state1 === state2) {
                addLoopArrow(state1, "", "", "")
            } else {
                addTransactionArrow(state1, state2, "", "")
            }

            editorMode = EditorMode.Idle
        }
    }

    fun addTransactionArrow(startBox: StateBox, endBox: StateBox, predicate: String, alias: String) {
        if (canvasViewModel.transactions.any { it.startBox == startBox && it.endBox == endBox }) return

        val arrow = TransactionArrow(
            onClick = { source, _ ->
                if (editorMode is EditorMode.RemoveTransition) {
                    canvasViewModel.removeTransaction(source)
                    canvasPane?.children?.remove(source)
                }
            },
            onArrowClick = { source, event ->
                canvasPane?.let { canvas ->
                    val converted = canvas.sceneToLocal(event.sceneX, event.sceneY)
                    val popover = createEditPopOver(source, converted.x, converted.y)
                    canvas.children.add(popover)
                }
            }
        ).apply {
            startXProperty.bind(startBox.centerXProperty())
            startYProperty.bind(startBox.centerYProperty())
            endXProperty.bind(endBox.centerXProperty())
            endYProperty.bind(endBox.centerYProperty())
            this.text = predicate
            this.alias = alias
        }

        canvasPane?.children?.add(arrow)
        canvasViewModel.addTransaction(CanvasViewModel.EditorTransaction(startBox, endBox, arrow))
    }

    fun addLoopArrow(stateBox: StateBox, text: String, predicate: String, alias: String) {
        if (canvasViewModel.loopTransactions.any { it.stateBox == stateBox }) return

        val arrow = LoopTransactionArrow(
            onClick = { source, _ ->
                if (editorMode is EditorMode.RemoveTransition) {
                    canvasViewModel.removeLoopTransaction(source)
                    canvasPane?.children?.remove(source)
                }
            },
            onArrowClick = { source, event ->
                canvasPane?.let { canvas ->
                    val converted = canvas.sceneToLocal(event.sceneX, event.sceneY)
                    val popover = createEditPopOver(source, converted.x, converted.y)
                    canvas.children.add(popover)
                }
            },
            onArrowDoubleClick = { _, _ ->
                // View handles text editor - no-op in ViewModel
            },
            text = text,
            alias = alias,
            predicate = predicate
        ).apply {
            layoutXProperty().bind(stateBox.centerXProperty())
            layoutYProperty().bind(stateBox.centerYProperty())
        }

        canvasPane?.children?.add(arrow)
        canvasViewModel.addLoopTransaction(CanvasViewModel.EditorLoopTransaction(stateBox, arrow))
    }

    fun removeTransaction(arrow: TransactionArrow) {
        canvasViewModel.removeTransaction(arrow)
        canvasPane?.children?.remove(arrow)
    }

    fun removeLoopArrow(arrow: LoopTransactionArrow) {
        canvasViewModel.removeLoopTransaction(arrow)
        canvasPane?.children?.remove(arrow)
    }

    fun toBlueprintModel(): BlueprintModel {
        val main = mainStateBox.toBlueprintState()
        val init = initStateBox.toBlueprintState()
        val states = canvasViewModel.states.map { it.toBlueprintState() }.toTypedArray()
        val blueprintTransactions = canvasViewModel.transactions.map { it.toBlueprintTransaction() }.toTypedArray()
        val blueprintLoopTransactions = canvasViewModel.loopTransactions.map { it.toBlueprintLoopTransaction() }.toTypedArray()

        return BlueprintModel(main, init, states, blueprintTransactions, blueprintLoopTransactions)
    }

    fun fromBlueprintModel(model: BlueprintModel) {
        canvasViewModel.states.toList().forEach {
            canvasViewModel.removeState(it)
            canvasPane?.children?.remove(it)
        }

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

        stateMap.values.filter { it !== mainStateBox && it !== initStateBox }.forEach { canvasViewModel.addState(it) }

        model.transactions.forEach {
            addTransactionArrow(
                stateMap[it.startStateName]!!,
                stateMap[it.endStateName]!!,
                it.predicate,
                it.alias,
            )
        }

        model.loopTransactions.forEach { loopTransaction ->
            addLoopArrow(
                stateMap[loopTransaction.stateName]!!,
                loopTransaction.text,
                loopTransaction.predicate,
                loopTransaction.alias
            )
        }
    }

    fun openStateTextEditor(state: StateBox): Tab {
        val editor = editorFactory.createTextEditor(
            text = state.text,
            onTextChanged = { state.text = it }
        )

        return Tab(state.name, editor).apply {
            textProperty().bind(state.nameProperty)

            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
            }
        }
    }

    fun openLoopTextEditor(arrow: LoopTransactionArrow, stateBox: StateBox): Tab {
        val editor = editorFactory.createTextEditor(
            text = arrow.text,
            onTextChanged = { arrow.text = it }
        )

        return Tab("${stateBox.name} (loop)", editor).apply {
            textProperty().bind(stateBox.nameProperty.concat(" (loop)"))

            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
            }
        }
    }

    fun onStatePress(stateBox: StateBox, event: MouseEvent) {
        if (editorMode is EditorMode.Idle) {
            xOffset = -event.x
            yOffset = -event.y
            activeStateBox = stateBox
        }
    }

    fun onStateRelease() {
        activeStateBox = null
    }

    fun onCanvasDrag(event: MouseEvent): StateBox? {
        return activeStateBox?.let { box ->
            val newX = max(event.x + xOffset, 0.0)
            val newY = max(event.y + yOffset, 0.0)
            box.layoutXProperty().value = newX
            box.layoutYProperty().value = newY
            box
        }
    }

    private fun StateBox.initNameChangingEvent() {
        var previousName = ""
        isEditModeEnabledProperty.addListener { _, _, value ->
            if (value) {
                previousName = name
            } else {
                if (nameChangingMonitor.tryRegister(name)) {
                    nameChangingMonitor.tryUnregister(previousName)
                } else {
                    name = previousName
                }
            }
        }
    }

    private fun instantiateStateBox(
        positionX: Double = 0.0,
        positionY: Double = 0.0,
        stateName: String = "",
        stateText: String = "",
    ): StateBox {
        val stateBox = StateBox(
            onPress = { source, event ->
                onStatePress(source, event)
            },
            onRelease = { _, _ ->
                onStateRelease()
            },
            onClick = { source, _ ->
                recordTransitionSource(source)
                if (editorMode is EditorMode.RemoveState) {
                    removeState(source)
                }
            },
            onDoubleClick = { _, _ ->
                // View handles text editor - no-op in ViewModel
            }
        ).apply {
            color = Color.CORAL

            initNameChangingEvent()

            layoutXProperty().value = positionX
            layoutYProperty().value = positionY
            name = stateName.ifEmpty { nameChangingMonitor.createNextDefaultName() }
            text = stateText

            nameChangingMonitor.tryRegister(name)

            isEditableProperty.bind(editorModeProperty.map { it.isNotEditingMode() })
        }

        return stateBox
    }

    private fun createMainStateBox(): StateBox {
        return StateBox(
            onDoubleClick = { source, _ ->
                // View handles text editor - no-op in ViewModel
            },
            onPress = { source, event ->
                onStatePress(source, event)
            },
            onRelease = { _, _ ->
                onStateRelease()
            }
        ).apply {
            color = Color.LIGHTGREEN
            isEditable = false
            squareHeight = FIXED_STATE_HEIGHT
            name = MAIN_STATE
            layoutXProperty().value += STATE_INSET
            layoutXProperty().value += STATE_INSET

            nameChangingMonitor.tryRegister(name)
        }
    }

    private fun createInitStateBox(): StateBox {
        return StateBox(
            onClick = { source, _ ->
                recordTransitionSource(source)
            },
            onPress = { source, event ->
                onStatePress(source, event)
            },
            onRelease = { _, _ ->
                onStateRelease()
            }
        ).apply {
            color = Color.LIGHTBLUE
            isEditButtonVisible = false
            isEditable = false
            squareHeight = FIXED_STATE_HEIGHT
            name = INIT_STATE
            layoutXProperty().value += STATE_INSET
            layoutYProperty().value += 100

            nameChangingMonitor.tryRegister(name)
        }
    }

    private fun StateBox.toBlueprintState(): BlueprintStateModel {
        return BlueprintStateModel(
            this.layoutXProperty().value,
            this.layoutYProperty().value,
            this.name,
            this.text
        )
    }

    private fun CanvasViewModel.EditorTransaction.toBlueprintTransaction(): BlueprintTransactionModel {
        return BlueprintTransactionModel(
            startStateName = this.startBox.name,
            endStateName = this.endBox.name,
            predicate = this.arrow.text,
            alias = this.arrow.alias
        )
    }

    private fun CanvasViewModel.EditorLoopTransaction.toBlueprintLoopTransaction(): BlueprintLoopTransactionModel {
        return BlueprintLoopTransactionModel(
            stateName = this.stateBox.name,
            predicate = this.arrow.predicate,
            alias = this.arrow.alias,
            text = this.arrow.text,
        )
    }

    private fun StateBox.applyBlueprintState(blueprintState: BlueprintStateModel) {
        this.apply {
            layoutXProperty().value = blueprintState.canvasPositionX
            layoutYProperty().value = blueprintState.canvasPositionY
            name = blueprintState.name
            text = blueprintState.text
        }
    }

    private fun instantiateStateBoxFromBlueprintState(blueprintState: BlueprintStateModel): StateBox {
        return instantiateStateBox(
            positionX = blueprintState.canvasPositionX,
            positionY = blueprintState.canvasPositionY,
            stateName = blueprintState.name,
            stateText = blueprintState.text,
        )
    }

    private fun createEditPopOver(arrow: ITransactionArrowData, x: Double, y: Double) =
        EditArrowPopOver(arrow, x, y).apply {
            setOnMouseExited {
                canvasPane?.children?.remove(this)
            }
        }
}
