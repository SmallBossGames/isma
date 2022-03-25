package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.*
import javafx.scene.paint.Color
import tornadofx.*

class AxisChangeFragmentModel(
    val axis: ConcatenationAxis
) {

    var distanceBetweenMarksProperty = SimpleDoubleProperty(24.0)
    var distanceBetweenMarks by distanceBetweenMarksProperty

    var textSizeProperty = SimpleDoubleProperty(12.0)
    var textSize by textSizeProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty

    var fontColorProperty = SimpleObjectProperty<Color>()
    var fontColor by fontColorProperty

    var minProperty = SimpleDoubleProperty(0.0)
    var min by minProperty

    var maxProperty = SimpleDoubleProperty(0.0)
    var max by maxProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    var markTypeProperty = SimpleObjectProperty(AxisMarkType.LINEAR)
    var axisMarkType by markTypeProperty

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor by axisColorProperty

    init {
        axisColor = axis.backGroundColor
        distanceBetweenMarks = axis.distanceBetweenMarks
        textSize = axis.textSize
        font = axis.font
        fontColor = axis.fontColor
        isHide = axis.isHide
        min = axis.settings.min
        max = axis.settings.max
    }

    fun createChangeSet() = UpdateAxisChangeSet(
        distance = distanceBetweenMarks,
        textSize = textSize,
        font = font,
        fontColor = fontColor,
        axisColor = axisColor,
        isHide = isHide,
        axisMarkType = axisMarkType,
        min = min,
        max = max
    )
}