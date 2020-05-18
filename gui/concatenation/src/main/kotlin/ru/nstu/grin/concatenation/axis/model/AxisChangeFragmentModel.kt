package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.*
import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.*

class AxisChangeFragmentModel : ViewModel() {
    var distanceBetweenMarksProperty = SimpleStringProperty()
    var distanceBetweenMarks by distanceBetweenMarksProperty

    var textSizeProperty = SimpleStringProperty()
    var textSize by textSizeProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty

    var fontColorProperty = SimpleObjectProperty<Color>()
    var fontColor by fontColorProperty

    var minProperty = SimpleDoubleProperty()
    var min by minProperty

    var maxProperty = SimpleDoubleProperty()
    var max by maxProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty

    var markTypeProperty = SimpleObjectProperty<AxisMarkType>(AxisMarkType.LINEAR)
    var axisMarkType by markTypeProperty

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor by axisColorProperty
}