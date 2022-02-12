package ru.nstu.grin.common.view

import javafx.scene.canvas.GraphicsContext

interface ChainDrawElement {
    fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double)
}