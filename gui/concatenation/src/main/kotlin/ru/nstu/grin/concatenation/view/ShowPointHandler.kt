package ru.nstu.grin.concatenation.view

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class ShowPointHandler(
    private val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<MouseEvent> {
    override fun handle(event: MouseEvent) {
        if (event.isPrimaryButtonDown.not()) return
        val nearFunction = model.cartesianSpaces.mapNotNull {
            it.functions.firstOrNull {
                it.points.any { it.isNearBy(event.x, event.y) }
            }
        }.firstOrNull() ?: return

        val nearPoint = nearFunction.points.first {
            it.isNearBy(event.x, event.y)
        }
        println("Found nearPoint $nearPoint")

        val pointToolTipSettings = model.pointToolTipSettings
        pointToolTipSettings.isShow = true
        pointToolTipSettings.x = nearPoint.x
        pointToolTipSettings.y = nearPoint.y

        pointToolTipSettings.xGraphic = nearPoint.xGraphic ?: 0.0
        pointToolTipSettings.yGraphic = nearPoint.yGraphic ?: 0.0
        chainDrawer.draw()
    }
}