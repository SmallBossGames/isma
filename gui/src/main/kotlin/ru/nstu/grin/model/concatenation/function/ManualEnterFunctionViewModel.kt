package ru.nstu.grin.model.concatenation.function

import javafx.beans.property.SimpleStringProperty
import ru.nstu.grin.model.concatenation.function.AbstractAddFunctionModel
import tornadofx.*

class ManualEnterFunctionViewModel : AbstractAddFunctionModel() {
    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty
}