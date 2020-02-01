package ru.nstu.grin.view

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import ru.nstu.grin.controller.GrinCanvasController
import ru.nstu.grin.model.DraggedDirection
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.view.GrinCanvasModelViewModel

class MoveAxisHandler(
    val model: GrinCanvasModelViewModel,
    val controller: GrinCanvasController
) : EventHandler<MouseEvent> {
    private var previousX: Double? = null
    private var previousY: Double? = null

    override fun handle(event: MouseEvent) {
        val direction = getDirection(event)
        val newDrawings = model.drawings.map {
            if (it is Function) {
                when {
                    (direction == DraggedDirection.LEFT || direction == DraggedDirection.RIGHT)
                        && it.xAxis.isOnIt(event.x, event.y) -> {
                        it.moveFunctionOnPlot(1.0, direction)
                    }
                    (direction == DraggedDirection.UP || direction == DraggedDirection.DOWN)
                        && it.yAxis.isOnIt(event.x, event.y) -> {
                        it.moveFunctionOnPlot(1.0, direction)
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

        previousX = event.x
        previousY = event.y
    }

    private fun getDirection(event: MouseEvent): DraggedDirection {
        val currentPreviousX = previousX
        val currentPreviousY = previousY
        return when {
            currentPreviousX != null && currentPreviousX < event.x -> {
                DraggedDirection.RIGHT
            }
            currentPreviousX != null && currentPreviousX > event.x -> {
                DraggedDirection.LEFT
            }
            currentPreviousY != null && currentPreviousY < event.y -> {
                DraggedDirection.DOWN
            }
            currentPreviousY != null && currentPreviousY > event.y -> {
                DraggedDirection.UP
            }
            else -> {
                DraggedDirection.UNDEFINED
            }
        }
    }
}