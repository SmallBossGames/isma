package ru.isma.next.editor.blueprint.views

import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.layout.Pane
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel

interface BlueprintViewAdapter {
    fun createStateBox(
        model: BlueprintStateModel,
        x: Double,
        y: Double,
        canvas: Pane,
        factory: (BlueprintStateModel, Double, Double) -> Node
    ): Node

    fun addTransactionArrow(
        startBox: Node,
        endBox: Node,
        model: BlueprintTransactionModel,
        canvas: Pane,
        factory: (BlueprintTransactionModel, Node, Node) -> Node
    ): Node

    fun addLoopTransactionArrow(
        stateBox: Node,
        model: BlueprintLoopTransactionModel,
        canvas: Pane,
        factory: (BlueprintLoopTransactionModel, Node) -> Node
    ): Node

    fun createTab(title: String, content: Node): Tab

    fun addNodeToCanvas(canvas: Pane, node: Node)

    fun removeNodeFromCanvas(canvas: Pane, node: Node)

    fun clearCanvas(canvas: Pane)
}
