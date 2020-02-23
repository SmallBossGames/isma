package ru.nstu.grin.model.simple.function

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import ru.nstu.grin.utils.ColorUtils
import tornadofx.ViewModel
import tornadofx.getValue
import tornadofx.setValue

class ManualFunctionModel : ViewModel() {
    var functionNameProperty = SimpleStringProperty(this, "functionName", "")
    var functionName: String by functionNameProperty

    var functionColorProperty = SimpleObjectProperty<Color>(ColorUtils.getRandomColor())
    var functionColor: Color by functionColorProperty

    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty
}