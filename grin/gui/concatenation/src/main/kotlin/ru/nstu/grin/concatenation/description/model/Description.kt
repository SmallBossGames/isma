package ru.nstu.grin.concatenation.description.model

import javafx.scene.paint.Color

/**
 * @author kostya05983
 */
data class Description(
    var text: String,
    var textSize: Double,
    var x: Double,
    var y: Double,
    var color: Color,
    var font: String,
) {
    fun isLocated(eventX: Double, eventY: Double): Boolean {
        return x - textSize*2 < eventX && eventX < x + textSize*2 && y - textSize*2 < eventY && eventY < y + textSize*2
    }
}

