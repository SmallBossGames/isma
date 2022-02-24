package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService
import tornadofx.Controller

class AxisChangeFragmentController : Controller() {
    private val axisChangeFragmentModel: AxisChangeFragmentModel by inject(params = params)
    private val logFragmentModel: LogarithmicTypeModel by inject(params = params)
    private val service: AxisCanvasService by inject()

    fun updateAxis() {
        service.updateAxis(
            axisChangeFragmentModel.axis,
            axisChangeFragmentModel.createChangeSet(),
            logFragmentModel.createChangeSet()
        )
    }
}