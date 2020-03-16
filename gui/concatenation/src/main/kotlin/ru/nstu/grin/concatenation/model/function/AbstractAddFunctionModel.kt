package ru.nstu.grin.concatenation.model.function

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.common.utils.ColorUtils
import ru.nstu.grin.concatenation.model.Direction
import ru.nstu.grin.concatenation.model.ExistDirection
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.setValue

abstract class AbstractAddFunctionModel : ViewModel() {
    var functionNameProperty = SimpleStringProperty(this, "functionName", "")
    var functionName: String by functionNameProperty

    var xDirectionProperty = SimpleObjectProperty(ExistDirection(Direction.BOTTOM, null))
    var xDirection: ExistDirection by xDirectionProperty

    var yDirectionProperty = SimpleObjectProperty(ExistDirection(Direction.LEFT, null))
    var yDirection: ExistDirection by yDirectionProperty

    var functionColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var functionColor by functionColorProperty

    var xAxisNameProperty = SimpleStringProperty()
    var xAxisName by xAxisNameProperty

    var xAxisColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var xAxisColor by xAxisColorProperty

    var xDelimeterColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var xDelimiterColor by xDelimeterColorProperty

    var yAxisNameProperty = SimpleStringProperty()
    var yAxisName by yAxisNameProperty

    var yAxisColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var yAxisColor by yAxisColorProperty

    var yDelimiterColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var yDelimeterColor by yDelimiterColorProperty
}