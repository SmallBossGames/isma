package ru.nstu.grin.simple.model.function

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class ManualFunctionModel : AbstractFunctionModel() {
    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty
}