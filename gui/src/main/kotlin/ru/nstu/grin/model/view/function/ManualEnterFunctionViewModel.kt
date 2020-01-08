package ru.nstu.grin.model.view.function

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class ManualEnterFunctionViewModel : AbstractAddFunctionModel() {
    var xPointsProperty = SimpleStringProperty(this, "xPoints", "")
    var xPoints: String by xPointsProperty

    var yPointsProperty = SimpleStringProperty(this, "yPoints", "")
    var yPoints: String by yPointsProperty
}