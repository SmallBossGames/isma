package ru.nstu.grin.model.simple.function

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.utils.ColorUtils
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.setValue

abstract class AbstractFunctionModel: ViewModel() {
    var nameProperty = SimpleStringProperty(this, "functionName", "")
    var name: String by nameProperty

    var colorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var color: Color by colorProperty
}