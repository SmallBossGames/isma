package ru.nstu.grin.model.simple.function

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.utils.ColorUtils
import tornadofx.ViewModel
import tornadofx.setValue
import tornadofx.getValue

class AnalyticFunctionModel : ViewModel() {
    var functionNameProperty = SimpleStringProperty(this, "functionName", "")
    var functionName: String by functionNameProperty

    var functionColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var functionColor: Color by functionColorProperty

    var textProperty = SimpleStringProperty("0.001*x*x")
    var text: String by textProperty

    var deltaProperty = SimpleDoubleProperty(0.1)
    var delta: Double by deltaProperty
}