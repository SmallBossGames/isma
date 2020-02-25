package ru.nstu.grin.view.simple

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.model.view.SimpleCanvasViewModel

class ScrollableHandler(
    private val model: SimpleCanvasViewModel,
    private val chainDrawer: SimpleChainDrawer
) : EventHandler<ScrollEvent> {
    private var upRemaining: Long = DEFAULT_TIMES_TO_SCROLL
    private var downRemaining: Long = DEFAULT_TIMES_TO_SCROLL
    private var delta: Double = model.settings.pixelCost / DEFAULT_TIMES_TO_SCROLL

    override fun handle(event: ScrollEvent) {
        if (event.deltaY > 0) {
            println("Plus")
            upRemaining--
            downRemaining++

            if (upRemaining <= 0) {
                model.settings.step = model.settings.step / 2
                upRemaining = DEFAULT_TIMES_TO_SCROLL
                downRemaining = DEFAULT_TIMES_TO_SCROLL
                delta = model.settings.pixelCost / DEFAULT_TIMES_TO_SCROLL
                model.settings.pixelCost /= DEFAULT_TIMES_TO_SCROLL
                return
            }
            model.settings.pixelCost += delta
        } else {
            println("Minus")
            downRemaining--
            upRemaining++

            if (downRemaining <= 0) {
                model.settings.step = model.settings.step * 2
                downRemaining = DEFAULT_TIMES_TO_SCROLL
                upRemaining = DEFAULT_TIMES_TO_SCROLL
                delta = model.settings.pixelCost / DEFAULT_TIMES_TO_SCROLL
                model.settings.pixelCost *= DEFAULT_TIMES_TO_SCROLL
                return
            }
            model.settings.pixelCost -= delta
        }
        chainDrawer.draw()
    }

    private companion object {
        const val DEFAULT_TIMES_TO_SCROLL = 5L
    }
}