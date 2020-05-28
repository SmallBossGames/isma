package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.*
import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.*

class AxisChangeFragmentModel : ViewModel() {
    var distanceBetweenMarksProperty = SimpleStringProperty(this, "distanceBeetweenMarks", "24.0")
    var distanceBetweenMarks by distanceBetweenMarksProperty

    var textSizeProperty = SimpleStringProperty(this, "textSizeProperty", "12.0")
    var textSize by textSizeProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty

    var fontColorProperty = SimpleObjectProperty<Color>()
    var fontColor by fontColorProperty

    var minProperty = SimpleDoubleProperty(this, "minProperty", 0.0)
    var min by minProperty

    var maxProperty = SimpleDoubleProperty(this, "maxProperty", 0.0)
    var max by maxProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    var markTypeProperty = SimpleObjectProperty<AxisMarkType>(AxisMarkType.LINEAR)
    var axisMarkType by markTypeProperty

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor by axisColorProperty
}