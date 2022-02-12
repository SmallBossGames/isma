package ru.nstu.grin.concatenation.canvas.model

import javafx.scene.canvas.GraphicsContext
import tornadofx.ViewModel

class CanvasViewModel: ViewModel() {
    lateinit var functionsLayerContext: GraphicsContext
    lateinit var uiLayerContext: GraphicsContext
}