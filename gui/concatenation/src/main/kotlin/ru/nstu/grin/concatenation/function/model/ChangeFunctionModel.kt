package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.ViewModel
import tornadofx.*

class ChangeFunctionModel : ViewModel() {
    var nameProperty = SimpleStringProperty()
    var name by nameProperty

    var functionColorProperty = SimpleObjectProperty<Color>()
    var functionColor by functionColorProperty

    var lineSizeProperty = SimpleStringProperty()
    var lineSize by lineSizeProperty

    var lineTypeProperty = SimpleObjectProperty<LineType>()
    var lineType by lineTypeProperty

    var isHideProperty = SimpleBooleanProperty()
    var isHide by isHideProperty
}