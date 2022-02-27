package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService

class AxisChangeFragmentController(
    private val axisChangeFragmentModel: AxisChangeFragmentModel,
    private val logFragmentModel: LogarithmicFragmentModel,
    private val service: AxisCanvasService
) {
    fun updateAxis() {
        service.updateAxis(
            axisChangeFragmentModel.axis,
            axisChangeFragmentModel.createChangeSet(),
            logFragmentModel.createChangeSet()
        )
    }
}