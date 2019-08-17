package ru.nstu.grin.axis

import javafx.scene.canvas.GraphicsContext
import org.nstu.grin.MappingPosition

/**
 * @author kostya05983
 * This class is responsible to draw axis
 */
class Axis(
        private val gc: GraphicsContext,
        private val delta: Double,
        private val minDelta: Double,
        private val position: MappingPosition
) {
    companion object {
        private const val WIDTH_AXIS = 20.0 //100 px in default
    }

    var label: String? = null

    init {
        when (position) {
            MappingPosition.TOP -> {
                gc.fillRect(0.0, 0.0, gc.canvas.width, WIDTH_AXIS)
            }
            MappingPosition.LEFT -> {
                gc.fillRect(0.0, 0.0, WIDTH_AXIS, gc.canvas.height)
            }
            MappingPosition.RIGHT -> {
                gc.fillRect(gc.canvas.width- WIDTH_AXIS, 0.0, WIDTH_AXIS, gc.canvas.height)
            }
            MappingPosition.BOTTOM -> {
                gc.fillRect(0.0, gc.canvas.height - WIDTH_AXIS, gc.canvas.width, gc.canvas.height)
            }
        }
    }
}