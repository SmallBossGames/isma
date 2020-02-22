package ru.nstu.grin.view.simple

import javafx.scene.canvas.Canvas
import ru.nstu.grin.view.ChainDrawer

class SimpleChainDrawer(private val canvas: Canvas) : ChainDrawer {
    private val axisDrawElement = AxisDrawElement()


    override fun draw() {
        axisDrawElement.draw(canvas.graphicsContext2D)
    }
}