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

    var markTypeProperty = SimpleObjectProperty(AxisScalingType.LINEAR)
    var markType by markTypeProperty

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor by axisColorProperty

    init {
        axisColor = axis.styleProperties.backgroundColor
        distanceBetweenMarks = axis.styleProperties.marksDistance
        textSize = axis.styleProperties.marksFont.size
        font = axis.styleProperties.marksFont.family
        fontColor = axis.styleProperties.marksColor
        isHide = axis.styleProperties.isVisible
        min = axis.scaleProperties.minValue
        max = axis.scaleProperties.maxValue
    }

    fun createChangeSet() = UpdateAxisChangeSet(
        distance = distanceBetweenMarks,
        textSize = textSize,
        font = font,
        fontColor = fontColor,
        axisColor = axisColor,
        isHide = isHide,
        axisScalingType = markType,
        min = min,
        max = max
    )
}