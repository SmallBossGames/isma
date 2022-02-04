package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.SimpleDoubleProperty

data class AxisSettings(
    var correlation: Double = 0.0,

    var pixelCost: Double = 40.0,

    var step: Double = 1.0,

    var isOnlyIntegerPow: Boolean = false,
    var integerStep: Int = 1,
    var isLogarithmic: Boolean = false,
    var logarithmBase: Double = 10.0
) {
    val minProperty = SimpleDoubleProperty(-14.0)
    val maxProperty = SimpleDoubleProperty(14.0)

    var min: Double
        get() {
            return minProperty.value
        }
        set(value) {
            minProperty.value = value
        }

    var max: Double
        get() {
            return maxProperty.value
        }
        set(value) {
            maxProperty.value = value
        }
}


