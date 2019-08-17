package ru.nstu.grin.axis

import javafx.scene.canvas.GraphicsContext

/**
 * @author kostya05983
 * This class is responsible to draw axis
 */
class Axis(
    private val gc: GraphicsContext,
    private val maxY: Double,
    private val minY: Double,
    private val delta: Double,
    private val minDelta: Double
) {
    var label: String? = null

     init {

     }
}