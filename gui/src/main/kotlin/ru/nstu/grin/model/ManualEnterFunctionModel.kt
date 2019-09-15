package ru.nstu.grin.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.*

class ManualEnterFunctionModel : ViewModel() {
    var textProperty = SimpleStringProperty()
    var text: String by textProperty

    var minXProperty = SimpleDoubleProperty()
    var minX: Double by minXProperty

    var maxXProperty = SimpleDoubleProperty()
    var maxX: Double by maxXProperty

    var minYProperty = SimpleDoubleProperty()
    var minY: Double by minYProperty

    var maxYProperty = SimpleDoubleProperty()
    var maxY: Double by maxYProperty

    var xDirectionProperty = SimpleStringProperty()
    var xDirection: String by xDirectionProperty

    var yDirectionProperty = SimpleStringProperty()
    var yDirection: String by yDirectionProperty

    var functionColorProperty = SimpleObjectProperty<Color>()
    var functionColor by functionColorProperty

    var xAxisColorProperty = SimpleObjectProperty<Color>()
    var xAxisColor by xAxisColorProperty

    var yAxisColorProperty = SimpleObjectProperty<Color>()
    var yAxisColor by yAxisColorProperty
}