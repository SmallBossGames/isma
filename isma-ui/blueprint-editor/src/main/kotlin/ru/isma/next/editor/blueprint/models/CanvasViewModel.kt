package ru.isma.next.editor.blueprint.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.isma.next.editor.blueprint.controls.LoopTransactionArrow
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow

class CanvasViewModel {
    data class EditorTransaction(
        val startBox: StateBox,
        val endBox: StateBox,
        val arrow: TransactionArrow
    )

    data class EditorLoopTransaction(
        val stateBox: StateBox,
        val arrow: LoopTransactionArrow
    )

    private val _states = FXCollections.observableArrayList<StateBox>()
    val states: ObservableList<StateBox> = _states

    private val _transactions = FXCollections.observableArrayList<EditorTransaction>()
    val transactions: ObservableList<EditorTransaction> = _transactions

    private val _loopTransactions = FXCollections.observableArrayList<EditorLoopTransaction>()
    val loopTransactions: ObservableList<EditorLoopTransaction> = _loopTransactions

    fun addState(box: StateBox) {
        _states.add(box)
    }

    fun removeState(box: StateBox) {
        _states.remove(box)
        _transactions.removeAll { it.startBox == box || it.endBox == box }
        _loopTransactions.removeAll { it.stateBox == box }
    }

    fun addTransaction(tx: EditorTransaction) {
        _transactions.add(tx)
    }

    fun removeTransaction(arrow: TransactionArrow) {
        _transactions.removeAll { it.arrow == arrow }
    }

    fun addLoopTransaction(loop: EditorLoopTransaction) {
        _loopTransactions.add(loop)
    }

    fun removeLoopTransaction(arrow: LoopTransactionArrow) {
        _loopTransactions.removeAll { it.arrow == arrow }
    }

    fun clearAll() {
        _states.clear()
        _transactions.clear()
        _loopTransactions.clear()
    }
}
