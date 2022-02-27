package ru.nstu.grin.concatenation.description.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Description
import tornadofx.getValue
import tornadofx.setValue

class ChangeDescriptionModel(
    val description: Description
) {

    var textProperty = SimpleStringProperty()
    var text by textProperty

    var textSizeProperty = SimpleStringProperty(this, "textSizeProperty", "12.0")
    var textSize by textSizeProperty

    var colorProperty = SimpleObjectProperty<Color>()
    var color by colorProperty

    var fontProperty = SimpleStringProperty()
    var font by fontProperty

    init {
        text = description.text
        textSize = description.textSize.toString()
        color = description.color
        font = description.font
    }
}