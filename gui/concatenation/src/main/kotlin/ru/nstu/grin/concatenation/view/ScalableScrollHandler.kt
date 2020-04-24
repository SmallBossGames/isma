package ru.nstu.grin.concatenation.view

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.ScaleSettings
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class ScalableScrollHandler(
    val model: ConcatenationCanvasModelViewModel,
    private val chainDrawer: ConcatenationChainDrawer
) : EventHandler<ScrollEvent> {
    private val currentCanvasSettings: MutableMap<CartesianSpace, ScaleSettings> = mutableMapOf()

    override fun handle(event: ScrollEvent) {
        val axises = model.cartesianSpaces.map {
            listOf(Pair(it, it.xAxis), Pair(it, it.yAxis))
        }.flatten()
        val cartesianSpace = axises.firstOrNull {
            it.second.isLocated(event.x, event.y)
        }?.first ?: return

        val scaleSettings = getScaleSettings(cartesianSpace)

        if (event.deltaY > 0) {
            scaleSettings.upRemaining--
            scaleSettings.downRemaining++
            println("Up")

            if (scaleSettings.upRemaining <= 0) {
                cartesianSpace.settings.xStep = cartesianSpace.settings.xStep / 2
                scaleSettings.upRemaining = TIMES_TO_SCROLL
                scaleSettings.downRemaining = TIMES_TO_SCROLL
                cartesianSpace.settings.xPixelCost = 40.0
                scaleSettings.delta = cartesianSpace.settings.xPixelCost / DELTA_DELIMITER
                return
            }
            cartesianSpace.settings.xPixelCost += scaleSettings.delta
        } else {
            println("Down")
            scaleSettings.downRemaining--
            scaleSettings.upRemaining++

            if (scaleSettings.downRemaining <= 0) {
                cartesianSpace.settings.xStep = cartesianSpace.settings.xStep * 2
                scaleSettings.downRemaining = TIMES_TO_SCROLL
                scaleSettings.upRemaining = TIMES_TO_SCROLL
                cartesianSpace.settings.xPixelCost = 40.0
                scaleSettings.delta = cartesianSpace.settings.xPixelCost / DELTA_DELIMITER
                return
            }
            cartesianSpace.settings.xPixelCost -= scaleSettings.delta
        }
        currentCanvasSettings[cartesianSpace] = scaleSettings
        chainDrawer.draw()
    }

    private fun getScaleSettings(cartesianSpace: CartesianSpace): ScaleSettings {
        return currentCanvasSettings[cartesianSpace] ?: ScaleSettings(
            delta = cartesianSpace.settings.xPixelCost / DELTA_DELIMITER
        )
    }

    private companion object {
        const val TIMES_TO_SCROLL = 5L
        const val DELTA_DELIMITER = 10L
    }
}