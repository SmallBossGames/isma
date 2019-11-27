package ru.nstu.grin.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.Direction
import ru.nstu.grin.utils.ColorUtils
import tornadofx.*

class AnalyticFunctionModel : ViewModel() {
    var textProperty = SimpleStringProperty()
    var text: String by textProperty

    var deltaProperty = SimpleDoubleProperty(0.1)
    var delta: Double by deltaProperty

    var minXProperty = SimpleDoubleProperty(0.0)
    var minX: Double by minXProperty

    var maxXProperty = SimpleDoubleProperty(10.0)
    var maxX: Double by maxXProperty

    var minYProperty = SimpleDoubleProperty(0.0)
    var minY: Double by minYProperty

    var maxYProperty = SimpleDoubleProperty(10.0)
    var maxY: Double by maxYProperty

    var xDirectionProperty = SimpleStringProperty(Direction.BOTTOM.name)
    var xDirection: String by xDirectionProperty

    var yDirectionProperty = SimpleStringProperty(Direction.LEFT.name)
    var yDirection: String by yDirectionProperty

    var functionColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var functionColor by functionColorProperty

    var xAxisColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var xAxisColor by xAxisColorProperty

    var xDelimeterColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var xDelimiterColor by xDelimeterColorProperty

    var yAxisColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var yAxisColor by yAxisColorProperty

    var yDelimiterColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var yDelimeterColor by yDelimiterColorProperty
}