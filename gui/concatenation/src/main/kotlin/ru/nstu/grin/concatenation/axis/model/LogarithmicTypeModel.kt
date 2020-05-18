package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import tornadofx.ViewModel
import tornadofx.*

class LogarithmicTypeModel : ViewModel() {
    var logarithmBaseProperty = SimpleDoubleProperty(10.0)
    var logarithmBase by logarithmBaseProperty

    var onlyIntegerPowProperty = SimpleBooleanProperty(true)
    var onlyIntegerPow by onlyIntegerPowProperty
}