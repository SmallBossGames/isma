package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.ViewModel
import tornadofx.*

class LogarithmicTypeModel : ViewModel() {
    private val axis: ConcatenationAxis by param()

    var logarithmBaseProperty = SimpleDoubleProperty(this, "logarithmBaseProperty", 10.0)
    var logarithmBase by logarithmBaseProperty

    var onlyIntegerPowProperty = SimpleBooleanProperty(true)
    var isOnlyIntegerPow by onlyIntegerPowProperty

    var integerStepProperty = SimpleIntegerProperty(this, "integerStepProperty", 1)
    var integerStep by integerStepProperty

    init {
        logarithmBase = axis.settings.logarithmBase
        isOnlyIntegerPow = axis.settings.isOnlyIntegerPow
        integerStep = axis.settings.integerStep
    }

    fun createChangeSet() = UpdateLogarithmicTypeChangeSet(
        logarithmBase = logarithmBase,
        isOnlyIntegerPow = isOnlyIntegerPow,
        integerStep = integerStep,
    )
}