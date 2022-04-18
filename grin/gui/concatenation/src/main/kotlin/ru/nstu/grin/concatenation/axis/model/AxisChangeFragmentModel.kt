package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.*
import javafx.scene.paint.Color
import tornadofx.*

class AxisChangeFragmentModel(
    val axis: ConcatenationAxis
) {
    var distanceBetweenMarksProperty = SimpleDoubleProperty()
    var marksDistance by distanceBetweenMarksProperty

    var marksDistanceTypeProperty = SimpleObjectProperty<MarksDistanceType>()
    var marksDistanceType: MarksDistanceType by marksDistanceTypeProperty

    var borderHeightProperty = SimpleDoubleProperty()
    var borderHeight by borderHeightProperty

    var textSizeProperty = SimpleDoubleProperty()
    var textSize by textSizeProperty

    var fontProperty = SimpleStringProperty()
    var font: String by fontProperty

    var fontColorProperty = SimpleObjectProperty<Color>()
    var fontColor: Color by fontColorProperty

    var minProperty = SimpleDoubleProperty()
    var min by minProperty

    var maxProperty = SimpleDoubleProperty()
    var max by maxProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    var markTypeProperty = SimpleObjectProperty<AxisScalingType>()
    var axisScalingType: AxisScalingType by markTypeProperty

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor: Color by axisColorProperty

    init {
        val styleProperties = axis.styleProperties
        val scaleProperties = axis.scaleProperties

        marksDistance = styleProperties.marksDistance
        marksDistanceType = styleProperties.marksDistanceType
        borderHeight = styleProperties.borderHeight
        textSize = styleProperties.marksFont.size
        font = styleProperties.marksFont.family
        fontColor = styleProperties.marksColor
        axisColor = styleProperties.backgroundColor
        isHide = styleProperties.isVisible
        axisScalingType = scaleProperties.scalingType
        min = scaleProperties.minValue
        max = scaleProperties.maxValue
    }

    fun createChangeSet() = UpdateAxisChangeSet(
        marksDistance = marksDistance,
        marksDistanceType = marksDistanceType,
        borderHeight = borderHeight,
        textSize = textSize,
        font = font,
        fontColor = fontColor,
        axisColor = axisColor,
        isHide = isHide,
        axisScalingType = axisScalingType,
        min = min,
        max = max
    )
}