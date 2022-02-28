package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.file.CanvasProjectLoader

class CanvasMenuBar(
    private val canvasProjectLoader: CanvasProjectLoader,
    private val concatenationCanvasController: ConcatenationCanvasController,
): MenuBar(){
    init {
        menus.addAll(
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
        )
    }
}