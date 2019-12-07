package ru.nstu.grin.model.view

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color
import ru.nstu.grin.utils.ColorUtils
import tornadofx.*

class ArrowViewModel : ViewModel() {
    var x: Double = 0.0
    var y: Double = 0.0

    var arrowColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var arrowColor by arrowColorProperty
}