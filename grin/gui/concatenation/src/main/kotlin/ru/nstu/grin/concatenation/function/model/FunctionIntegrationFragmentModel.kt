package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class FunctionIntegrationFragmentModel(val function: ConcatenationFunction) {
    var integrationMethodProperty = SimpleObjectProperty(IntegrationMethod.TRAPEZE)
    var integrationMethod by integrationMethodProperty

    var leftBorderProperty = SimpleDoubleProperty(this, "leftBorderProperty", 0.0)
    var leftBorder by leftBorderProperty

    var rightBorderProperty = SimpleDoubleProperty(this, "rightBorderProperty", 0.0)
    var rightBorder by rightBorderProperty

    init {
        leftBorder = function.points.minOfOrNull { point -> point.x } ?: 0.0
        rightBorder = function.points.maxOfOrNull { point -> point.x } ?: 0.0
    }
}