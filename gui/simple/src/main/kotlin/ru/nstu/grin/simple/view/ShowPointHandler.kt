package ru.nstu.grin.simple.view

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel

class ShowPointHandler(
    private val model: SimpleCanvasViewModel,
    private val chainDrawer: SimpleChainDrawer
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        if (event.isPrimaryButtonDown.not()) return
        val isNearFunction = model.functions.any {
            it.points.any { it.isNearBy(event.x, event.y) }
        }
        println("IsNear $isNearFunction")
        if (isNearFunction.not()) return

        val pointTipSettings = model.pointToolTipSettings
        pointTipSettings.isShow = true
        pointTipSettings.x = event.x
        pointTipSettings.y = event.y
        chainDrawer.draw()
    }
}