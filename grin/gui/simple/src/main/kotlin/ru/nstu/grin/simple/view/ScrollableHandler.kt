package ru.nstu.grin.simple.view

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel

class ScrollableHandler(
    private val model: SimpleCanvasViewModel,
    private val chainDrawer: SimpleChainDrawer
) : EventHandler<ScrollEvent> {
    private var upRemaining: Long =
        TIMES_TO_SCROLL
    private var downRemaining: Long =
        TIMES_TO_SCROLL
    private var delta: Double = model.settings.pixelCost / DELTA_DELIMITER

    override fun handle(event: ScrollEvent) {
        if (event.deltaY > 0) {
            upRemaining--
            downRemaining++

            if (upRemaining <= 0) {
                model.settings.step = model.settings.step / 2
                upRemaining = TIMES_TO_SCROLL
                downRemaining = TIMES_TO_SCROLL
                model.settings.pixelCost =
                    PIXEL_COST
                delta = model.settings.pixelCost / DELTA_DELIMITER
                return
            }
            model.settings.pixelCost += delta
        } else {
            downRemaining--
            upRemaining++

            if (downRemaining <= 0) {
                model.settings.step = model.settings.step * 2
                downRemaining = TIMES_TO_SCROLL
                upRemaining = TIMES_TO_SCROLL
                model.settings.pixelCost =
                    PIXEL_COST
                delta = model.settings.pixelCost / DELTA_DELIMITER
                return
            }
            model.settings.pixelCost -= delta
        }
        chainDrawer.draw()
    }

    private companion object {
        const val TIMES_TO_SCROLL = 5L
        const val DELTA_DELIMITER = 10L
        const val PIXEL_COST = 120.0
    }
}