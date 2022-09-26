package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import javafx.scene.text.Font
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService

class AxisChangeFragmentViewModel(
    private val service: AxisCanvasService,
    private val axis: ConcatenationAxis
) {
    val nameProperty = SimpleStringProperty()
    var name: String by nameProperty

    val directionProperty = SimpleObjectProperty<Direction>()
    var direction: Direction by directionProperty

    val distanceBetweenMarksProperty = SimpleDoubleProperty()
    var marksDistance by distanceBetweenMarksProperty

    val marksDistanceTypeProperty = SimpleObjectProperty<MarksDistanceType>()
    var marksDistanceType: MarksDistanceType by marksDistanceTypeProperty

    val borderHeightProperty = SimpleDoubleProperty()
    var borderHeight by borderHeightProperty

    val textSizeProperty = SimpleDoubleProperty()
    var textSize by textSizeProperty

    val fontProperty = SimpleStringProperty()
    var font: String by fontProperty

    val fontColorProperty = SimpleObjectProperty<Color>()
    var fontColor: Color by fontColorProperty

    val minProperty = SimpleDoubleProperty()
    var min by minProperty

    val maxProperty = SimpleDoubleProperty()
    var max by maxProperty

    val isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    val markTypeProperty = SimpleObjectProperty<AxisScalingType>()
    var axisScalingType: AxisScalingType by markTypeProperty

    val axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor: Color by axisColorProperty

    init {
        val axisName = axis.name
        val axisDirection = axis.direction
        val styleProperties = axis.styleProperties
        val scaleProperties = axis.scaleProperties

        name = axisName
        direction = axisDirection
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

    fun commit() {
        service.updateAxis(axis, createChangeSet())
    }

    private fun createChangeSet() = UpdateAxisChangeSet(
        name = name,
        direction = direction,
        styleProperties = AxisStyleProperties(
            backgroundColor = axisColor,
            borderHeight = borderHeight,
            marksDistanceType = marksDistanceType,
            marksDistance = marksDistance,
            marksColor = fontColor,
            marksFont = Font.font(font, textSize),
            isVisible = isHide
        ),
        scaleProperties = AxisScaleProperties(
            minValue = min,
            maxValue = max,
        )
    )


}