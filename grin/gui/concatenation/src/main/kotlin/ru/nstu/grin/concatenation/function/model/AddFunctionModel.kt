package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.axis.model.Direction
import tornadofx.ViewModel
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import java.awt.Font

class AddFunctionModel : ViewModel() {
    var cartesianSpaceNameProperty = SimpleStringProperty(this, "cartesianSpaceName", "Пространство номер 1")
    var cartesianSpaceName by cartesianSpaceNameProperty

    var functionNameProperty = SimpleStringProperty(this, "functionName", "Функция номер 1")
    var functionName: String by functionNameProperty

    var functionColorProperty = SimpleObjectProperty(Color.BLACK)
    var functionColor by functionColorProperty

    var functionLineSizeProperty = SimpleStringProperty(this, "functionLineSize", "4.0")
    var functionLineSize by functionLineSizeProperty

    var functionLineTypeProperty = SimpleObjectProperty<LineType>(LineType.POLYNOM)
    var functionLineType by functionLineTypeProperty

    var xDirectionProperty = SimpleObjectProperty(Direction.BOTTOM)
    var xDirection: Direction by xDirectionProperty

    var yDirectionProperty = SimpleObjectProperty(Direction.LEFT)
    var yDirection: Direction by yDirectionProperty


    var xAxisNameProperty = SimpleStringProperty(this, "xAxisName", "Ось 1")
    var xAxisName by xAxisNameProperty

    var xAxisColorProperty = SimpleObjectProperty(Color.LIGHTGREY)
    var xAxisColor by xAxisColorProperty

    var xDelimeterColorProperty = SimpleObjectProperty(Color.BLACK)
    var xDelimiterColor by xDelimeterColorProperty

    var xDistanceBetweenMarksProperty = SimpleStringProperty(this, "xDistanceBetweenMarks", "40.0")
    var xDistanceBetweenMarks by xDistanceBetweenMarksProperty

    var xFontProperty = SimpleStringProperty(Font.SANS_SERIF)
    var xFont by xFontProperty

    var xTextSizeProperty = SimpleStringProperty(this, "xTextSizeProperty", "10.0")
    var xTextSize by xTextSizeProperty

    var yAxisNameProperty = SimpleStringProperty(this, "yAxisName", "Ось 2")
    var yAxisName by yAxisNameProperty

    var yAxisColorProperty = SimpleObjectProperty(Color.LIGHTGREY)
    var yAxisColor by yAxisColorProperty

    var yDelimiterColorProperty = SimpleObjectProperty(Color.BLACK)
    var yDelimeterColor by yDelimiterColorProperty

    var yDistanceBetweenMarksProperty = SimpleStringProperty(this, "yDistanceBeetweenMarks", "40.0")
    var yDistanceBetweenMarks by yDistanceBetweenMarksProperty

    var yFontProperty = SimpleStringProperty(Font.SANS_SERIF)
    var yFont by yFontProperty

    var yTextSizeProperty = SimpleStringProperty(this, "yTextSizeProperty", "10.0")
    var yTextSize by yTextSizeProperty

    var stepProperty = SimpleIntegerProperty(this, "stepProperty", 1)
    var step by stepProperty

    var inputWayProperty = SimpleObjectProperty(InputWay.FILE)
    var inputWay: InputWay by inputWayProperty

    var details: FunctionDetails = FileFunctionModel()
}