package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class CopyFunctionModel(
    val function: ConcatenationFunction
) {
    val nameProperty = SimpleStringProperty()
    var name by nameProperty
}