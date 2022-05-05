package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

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