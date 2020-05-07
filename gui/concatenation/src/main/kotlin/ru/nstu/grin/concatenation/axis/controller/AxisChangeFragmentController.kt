package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.canvas.events.GetAxisEvent
import tornadofx.Controller
import java.util.*

class AxisChangeFragmentController : Controller() {
    private val axisId: UUID by param()
    private val model: AxisChangeFragmentModel by inject()

    init {
        subscribe<GetAxisEvent> {
            println("Test")
            if (it.axis.id == axisId) {
                model.axisColor = it.axis.backGroundColor
                model.distanceBetweenMarks = it.axis.distanceBetweenMarks.toString()
                model.textSize = it.axis.textSize.toString()
                model.font = it.axis.font
                model.fontColor = it.axis.fontColor
            }
        }
    }
}