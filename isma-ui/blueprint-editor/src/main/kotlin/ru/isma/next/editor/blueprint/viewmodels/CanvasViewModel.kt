package ru.isma.next.editor.blueprint.viewmodels

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.isma.next.editor.blueprint.constants.DEFAULT_STATE_HEIGHT
import ru.isma.next.editor.blueprint.constants.DEFAULT_STATE_WIDTH
import ru.isma.next.editor.blueprint.utilities.NameChangingMonitor

class CanvasViewModel {
    private val _states = FXCollections.observableArrayList<StateViewModel>()
    val states: ObservableList<StateViewModel> = _states

    private val _transactions = FXCollections.observableArrayList<TransactionViewModel>()
    val transactions: ObservableList<TransactionViewModel> = _transactions

    private val _loopTransactions = FXCollections.observableArrayList<LoopTransactionViewModel>()
    val loopTransactions: ObservableList<LoopTransactionViewModel> = _loopTransactions

    private val nameMonitor = NameChangingMonitor("State")

    fun addState(state: StateViewModel) {
        _states.add(state)
        nameMonitor.tryRegister(state.name)
    }

    fun createState(
        text: String = "",
        x: Double = 0.0,
        y: Double = 0.0,
        name: String = "",
        kind: StateKind = StateKind.USER,
        squareWidth: Double = DEFAULT_STATE_WIDTH,
        squareHeight: Double = DEFAULT_STATE_HEIGHT
    ): StateViewModel {
        val state = StateViewModel(
            name = name,
            text = text,
            x = x,
            y = y,
            squareWidth = squareWidth,
            squareHeight = squareHeight,
            kind = kind,
            isNameUnique = { candidate ->
                candidate == name || !nameMonitor.isRegistered(candidate)
            }
        )
        return state
    }

    fun removeState(state: StateViewModel) {
        _states.removeAll { it == state }
        _transactions.removeAll { it.startStateName == state.name || it.endStateName == state.name }
        _loopTransactions.removeAll { it.stateName == state.name }
        nameMonitor.tryUnregister(state.name)
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
        nameMonitor.reset()
    }

    fun stateByName(name: String): StateViewModel? = _states.find { it.name == name }

    fun createNextDefaultStateName(): String = nameMonitor.createNextDefaultName()
}
