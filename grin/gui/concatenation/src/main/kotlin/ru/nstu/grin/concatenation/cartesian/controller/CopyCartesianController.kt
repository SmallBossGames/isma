package ru.nstu.grin.concatenation.cartesian.controller

import ru.nstu.grin.concatenation.cartesian.model.CartesianCopyDataModel
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianModel
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService

class CopyCartesianController(
    private val cartesianCanvasService: CartesianCanvasService
) {
    fun copy(model: CopyCartesianModel) {
        val data = CartesianCopyDataModel(
            origin = model.space,
            name = model.name,
            xAxisName = model.xAxisName,
            yAxisName = model.yAxisName
        )
        cartesianCanvasService.copyCartesian(data)
    }
}