package ru.nstu.grin.concatenation.axis.model

import javafx.scene.paint.Color
import javafx.scene.text.Font

enum class MarksDistanceType { PIXEL, VALUE }

data class AxisStyleProperties(
    val backgroundColor: Color = Color.TRANSPARENT,
    val marksDistanceType: MarksDistanceType = MarksDistanceType.PIXEL,
    val marksDistance: Double = 20.0,
    val marksColor: Color = Color.BLACK,
    val marksFont: Font = Font.font("Arial", 12.0),
    val isVisible: Boolean = false,
)


