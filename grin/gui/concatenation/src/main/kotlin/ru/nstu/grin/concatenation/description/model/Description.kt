package ru.nstu.grin.concatenation.description.model

import javafx.scene.paint.Color
import javafx.scene.text.Font

/**
 * @author kostya05983
 */
data class Description(
    var text: String,
    var x: Double,
    var y: Double,
    var color: Color,
    var font: Font,
) {
    fun isLocated(eventX: Double, eventY: Double): Boolean {
        return x - font.size*2 < eventX && eventX < x + font.size*2 && y - font.size*2 < eventY && eventY < y + font.size*2
    }
}

