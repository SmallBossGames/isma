package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.setValue
import ru.isma.javafx.extensions.helpers.getValue
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService

class ChangeCartesianSpaceViewModel(
    private val space: CartesianSpace,
    private val cartesianCanvasService: CartesianCanvasService
) {
    val nameProperty = SimpleStringProperty("")
    var name:String by nameProperty

    val isShowGridProperty = SimpleBooleanProperty()
    var isShowGrid by isShowGridProperty

    init {
        name = space.name
        isShowGrid = space.isShowGrid
    }

    fun commit() {
        val updateCartesianDataModel = UpdateCartesianDataModel(
            space = space,
            name = name,
            isShowGrid = isShowGrid
        )
        cartesianCanvasService.updateCartesian(updateCartesianDataModel)
    }
}