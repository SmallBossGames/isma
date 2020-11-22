package ru.nstu.isma.controller

import ru.nstu.isma.view.drawers.StateDrawer
import tornadofx.Controller

class StateChartChainDrawer : Controller() {
    private val stateDrawer: StateDrawer by inject()

    fun draw() {
        stateDrawer.draw()
    }
}