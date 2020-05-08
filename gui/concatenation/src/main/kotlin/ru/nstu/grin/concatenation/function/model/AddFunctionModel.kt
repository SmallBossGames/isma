package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.setValue
import java.awt.Font

class AddFunctionModel : ViewModel() {
    var functionNameProperty = SimpleStringProperty(this, "functionName", "Функция номер 1")
    var functionName: String by functionNameProperty

    var functionColorProperty = SimpleObjectProperty(Color.BLACK)
    var functionColor by functionColorProperty

    var functionLineSizeProperty = SimpleStringProperty("4.0")
    var functionLineSize by functionLineSizeProperty

    var functionLineTypeProperty = SimpleObjectProperty<LineType>(LineType.POLYNOM)
    var functionLineType by functionLineTypeProperty

    var xDirectionProperty = SimpleObjectProperty(
        ExistDirection(
            Direction.BOTTOM,
            null
        )
    )
    var xDirection: ExistDirection by xDirectionProperty

    var yDirectionProperty = SimpleObjectProperty(
        ExistDirection(
            Direction.LEFT,
            null
        )
    )
    var yDirection: ExistDirection by yDirectionProperty


    var xAxisNameProperty = SimpleStringProperty(this, "xAxisName", "Ось 1")
    var xAxisName by xAxisNameProperty

    var xAxisColorProperty = SimpleObjectProperty(Color.LIGHTGREY)
    var xAxisColor by xAxisColorProperty

    var xDelimeterColorProperty = SimpleObjectProperty(Color.BLACK)
    var xDelimiterColor by xDelimeterColorProperty

    var xDistanceBetweenMarksProperty = SimpleStringProperty("40.0")
    var xDistanceBetweenMarks by xDistanceBetweenMarksProperty

    var xFontProperty = SimpleStringProperty(Font.SANS_SERIF)
    var xFont by xFontProperty

    var xTextSizeProperty = SimpleStringProperty("10.0")
    var xTextSize by xTextSizeProperty

    var yAxisNameProperty = SimpleStringProperty(this, "yAxisName", "Ось 2")
    var yAxisName by yAxisNameProperty

    var yAxisColorProperty = SimpleObjectProperty(Color.LIGHTGREY)
    var yAxisColor by yAxisColorProperty

    var yDelimiterColorProperty = SimpleObjectProperty(Color.BLACK)
    var yDelimeterColor by yDelimiterColorProperty

    var yDistanceBetweenMarksProperty = SimpleStringProperty("40.0")
    var yDistanceBetweenMarks by yDistanceBetweenMarksProperty

    var yFontProperty = SimpleStringProperty(Font.SANS_SERIF)
    var yFont by yFontProperty

    var yTextSizeProperty = SimpleStringProperty("10.0")
    var yTextSize by yTextSizeProperty

    var stepProperty = SimpleIntegerProperty(1)
    var step by stepProperty

    var inputWayProperty = SimpleObjectProperty<InputWay>(InputWay.FILE)
    var inputWay by inputWayProperty

    var details: FunctionDetails = FileFunctionModel()
}