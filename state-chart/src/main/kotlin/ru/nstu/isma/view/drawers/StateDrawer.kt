package ru.nstu.isma.view.drawers

import ru.nstu.isma.model.StateChartCanvasModel
import ru.nstu.isma.view.Drawer
import tornadofx.Controller

class StateDrawer : Drawer, Controller() {
    private val model: StateChartCanvasModel by inject()

    override fun draw() {
        val gc = model.canvas.graphicsContext2D
        val stateElements = model.stateElements
        for (stateElement in stateElements) {
            val center = stateElement.center
            gc.strokeOval(center.x, center.y, stateElement.size, stateElement.size)
        }
    }
}