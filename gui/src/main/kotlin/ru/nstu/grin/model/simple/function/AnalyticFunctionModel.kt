package ru.nstu.grin.model.simple.function

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.setValue
import tornadofx.getValue

class AnalyticFunctionModel : AbstractFunctionModel() {
    var textProperty = SimpleStringProperty("0.001*x*x")
    var text: String by textProperty

    var deltaProperty = SimpleDoubleProperty(0.1)
    var delta: Double by deltaProperty
}