package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import tornadofx.*

sealed class FunctionDetails : ViewModel()

class AnalyticFunctionModel : FunctionDetails() {
    var textFunctionProperty = SimpleStringProperty("x*x")
    var textFunction by textFunctionProperty

    var deltaProperty = SimpleDoubleProperty(0.1)
    var delta: Double by deltaProperty
}

class ManualFunctionModel : FunctionDetails() {
    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty
}

class FileFunctionModel : FunctionDetails() {
    var points: List<List<Point>>? = null
    var addFunctionsMode: AddFunctionsMode? = null
}