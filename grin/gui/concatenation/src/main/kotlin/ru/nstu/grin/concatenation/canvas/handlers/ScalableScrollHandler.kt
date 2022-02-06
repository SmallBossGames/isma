package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Controller

class ScalableScrollHandler : EventHandler<ScrollEvent>, Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()

    override fun handle(event: ScrollEvent) {
        val axes = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()

        val axis = axes.firstOrNull {
            it.isLocated(event.x, event.y)
        }

        if (axis != null) {
            handleScaleByAxis(event, axis)
        } else {
            for (cartesianSpace in model.cartesianSpaces) {
                handleScaleByAxis(event, cartesianSpace.xAxis)
                handleScaleByAxis(event, cartesianSpace.yAxis)
            }
        }
        chainDrawer.draw()
    }

    private fun handleScaleByAxis(event: ScrollEvent, axis: ConcatenationAxis) {
        val delta = event.deltaY
        val oldMin = axis.settings.min
        val oldMax = axis.settings.max

        if(delta > 0) {
            axis.settings.min = oldMin * (1.0 + delta * AXIS_SCALING_COEFFICIENT)
            axis.settings.max = oldMax * (1.0 - delta * AXIS_SCALING_COEFFICIENT)
        } else {
            axis.settings.min = oldMin / (1.0 - delta * AXIS_SCALING_COEFFICIENT)
            axis.settings.max = oldMax / (1.0 + delta * AXIS_SCALING_COEFFICIENT)
        }
    }

    private companion object {
        const val AXIS_SCALING_COEFFICIENT = 0.003
    }
}