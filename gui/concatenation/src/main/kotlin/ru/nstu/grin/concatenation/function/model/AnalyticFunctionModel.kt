package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import ru.nstu.grin.concatenation.function.model.AbstractAddFunctionModel
import tornadofx.*

class AnalyticFunctionModel : AbstractAddFunctionModel() {
    var textProperty = SimpleStringProperty("0.001*x*x")
    var text: String by textProperty

    var deltaProperty = SimpleDoubleProperty(0.1)
    var delta: Double by deltaProperty
}