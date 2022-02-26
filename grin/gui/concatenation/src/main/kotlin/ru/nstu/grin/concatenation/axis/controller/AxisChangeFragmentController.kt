package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService
import tornadofx.Controller
import tornadofx.Scope

class AxisChangeFragmentController(
    override val scope: Scope,
    private val axisChangeFragmentModel: AxisChangeFragmentModel,
    private val logFragmentModel: LogarithmicFragmentModel,
) : Controller() {
    private val service: AxisCanvasService by inject()

    fun updateAxis() {
        service.updateAxis(
            axisChangeFragmentModel.axis,
            axisChangeFragmentModel.createChangeSet(),
            logFragmentModel.createChangeSet()
        )
    }
}