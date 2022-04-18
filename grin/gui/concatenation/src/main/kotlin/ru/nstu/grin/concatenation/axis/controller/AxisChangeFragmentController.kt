package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService

class AxisChangeFragmentController(
    private val service: AxisCanvasService
) {
    fun updateAxis(model: AxisChangeFragmentModel) {
        service.updateAxis(model.axis, model.createChangeSet())
    }
}