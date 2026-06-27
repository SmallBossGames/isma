package ru.isma.next.editor.blueprint.models

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import ru.isma.next.editor.blueprint.controls.LoopTransactionArrow
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow

class CanvasViewModel {
    data class EditorState(
        val model: BlueprintStateModel,
        val node: Node
    )

    data class EditorTransaction(
        val startBox: StateBox,
        val endBox: StateBox,
        val arrow: TransactionArrow,
        val node: Node
    )

    data class EditorLoopTransaction(
        val stateBox: StateBox,
        val arrow: LoopTransactionArrow,
        val node: Node
    )

    private val _states = FXCollections.observableArrayList<EditorState>()
    val states: ObservableList<EditorState> = _states

    private val _transactions = FXCollections.observableArrayList<EditorTransaction>()
    val transactions: ObservableList<EditorTransaction> = _transactions

    private val _loopTransactions = FXCollections.observableArrayList<EditorLoopTransaction>()
    val loopTransactions: ObservableList<EditorLoopTransaction> = _loopTransactions

    fun addState(model: BlueprintStateModel, node: Node) {
        _states.add(EditorState(model, node))
    }

    fun removeState(model: BlueprintStateModel) {
        _states.removeAll { it.model == model }
        _transactions.removeAll { it.startBox.name == model.name || it.endBox.name == model.name }
        _loopTransactions.removeAll { it.stateBox.name == model.name }
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
