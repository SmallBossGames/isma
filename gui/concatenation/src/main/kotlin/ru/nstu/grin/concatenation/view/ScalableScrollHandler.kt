package ru.nstu.grin.concatenation.view

import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import ru.nstu.grin.concatenation.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.model.ConcatenationFunction
import ru.nstu.grin.concatenation.model.CoordinateDirection
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class ScalableScrollHandler(
    val model: ConcatenationCanvasModelViewModel,
    val controller: ConcatenationCanvasController
) : EventHandler<ScrollEvent> {
    override fun handle(event: ScrollEvent) {
//        if (event.deltaY == 0.0) return
//        val newDrawings = model.drawings.map {
//            if (it is ConcatenationFunction) {
//                when {
//                    it.xAxis.isOnIt(event.x, event.y) -> {
//                        if (event.deltaY > 0) {
//                            println("Plus")
//                            it.scale(1.1, CoordinateDirection.X)
//                        } else {
//                            println("Shrink")
//                            it.scale(1.0 / 1.1, CoordinateDirection.X)
//                        }
//                    }
//                    it.yAxis.isOnIt(event.x, event.y) -> {
//                        if (event.deltaY > 0) {
//                            println("Plus")
//                            it.scale(1.1, CoordinateDirection.Y)
//                        } else {
//                            println("Shrink")
//                            it.scale(1.0 / 1.1, CoordinateDirection.Y)
//                        }
//                    }
//                    else -> {
//                        it
//                    }
//                }
//            } else {
//                it
//            }
//        }
//        controller.clearCanvas()
//        model.drawings.addAll(newDrawings)
    }
}