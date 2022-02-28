package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.file.CanvasProjectLoader

class ConcatenationView(
    private val canvasProjectLoader: CanvasProjectLoader,
    private val concatenationCanvasController: ConcatenationCanvasController,
    elementsView: ElementsView,
    canvasWorkPanel: CanvasWorkPanel,
    concatenationCanvas: ConcatenationCanvas,
) : BorderPane() {
    init {
        top = VBox(
            MenuBar(
                Menu("File", null,
                    MenuItem("Save as...").apply {
                        setOnAction {
                            canvasProjectLoader.save(scene.window)
                        }
                    },
                    MenuItem("Open").apply {
                        setOnAction {
                            canvasProjectLoader.load(scene.window)
                        }
                    }
                ),
                Menu("Canvas", null,
                    MenuItem("Clean all").apply {
                        setOnAction {
                            concatenationCanvasController.clearCanvas()
                        }
                    }
                )
            ),
            canvasWorkPanel.root
        )

        center = concatenationCanvas.root

        right = elementsView
    }
}
