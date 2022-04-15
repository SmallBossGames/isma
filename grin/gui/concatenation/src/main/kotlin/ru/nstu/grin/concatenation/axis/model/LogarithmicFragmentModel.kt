package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import tornadofx.getValue
import tornadofx.setValue

class LogarithmicFragmentModel(
    axis: ConcatenationAxis
) {
    var logarithmBaseProperty = SimpleDoubleProperty(this, "logarithmBaseProperty", 10.0)
    var logarithmBase by logarithmBaseProperty

    var onlyIntegerPowProperty = SimpleBooleanProperty(true)
    var isOnlyIntegerPow by onlyIntegerPowProperty

    init {
        logarithmBase = axis.scaleProperties.scalingLogBase
        isOnlyIntegerPow = axis.styleProperties.marksDistanceType == MarksDistanceType.VALUE
    }

    fun createChangeSet() = UpdateLogarithmicTypeChangeSet(
        logarithmBase = logarithmBase,
        isOnlyIntegerPow = isOnlyIntegerPow,
    )
}