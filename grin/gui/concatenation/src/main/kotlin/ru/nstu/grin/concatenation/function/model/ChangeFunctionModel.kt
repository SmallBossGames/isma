package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.getValue
import tornadofx.setValue

class ChangeFunctionModel(val function: ConcatenationFunction) {
    var nameProperty = SimpleStringProperty()
    var name by nameProperty

    var functionColorProperty = SimpleObjectProperty<Color>()
    var functionColor by functionColorProperty

    var lineSizeProperty = SimpleDoubleProperty()
    var lineSize by lineSizeProperty

    var lineTypeProperty = SimpleObjectProperty<LineType>()
    var lineType by lineTypeProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    init {
        name = function.name
        functionColor = function.functionColor
        lineSize = function.lineSize
        lineType = function.lineType
        isHide = !function.isHide
    }
}