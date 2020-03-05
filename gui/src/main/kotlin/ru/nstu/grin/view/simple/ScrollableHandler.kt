package ru.nstu.grin.view.simple

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.model.view.SimpleCanvasViewModel

class ScrollableHandler(
    private val model: SimpleCanvasViewModel,
    private val chainDrawer: SimpleChainDrawer
) : EventHandler<ScrollEvent> {
    private var upRemaining: Long = TIMES_TO_SCROLL
    private var downRemaining: Long = TIMES_TO_SCROLL
    private var delta: Double = model.settings.pixelCost / DELTA_DELIMITER
    private var functionDelta: Double = model.settings.functionPixelCost / TIMES_TO_SCROLL

    override fun handle(event: ScrollEvent) {
        if (event.deltaY > 0) {
            println("Plus")
            upRemaining--
            downRemaining++

            if (upRemaining <= 0) {
                model.settings.step = model.settings.step / 2
                upRemaining = TIMES_TO_SCROLL
                downRemaining = TIMES_TO_SCROLL
                model.settings.pixelCost = PIXEL_COST
                delta = model.settings.pixelCost / DELTA_DELIMITER
                functionDelta = model.settings.functionPixelCost
                return
            }
            model.settings.pixelCost += delta
            model.settings.functionPixelCost += functionDelta
        } else {
            println("Minus")
            downRemaining--
            upRemaining++

            if (downRemaining <= 0) {
                model.settings.step = model.settings.step * 2
                downRemaining = TIMES_TO_SCROLL
                upRemaining = TIMES_TO_SCROLL
                model.settings.pixelCost = PIXEL_COST
                delta = model.settings.pixelCost / DELTA_DELIMITER
                functionDelta = model.settings.functionPixelCost / TIMES_TO_SCROLL
                return
            }
            model.settings.pixelCost -= delta
            model.settings.functionPixelCost -= functionDelta
        }
        chainDrawer.draw()
    }

    private companion object {
        const val TIMES_TO_SCROLL = 5L
        const val DELTA_DELIMITER = 10L
        const val PIXEL_COST = 120.0
    }
}