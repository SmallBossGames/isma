package ru.isma.next.editor.blueprint.viewmodels

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.isma.next.editor.blueprint.constants.DEFAULT_STATE_HEIGHT
import ru.isma.next.editor.blueprint.constants.DEFAULT_STATE_WIDTH
import ru.isma.next.editor.blueprint.viewmodels.LoopTransactionViewModel
import ru.isma.next.editor.blueprint.viewmodels.StateViewModel
import ru.isma.next.editor.blueprint.viewmodels.TransactionViewModel

class CanvasViewModel {
    private val _states = FXCollections.observableArrayList<StateViewModel>()
    val states: ObservableList<StateViewModel> = _states

    private val _transactions = FXCollections.observableArrayList<TransactionViewModel>()
    val transactions: ObservableList<TransactionViewModel> = _transactions

    private val _loopTransactions = FXCollections.observableArrayList<LoopTransactionViewModel>()
    val loopTransactions: ObservableList<LoopTransactionViewModel> = _loopTransactions

    private val registeredStateNames = HashSet<String>()
    private var stateNameCounter = 1

    fun addState(state: StateViewModel) {
        _states.add(state)
        tryRegisterStateName(state.name)
    }

    fun createState(
        text: String = "",
        x: Double = 0.0,
        y: Double = 0.0,
        name: String = "",
        color: javafx.scene.paint.Paint = javafx.scene.paint.Color.CORAL,
        editable: Boolean = true,
        editButtonVisible: Boolean = true,
        squareWidth: Double = DEFAULT_STATE_WIDTH,
        squareHeight: Double = DEFAULT_STATE_HEIGHT
    ): StateViewModel {
        val stateName = name
        val state = StateViewModel(
            name = name,
            text = text,
            x = x,
            y = y,
            squareWidth = squareWidth,
            squareHeight = squareHeight,
            color = color,
            editable = editable,
            editButtonVisible = editButtonVisible,
            isNameUnique = { candidate ->
                candidate == stateName || !registeredStateNames.contains(candidate)
            }
        )
        return state
    }

    fun removeState(state: StateViewModel) {
        _states.removeAll { it == state }
        _transactions.removeAll { it.startStateName == state.name || it.endStateName == state.name }
        _loopTransactions.removeAll { it.stateName == state.name }
        tryUnregisterStateName(state.name)
    }

    fun addTransaction(tx: TransactionViewModel) {
        _transactions.add(tx)
    }

    fun removeTransaction(tx: TransactionViewModel) {
        _transactions.removeAll { it == tx }
    }

    fun addLoopTransaction(loop: LoopTransactionViewModel) {
        _loopTransactions.add(loop)
    }

    fun removeLoopTransaction(loop: LoopTransactionViewModel) {
        _loopTransactions.removeAll { it == loop }
    }

    fun clearAll() {
        _states.clear()
        _transactions.clear()
        _loopTransactions.clear()
        registeredStateNames.clear()
        stateNameCounter = 1
    }

    fun tryRegisterStateName(name: String): Boolean {
        if (registeredStateNames.contains(name)) {
            return false
        }
        registeredStateNames.add(name)
        return true
    }

    fun tryUnregisterStateName(name: String): Boolean {
        if (!registeredStateNames.contains(name)) {
            return false
        }
        registeredStateNames.remove(name)
        return true
    }

    fun createNextDefaultStateName(): String {
        val name = "State $stateNameCounter"
        stateNameCounter++
        return name
    }
}
