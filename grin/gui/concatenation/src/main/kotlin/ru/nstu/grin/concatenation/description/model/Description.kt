package ru.nstu.grin.concatenation.description.model

import javafx.scene.paint.Color
import javafx.scene.text.Font


data class Description(
    var text: String,
    var textOffsetX: Double,
    var textOffsetY: Double,
    var color: Color,
    var font: Font,
    var pointerX: Double,
    var pointerY: Double,
) {
    fun isLocated(eventX: Double, eventY: Double): Boolean {
        return textOffsetX - font.size*2 < eventX && eventX < textOffsetX + font.size*2 && textOffsetY - font.size*2 < eventY && eventY < textOffsetY + font.size*2
    }
}

