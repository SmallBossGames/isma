package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.model.UpdateCartesianDataModel
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService
import tornadofx.Controller

class ChangeCartesianController : Controller() {
    private val cartesianCanvasService: CartesianCanvasService by inject()

    fun updateCartesianSpace(model: ChangeCartesianSpaceModel) {
        val updateCartesianDataModel = UpdateCartesianDataModel(
            space = model.space,
            name = model.name,
            isShowGrid = model.isShowGrid
        )
        cartesianCanvasService.updateCartesian(updateCartesianDataModel)
    }
}