package ru.isma.next.editor.blueprint.views

import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.layout.Pane
import ru.isma.next.editor.blueprint.controls.LoopTransactionArrow
import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel
import ru.isma.next.editor.blueprint.models.BlueprintTransactionModel

class JavaFxBlueprintViewAdapter : BlueprintViewAdapter {

    override fun createStateBox(
        model: BlueprintStateModel,
        x: Double,
        y: Double,
        canvas: Pane,
        factory: (BlueprintStateModel, Double, Double) -> Node
    ): Node {
        val node = factory(model, x, y)
        canvas.children.add(node)
        return node
    }

    override fun addTransactionArrow(
        startBox: Node,
        endBox: Node,
        model: BlueprintTransactionModel,
        canvas: Pane,
        factory: (BlueprintTransactionModel, Node, Node) -> Node
    ): Node {
        val node = factory(model, startBox, endBox)
        canvas.children.add(node)
        return node
    }

    override fun addLoopTransactionArrow(
        stateBox: Node,
        model: BlueprintLoopTransactionModel,
        canvas: Pane,
        factory: (BlueprintLoopTransactionModel, Node) -> Node
    ): Node {
        val node = factory(model, stateBox)
        canvas.children.add(node)
        return node
    }

    override fun createTab(title: String, content: Node): Tab {
        return Tab(title, content).apply {
            isClosable = true
        }
    }

    override fun addNodeToCanvas(canvas: Pane, node: Node) {
        canvas.children.add(node)
    }

    override fun removeNodeFromCanvas(canvas: Pane, node: Node) {
        canvas.children.remove(node)
    }

    override fun clearCanvas(canvas: Pane) {
        canvas.children.clear()
    }
}
