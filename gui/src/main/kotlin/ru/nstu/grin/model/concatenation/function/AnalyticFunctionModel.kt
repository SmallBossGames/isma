package ru.nstu.grin.model.concatenation.function

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import ru.nstu.grin.model.concatenation.function.AbstractAddFunctionModel
import tornadofx.*

class AnalyticFunctionModel : AbstractAddFunctionModel() {
    var textProperty = SimpleStringProperty("0.001*x*x")
    var text: String by textProperty

    var deltaProperty = SimpleDoubleProperty(0.1)
    var delta: Double by deltaProperty
}