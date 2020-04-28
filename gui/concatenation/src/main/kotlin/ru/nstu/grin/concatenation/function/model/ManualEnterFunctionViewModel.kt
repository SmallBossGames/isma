package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleStringProperty
import ru.nstu.grin.concatenation.function.model.AbstractAddFunctionModel
import tornadofx.*

class ManualEnterFunctionViewModel : AbstractAddFunctionModel() {
    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty
}