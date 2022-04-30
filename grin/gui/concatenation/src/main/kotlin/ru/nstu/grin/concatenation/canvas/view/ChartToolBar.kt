package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController

class ChartToolBar(
    concatenationCanvasModel: ConcatenationCanvasController,
    chainDrawer: ConcatenationChainDrawer
): ToolBar() {
    val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        items.addAll(
            Button("Normalize").apply {
                setOnAction {
                    coroutineScope.launch {
                        concatenationCanvasModel.normalizeSpaces()
                        chainDrawer.draw()
                    }
                }
            }
        )
    }
}