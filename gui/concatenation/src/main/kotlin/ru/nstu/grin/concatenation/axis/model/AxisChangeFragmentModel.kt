package ru.nstu.grin.concatenation.axis.model

import javafx.beans.property.*
import javafx.scene.paint.Color
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

    var axisColorProperty = SimpleObjectProperty<Color>()
    var axisColor by axisColorProperty
}