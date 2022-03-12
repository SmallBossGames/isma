package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.axis.model.Direction
import java.awt.Font

class AddFunctionModel {
    var cartesianSpaceNameProperty = SimpleStringProperty("Space 1")
    var cartesianSpaceName: String by cartesianSpaceNameProperty

    var functionNameProperty = SimpleStringProperty("Function 1")
    var functionName: String by functionNameProperty

    var functionColorProperty = SimpleObjectProperty(Color.BLACK)
    var functionColor: Color by functionColorProperty

    var functionLineSizeProperty = SimpleDoubleProperty(4.0)
    var functionLineSize by functionLineSizeProperty

    var functionLineTypeProperty = SimpleObjectProperty(LineType.POLYNOM)
    var functionLineType by functionLineTypeProperty

    val xAxis = AxisViewModel(Direction.BOTTOM, "Axis 1")

    val yAxis = AxisViewModel(Direction.LEFT, "Axis 2")

    var stepProperty = SimpleIntegerProperty(this, "stepProperty", 1)
    var step by stepProperty

    var inputWayProperty = SimpleObjectProperty(InputWay.FILE)
    var inputWay: InputWay by inputWayProperty
}

class AxisViewModel(
    direction: Direction,
    name: String,
    color: Color = Color.LIGHTGREY,
    delimeterColor: Color = Color.BLACK,
    distanceBetweenMarks: Double = 40.0,
    font: String = Font.SANS_SERIF,
    textSize: Double = 10.0
) {
    val directionProperty = SimpleObjectProperty(direction)
    var direction: Direction by directionProperty

    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    val colorProperty = SimpleObjectProperty(color)
    var color: Color by colorProperty

    val delimiterColorProperty = SimpleObjectProperty(delimeterColor)
    var delimeterColor: Color by delimiterColorProperty

    val distanceBetweenMarksProperty = SimpleDoubleProperty(distanceBetweenMarks)
    var distanceBetweenMarks by distanceBetweenMarksProperty

    val fontProperty = SimpleStringProperty(font)
    var font: String by fontProperty

    val textSizeProperty = SimpleDoubleProperty(textSize)
    var textSize: Double by textSizeProperty
}