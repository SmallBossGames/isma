package ru.nstu.grin.concatenation.canvas.view

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class ChartPanel(
    concatenationCanvasModel: ConcatenationCanvasModel,
    chainDrawer: ConcatenationChainDrawer
): ToolBar() {
    init {
        items.addAll(
            Button("Normalize").apply {
                onAction = EventHandler {
                    concatenationCanvasModel.normalizeSpaces()
                    chainDrawer.draw()
                }
            }
        )
    }
}