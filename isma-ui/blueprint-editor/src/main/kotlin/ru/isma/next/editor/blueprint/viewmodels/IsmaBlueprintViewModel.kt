package ru.isma.next.editor.blueprint.viewmodels

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Tab
import javafx.scene.paint.Color
import ru.isma.next.editor.blueprint.constants.*
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.blueprint.viewmodels.CanvasViewModel
import ru.isma.next.editor.blueprint.viewmodels.LoopTransactionViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel
import ru.isma.next.editor.blueprint.viewmodels.TransactionViewModel

class IsmaBlueprintViewModel(
    private val editorFactory: ITextEditorFactory
) {
    val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
    var editorMode: EditorMode
        get() = editorModeProperty.value
        private set(value) {
            editorModeProperty.value = value
        }

    val addTransitionButtonText: StringProperty by lazy {
        SimpleStringProperty().also { prop ->
            prop.bind(editorModeProperty.map { mode ->
                when (mode) {
                    is EditorMode.AddTransition -> "Stop adding transaction"
                    else -> "New transition"
                }
            })
        }
    }

    val removeStateButtonText: StringProperty by lazy {
        SimpleStringProperty().also { prop ->
            prop.bind(editorModeProperty.map { mode ->
                when (mode) {
                    is EditorMode.RemoveState -> "Stop remove state"
                    else -> "Remove state"
                }
            })
        }
    }

    val removeTransitionButtonText: StringProperty by lazy {
        SimpleStringProperty().also { prop ->
            prop.bind(editorModeProperty.map { mode ->
                when (mode) {
                    is EditorMode.RemoveTransition -> "Stop remove transition"
                    else -> "Remove transition"
                }
            })
        }
    }

    var onStateDoubleClick: (StateViewModel) -> Unit = {}

    val canvasViewModel = CanvasViewModel()

    private val mainState: StateViewModel = createMainState()
    private val initState: StateViewModel = createInitState()

    fun resetMode() {
        editorMode = EditorMode.Idle
    }

    fun toggleAddTransition() {
        resetMode()
        editorMode = EditorMode.AddTransition(mutableSetOf())
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

    fun addState(positionX: Double = 10.0, positionY: Double = 200.0, stateText: String = ""): StateViewModel {
        val name = canvasViewModel.createNextDefaultStateName()
        val state = canvasViewModel.createState(
            text = stateText,
            x = positionX,
            y = positionY,
            name = name,
            color = Color.CORAL
        )
        canvasViewModel.addState(state)
        return state
    }

    fun removeState(stateViewModel: StateViewModel) {
        if (stateViewModel.name == MAIN_STATE || stateViewModel.name == INIT_STATE) return
        canvasViewModel.removeState(stateViewModel)
    }

    fun getMainState(): StateViewModel = mainState
    fun getInitState(): StateViewModel = initState
    fun getAllStates(): List<StateViewModel> = canvasViewModel.states.toList()

    fun recordTransitionSource(stateViewModel: StateViewModel) {
        val addTransitionMode = editorMode as? EditorMode.AddTransition ?: return

        addTransitionMode.selectedStates.add(stateViewModel)

        if (addTransitionMode.selectedStates.size < 2) {
            return
        }

        val states = addTransitionMode.selectedStates
        val state1 = states.first()
        val state2 = states.elementAt(1)

        if (state1 === state2) {
            addLoopArrow(state1, "", "", "")
        } else {
            addTransactionArrow(state1, state2, "", "")
        }

        editorMode = EditorMode.Idle
    }

    fun addTransactionArrow(start: StateViewModel, end: StateViewModel, predicate: String, alias: String) {
        if (canvasViewModel.transactions.any { it.startStateName == start.name && it.endStateName == end.name }) return

        val tx = TransactionViewModel(
            startStateName = start.name,
            endStateName = end.name,
            predicate = predicate,
            alias = alias
        )
        canvasViewModel.addTransaction(tx)
    }

    fun addLoopArrow(state: StateViewModel, text: String, predicate: String, alias: String) {
        if (canvasViewModel.loopTransactions.any { it.stateName == state.name }) return

        val loop = LoopTransactionViewModel(
            stateName = state.name,
            predicate = predicate,
            alias = alias,
            text = text
        )
        canvasViewModel.addLoopTransaction(loop)
    }

    fun removeTransaction(tx: TransactionViewModel) {
        canvasViewModel.removeTransaction(tx)
    }

    fun removeLoopArrow(loop: LoopTransactionViewModel) {
        canvasViewModel.removeLoopTransaction(loop)
    }

    fun toBlueprintModel(): BlueprintModel {
        val main = BlueprintStateModel(
            mainState.x,
            mainState.y,
            mainState.name,
            mainState.text
        )
        val init = BlueprintStateModel(
            initState.x,
            initState.y,
            initState.name,
            initState.text
        )
        val states = canvasViewModel.states.map {
            BlueprintStateModel(it.x, it.y, it.name, it.text)
        }.toTypedArray()
        val blueprintTransactions = canvasViewModel.transactions.map {
            BlueprintTransactionModel(
                startStateName = it.startStateName,
                endStateName = it.endStateName,
                predicate = it.predicate,
                alias = it.alias
            )
        }.toTypedArray()
        val blueprintLoopTransactions = canvasViewModel.loopTransactions.map {
            BlueprintLoopTransactionModel(
                stateName = it.stateName,
                predicate = it.predicate,
                alias = it.alias,
                text = it.text
            )
        }.toTypedArray()

        return BlueprintModel(main, init, states, blueprintTransactions, blueprintLoopTransactions)
    }

    fun fromBlueprintModel(model: BlueprintModel) {
        canvasViewModel.clearAll()

        mainState.apply {
            x = model.main.canvasPositionX
            y = model.main.canvasPositionY
            name = model.main.name
            text = model.main.text
            editable = true
        }
        canvasViewModel.addState(mainState)

        initState.apply {
            x = model.init.canvasPositionX
            y = model.init.canvasPositionY
            name = model.init.name
            text = model.init.text
            editable = false
        }
        canvasViewModel.addState(initState)

        val stateMap = mutableMapOf<String, StateViewModel>().apply {
            put(initState.name, initState)
            put(mainState.name, mainState)
        }

        model.states.forEach { blueprintState ->
            val state = canvasViewModel.createState(
                name = blueprintState.name,
                text = blueprintState.text,
                x = blueprintState.canvasPositionX,
                y = blueprintState.canvasPositionY,
                color = Color.CORAL
            )
            canvasViewModel.addState(state)
            stateMap[blueprintState.name] = state
        }

        model.transactions.forEach { blueprintTx ->
            val startState = stateMap[blueprintTx.startStateName]
            val endState = stateMap[blueprintTx.endStateName]
            if (startState != null && endState != null) {
                addTransactionArrow(startState, endState, blueprintTx.predicate, blueprintTx.alias)
            }
        }

        model.loopTransactions.forEach { loopTx ->
            val state = stateMap[loopTx.stateName]
            if (state != null) {
                addLoopArrow(state, loopTx.text, loopTx.predicate, loopTx.alias)
            }
        }
    }

    fun openStateTextEditor(stateViewModel: StateViewModel): Tab {
        val editor = editorFactory.createTextEditor(
            text = stateViewModel.text,
            onTextChanged = { stateViewModel.text = it }
        )

        return Tab(stateViewModel.name, editor).apply {
            textProperty().bind(stateViewModel.nameProperty)

            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
            }
        }
    }

    fun openLoopTextEditor(loopTxViewModel: LoopTransactionViewModel, stateViewModel: StateViewModel): Tab {
        val editor = editorFactory.createTextEditor(
            text = loopTxViewModel.text,
            onTextChanged = { loopTxViewModel.text = it }
        )

        return Tab("${stateViewModel.name} (loop)", editor).apply {
            textProperty().bind(stateViewModel.nameProperty.concat(" (loop)"))

            setOnCloseRequest {
                editorFactory.disposeInstance(editor)
            }
        }
    }

    fun computeArrowDisplayText(alias: String, predicate: String): String {
        return if (alias.isNotBlank()) alias else predicate
    }

    private fun createMainState(): StateViewModel {
        val state = StateViewModel(
            name = MAIN_STATE,
            text = "",
            x = STATE_INSET,
            y = 0.0,
            squareHeight = FIXED_STATE_HEIGHT,
            color = Color.LIGHTGREEN,
            editable = false
        )
        canvasViewModel.addState(state)
        return state
    }

    private fun createInitState(): StateViewModel {
        val state = StateViewModel(
            name = INIT_STATE,
            text = "",
            x = STATE_INSET,
            y = 100.0,
            squareHeight = FIXED_STATE_HEIGHT,
            color = Color.LIGHTBLUE,
            editable = false,
            editButtonVisible = false
        )
        canvasViewModel.addState(state)
        return state
    }
}
