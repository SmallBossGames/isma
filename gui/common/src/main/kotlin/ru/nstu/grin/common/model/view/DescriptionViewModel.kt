package ru.nstu.grin.common.model.view

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.ViewModel
import tornadofx.*

class DescriptionViewModel : ViewModel() {
    var x: Double = 0.0
    var y: Double = 0.0

    var textProperty = SimpleStringProperty()
    var text: String by textProperty

    var sizeProperty = SimpleDoubleProperty()
    var size: Double by sizeProperty

    var textColorProperty = SimpleObjectProperty<Color>(Color.BLACK)
    var textColor by textColorProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty
}