package ru.nstu.grin.simple.view

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel

class DraggedHandler(
    private val model: SimpleCanvasViewModel,
    private val drawer: SimpleChainDrawer
) : EventHandler<MouseEvent> {
    private var x = -1.0
    private var y = -1.0

    override fun handle(event: MouseEvent) {
        if (!event.isPrimaryButtonDown) return
        if (x == -1.0) x = event.x
        if (y == -1.0) y = event.y

        when {
            event.x < x -> {
                model.settings.xCorrelation -= DELTA
            }
            event.x > x -> {
                model.settings.xCorrelation += DELTA
            }
            else -> {
            }
        }
        when {
            event.y < y -> {
                model.settings.yCorrelation -= DELTA
            }
            event.y > y -> {
                model.settings.yCorrelation += DELTA
            }
            else -> {
            }
        }
        x = event.x
        y = event.y
        drawer.draw()
    }

    private companion object {
        const val DELTA = 2.0
    }
}