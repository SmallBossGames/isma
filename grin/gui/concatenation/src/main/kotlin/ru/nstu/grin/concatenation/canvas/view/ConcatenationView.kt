package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox

class ConcatenationView(
    canvasMenuBar: CanvasMenuBar,
    elementsView: ElementsView,
    canvasToolBar: CanvasToolBar,
    concatenationCanvas: ConcatenationCanvas,
) : BorderPane() {
    init {
        top = VBox(
            canvasMenuBar,
            canvasToolBar
        )

        center = concatenationCanvas

        right = elementsView
    }
}
