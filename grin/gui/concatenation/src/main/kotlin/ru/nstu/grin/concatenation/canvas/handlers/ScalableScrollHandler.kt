package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.model.ScaleSettings
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Controller

class ScalableScrollHandler : EventHandler<ScrollEvent>, Controller() {
    private val model: ConcatenationCanvasModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()
    private val currentCanvasSettings: MutableMap<ConcatenationAxis, ScaleSettings> = mutableMapOf()

    override fun handle(event: ScrollEvent) {
        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val axis = axises.firstOrNull {
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
        val scaleSettings = getScaleSettings(axis)

        if (event.deltaY > 0) {
            scaleSettings.upRemaining--
            scaleSettings.downRemaining++

            if (scaleSettings.upRemaining <= 0) {
                axis.settings.min += DELTA
                axis.settings.max -= DELTA
                scaleSettings.upRemaining =
                    TIMES_TO_SCROLL
                scaleSettings.downRemaining =
                    TIMES_TO_SCROLL
                scaleSettings.delta = axis.settings.pixelCost / DELTA_DELIMITER
                return
            }
        } else {
            scaleSettings.downRemaining--
            scaleSettings.upRemaining++

            if (scaleSettings.downRemaining <= 0) {
                axis.settings.min -= DELTA
                axis.settings.max += DELTA
                scaleSettings.downRemaining =
                    TIMES_TO_SCROLL
                scaleSettings.upRemaining =
                    TIMES_TO_SCROLL
                scaleSettings.delta = axis.settings.pixelCost / DELTA_DELIMITER
                return
            }
        }
        currentCanvasSettings[axis] = scaleSettings
    }

    private fun getScaleSettings(axis: ConcatenationAxis): ScaleSettings {
        return currentCanvasSettings[axis] ?: ScaleSettings(
            delta = axis.settings.pixelCost / DELTA_DELIMITER
        )
    }

    private companion object {
        const val DELTA = 2.0
        const val TIMES_TO_SCROLL = 3L
        const val DELTA_DELIMITER = 10L
    }
}