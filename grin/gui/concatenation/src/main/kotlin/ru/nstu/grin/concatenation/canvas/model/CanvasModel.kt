package ru.nstu.grin.concatenation.canvas.model

import javafx.scene.canvas.Canvas
import tornadofx.ViewModel

class CanvasModel: ViewModel() {
    lateinit var functionsLayer: Canvas
    lateinit var uiLayer: Canvas
}