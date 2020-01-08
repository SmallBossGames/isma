package ru.nstu.grin.model.view

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction
import ru.nstu.grin.utils.ColorUtils
import tornadofx.*

class ManualEnterFunctionViewModel : ViewModel() {
    var functionNameProperty = SimpleStringProperty()
    var functionName: String by functionNameProperty

    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty

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