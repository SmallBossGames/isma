package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.events.GetAxisEvent
import ru.nstu.grin.concatenation.axis.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.Controller
import java.util.*

class AxisChangeFragmentController : Controller() {
    private val axisId: UUID by param()
    private val model: AxisChangeFragmentModel by inject()
    private val logarithmicTypeModel: LogarithmicTypeModel by inject()

    init {
        subscribe<GetAxisEvent> {
            if (it.axis.id == axisId) {
                model.axisColor = it.axis.backGroundColor
                model.distanceBetweenMarks = it.axis.distanceBetweenMarks.toString()
                model.textSize = it.axis.textSize.toString()
                model.font = it.axis.font
                model.fontColor = it.axis.fontColor
                model.isHide = it.axis.isHide
                model.min = it.axis.settings.min
                model.max = it.axis.settings.max
            }
        }
    }

    fun updateAxis() {
        val event = UpdateAxisEvent(
            id = axisId,
            distance = model.distanceBetweenMarks.toDouble(),
            textSize = model.textSize.toDouble(),
            font = model.font,
            fontColor = model.fontColor,
            axisColor = model.axisColor,
            isHide = model.isHide,
            axisMarkType = model.axisMarkType,
            logarithmBase = logarithmicTypeModel.logarithmBase,
            isOnlyIntegerPow = logarithmicTypeModel.isOnlyIntegerPow,
            integerStep = logarithmicTypeModel.integerStep,
            min = model.min,
            max = model.max
        )
        fire(event)
    }
}