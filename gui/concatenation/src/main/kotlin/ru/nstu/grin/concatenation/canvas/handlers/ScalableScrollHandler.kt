package ru.nstu.grin.concatenation.canvas.handlers

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.concatenation.canvas.view.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.model.ScaleSettings
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel

class ScalableScrollHandler(
    val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<ScrollEvent> {
    private val currentCanvasSettings: MutableMap<ConcatenationAxis, ScaleSettings> = mutableMapOf()

    override fun handle(event: ScrollEvent) {
        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val axis = axises.firstOrNull {
            it.isLocated(event.x, event.y)
        } ?: return

        val scaleSettings = getScaleSettings(axis)

        if (event.deltaY > 0) {
            scaleSettings.upRemaining--
            scaleSettings.downRemaining++
            println("Up")

            if (scaleSettings.upRemaining <= 0) {
                axis.settings.step = axis.settings.step / 2
                scaleSettings.upRemaining =
                    TIMES_TO_SCROLL
                scaleSettings.downRemaining =
                    TIMES_TO_SCROLL
                axis.settings.pixelCost = 40.0
                scaleSettings.delta = axis.settings.pixelCost / DELTA_DELIMITER
                return
            }
            axis.settings.pixelCost += scaleSettings.delta
        } else {
            println("Down")
            scaleSettings.downRemaining--
            scaleSettings.upRemaining++

            if (scaleSettings.downRemaining <= 0) {
                axis.settings.step = axis.settings.step * 2
                scaleSettings.downRemaining =
                    TIMES_TO_SCROLL
                scaleSettings.upRemaining =
                    TIMES_TO_SCROLL
                axis.settings.pixelCost = 40.0
                scaleSettings.delta = axis.settings.pixelCost / DELTA_DELIMITER
                return
            }
            axis.settings.pixelCost -= scaleSettings.delta
        }
        currentCanvasSettings[axis] = scaleSettings
        chainDrawer.draw()
    }

    private fun getScaleSettings(axis: ConcatenationAxis): ScaleSettings {
        return currentCanvasSettings[axis] ?: ScaleSettings(
            delta = axis.settings.pixelCost / DELTA_DELIMITER
        )
    }

    private companion object {
        const val TIMES_TO_SCROLL = 5L
        const val DELTA_DELIMITER = 10L
    }
}