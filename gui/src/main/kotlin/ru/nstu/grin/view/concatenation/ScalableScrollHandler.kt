package ru.nstu.grin.view.concatenation

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.controller.concatenation.ConcatenationCanvasController
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.view.ConcatenationCanvasModelViewModel

class ScalableScrollHandler(
    val model: ConcatenationCanvasModelViewModel,
    val controller: ConcatenationCanvasController
) : EventHandler<ScrollEvent> {
    override fun handle(event: ScrollEvent) {
        if (event.deltaY == 0.0) return
        val newDrawings = model.drawings.map {
            if (it is Function) {
                when {
                    it.xAxis.isOnIt(event.x, event.y) -> {
                        if (event.deltaY > 0) {
                            println("Plus")
                            it.scale(1.1, CoordinateDirection.X)
                        } else {
                            println("Shrink")
                            it.scale(1.0 / 1.1, CoordinateDirection.X)
                        }
                    }
                    it.yAxis.isOnIt(event.x, event.y) -> {
                        if (event.deltaY > 0) {
                            println("Plus")
                            it.scale(1.1, CoordinateDirection.Y)
                        } else {
                            println("Shrink")
                            it.scale(1.0 / 1.1, CoordinateDirection.Y)
                        }
                    }
                    else -> {
                        it
                    }
                }
            } else {
                it
            }
        }
        controller.clearCanvas()
        model.drawings.addAll(newDrawings)
    }
}