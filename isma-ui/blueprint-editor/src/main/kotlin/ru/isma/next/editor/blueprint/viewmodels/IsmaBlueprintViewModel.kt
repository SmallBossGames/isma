package ru.isma.next.editor.blueprint.viewmodels

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import ru.isma.next.editor.blueprint.constants.FIXED_STATE_HEIGHT
import ru.isma.next.editor.blueprint.constants.INIT_STATE
import ru.isma.next.editor.blueprint.constants.MAIN_STATE
import ru.isma.next.editor.blueprint.constants.STATE_INSET
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel

class IsmaBlueprintViewModel {
    val editorModeProperty = SimpleObjectProperty<EditorMode>(EditorMode.Idle)
    var editorMode: EditorMode
        get() = editorModeProperty.value
        private set(value) {
            editorModeProperty.value = value
        }

    val eventProperty = SimpleObjectProperty<BlueprintEvent>()

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

    val canvasViewModel = CanvasViewModel()

    private val mainState: StateViewModel = createMainState()
    private val initState: StateViewModel = createInitState()

    fun resetMode() {
        editorMode = EditorMode.Idle
    }

    fun toggleAddTransition() {
        editorMode = if (editorMode is EditorMode.AddTransition) {
            EditorMode.Idle
        } else {
            EditorMode.AddTransition(mutableListOf())
        }
    }

    fun toggleRemoveState() {
        editorMode = if (editorMode is EditorMode.RemoveState) {
            EditorMode.Idle
        } else {
            EditorMode.RemoveState
        }
    }

    fun toggleRemoveTransition() {
        editorMode = if (editorMode is EditorMode.RemoveTransition) {
            EditorMode.Idle
        } else {
            EditorMode.RemoveTransition
        }
    }

    fun addState(positionX: Double = 10.0, positionY: Double = 200.0, stateText: String = ""): StateViewModel {
        resetMode()
        val name = canvasViewModel.createNextDefaultStateName()
        val state = canvasViewModel.createState(
            text = stateText,
            x = positionX,
            y = positionY,
            name = name,
            kind = StateKind.USER
        )
        canvasViewModel.addState(state)
        return state
    }

    fun removeState(stateViewModel: StateViewModel) {
        if (stateViewModel.kind != StateKind.USER) return
        canvasViewModel.removeState(stateViewModel)
    }

    fun recordTransitionSource(stateViewModel: StateViewModel) {
        if (stateViewModel.kind != StateKind.USER) return

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

    fun handleStateClick(state: StateViewModel) {
        when (val mode = editorMode) {
            is EditorMode.AddTransition -> recordTransitionSource(state)
            is EditorMode.RemoveState -> removeState(state)
            else -> if (state.kind == StateKind.USER) state.startEdit()
        }
    }

    fun handleStateDoubleClick(state: StateViewModel) {
        fireEvent(BlueprintEvent.OpenStateEditor(state))
    }

    fun handleArrowBodyClick(tx: TransactionViewModel) {
        if (editorMode is EditorMode.RemoveTransition) {
            removeTransaction(tx)
        }
    }

    fun handleLoopArrowBodyClick(loop: LoopTransactionViewModel) {
        if (editorMode is EditorMode.RemoveTransition) {
            removeLoopArrow(loop)
        }
    }

    fun handleArrowheadClick(tx: TransactionViewModel): Boolean {
        if (editorMode is EditorMode.RemoveTransition) {
            removeTransaction(tx)
            return false
        }
        return true
    }

    fun handleLoopArrowheadDoubleClick(loop: LoopTransactionViewModel, state: StateViewModel) {
        fireEvent(BlueprintEvent.OpenLoopEditor(loop, state))
    }

    fun commitNameEdit(state: StateViewModel, newName: String) {
        state.name = newName
        state.commitEdit()
    }

    fun fireEvent(event: BlueprintEvent) {
        eventProperty.value = event
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
        val states = canvasViewModel.states
            .filter { it.kind == StateKind.USER }
            .map {
                BlueprintStateModel(it.x, it.y, it.name, it.text)
            }
            .toTypedArray()
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
        }
        canvasViewModel.addState(mainState)

        initState.apply {
            x = model.init.canvasPositionX
            y = model.init.canvasPositionY
            name = model.init.name
            text = model.init.text
        }
        canvasViewModel.addState(initState)

        val stateMap = mutableMapOf<String, StateViewModel>().apply {
            put(initState.name, initState)
            put(mainState.name, mainState)
        }

        model.states
            .filter { it.name != MAIN_STATE && it.name != INIT_STATE }
            .forEach { blueprintState ->
            val state = canvasViewModel.createState(
                name = blueprintState.name,
                text = blueprintState.text,
                x = blueprintState.canvasPositionX,
                y = blueprintState.canvasPositionY,
                kind = StateKind.USER
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

    private fun createMainState(): StateViewModel {
        val state = StateViewModel(
            name = MAIN_STATE,
            text = "",
            x = STATE_INSET,
            y = 0.0,
            squareHeight = FIXED_STATE_HEIGHT,
            kind = StateKind.MAIN
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
            kind = StateKind.INIT
        )
        canvasViewModel.addState(state)
        return state
    }
}
