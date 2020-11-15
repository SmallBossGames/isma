package ru.nstu.grin.concatenation.description.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import tornadofx.ViewModel
import tornadofx.*

class ChangeDescriptionModel : ViewModel() {
    var textProperty = SimpleStringProperty()
    var text by textProperty

    var textSizeProperty = SimpleStringProperty(this, "textSizeProperty", "12.0")
    var textSize by textSizeProperty

    var colorProperty = SimpleObjectProperty<Color>()
    var color by colorProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty
}