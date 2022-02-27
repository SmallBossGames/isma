package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class CopyFunctionModel(
    val function: ConcatenationFunction
) {
    val nameProperty = SimpleStringProperty()
    var name by nameProperty
}