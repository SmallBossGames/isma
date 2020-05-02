package ru.nstu.grin.simple.view

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import ru.nstu.grin.common.model.PointSettings
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel

class ShowPointHandler(
    private val model: SimpleCanvasViewModel,
    private val chainDrawer: SimpleChainDrawer
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        if (event.button == MouseButton.SECONDARY) {
            model.pointToolTipSettings.isShow = false
            model.pointToolTipSettings.pointsSettings.clear()
        }
        if (event.isPrimaryButtonDown.not()) return
        val nearFunction = model.functions.firstOrNull {
            it.points.any { it.isNearBy(event.x, event.y) }
        } ?: return

        val nearPoint = nearFunction.points.first {
            it.isNearBy(event.x, event.y)
        }

        val pointTipSettings = model.pointToolTipSettings
        pointTipSettings.isShow = true
        val pointSettings = PointSettings(
            nearPoint.x,
            nearPoint.y,
            nearPoint.xGraphic ?: 0.0,
            nearPoint.yGraphic ?: 0.0
        )
        model.pointToolTipSettings.pointsSettings.add(pointSettings)
        chainDrawer.draw()
    }
}