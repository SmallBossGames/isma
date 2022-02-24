package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.*
import javafx.scene.paint.Color
import tornadofx.*

class AxisChangeFragmentModel : ViewModel() {
    val axis: ConcatenationAxis by param()

    var distanceBetweenMarksProperty = SimpleStringProperty(this, "distanceBeetweenMarks", "24.0")
    var distanceBetweenMarks by distanceBetweenMarksProperty

    var textSizeProperty = SimpleStringProperty(this, "textSizeProperty", "12.0")
    var textSize by textSizeProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty

    var fontColorProperty = SimpleObjectProperty<Color>()
    var fontColor by fontColorProperty

    var minProperty = SimpleStringProperty(this, "minProperty", "0.0")
    var min by minProperty

    var maxProperty = SimpleStringProperty(this, "maxProperty", "0.0")
    var max by maxProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    var markTypeProperty = SimpleObjectProperty<AxisMarkType>(AxisMarkType.LINEAR)
    var axisMarkType by markTypeProperty

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor by axisColorProperty

    init {
        axisColor = axis.backGroundColor
        distanceBetweenMarks = axis.distanceBetweenMarks.toString()
        textSize = axis.textSize.toString()
        font = axis.font
        fontColor = axis.fontColor
        isHide = axis.isHide
        min = axis.settings.min.toString()
        max = axis.settings.max.toString()
    }

    fun createChangeSet() = UpdateAxisChangeSet(
        id = axis.id,
        distance = distanceBetweenMarks.toDouble(),
        textSize = textSize.toDouble(),
        font = font,
        fontColor = fontColor,
        axisColor = axisColor,
        isHide = isHide,
        axisMarkType = axisMarkType,
        min = min.toDouble(),
        max = max.toDouble()
    )
}