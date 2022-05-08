package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService

class CopyCartesianViewModel(
    private val space: CartesianSpace,
    private val cartesianCanvasService: CartesianCanvasService
) {
    var nameProperty = SimpleStringProperty()
    var name by nameProperty

    var xAxisNameProperty = SimpleStringProperty()
    var xAxisName by xAxisNameProperty

    var yAxisNameProperty = SimpleStringProperty()
    var yAxisName by yAxisNameProperty

    fun copy() {
        val data = CartesianCopyDataModel(
            origin = space,
            name = name,
            xAxisName = xAxisName,
            yAxisName = yAxisName
        )
        cartesianCanvasService.copyCartesian(data)
    }
}