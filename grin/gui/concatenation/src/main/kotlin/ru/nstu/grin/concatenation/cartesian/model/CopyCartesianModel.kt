package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class CopyCartesianModel(
    val space: CartesianSpace
) {
    var nameProperty = SimpleStringProperty()
    var name by nameProperty

    var xAxisNameProperty = SimpleStringProperty()
    var xAxisName by xAxisNameProperty

    var yAxisNameProperty = SimpleStringProperty()
    var yAxisName by yAxisNameProperty
}