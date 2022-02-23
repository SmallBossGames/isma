package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.Controller

class AxisChangeFragmentController : Controller() {
    private val axis: ConcatenationAxis by param()
    private val model: AxisChangeFragmentModel by inject()
    private val logarithmicTypeModel: LogarithmicTypeModel by inject()

    init {
        model.axisColor = axis.backGroundColor
        model.distanceBetweenMarks = axis.distanceBetweenMarks.toString()
        model.textSize = axis.textSize.toString()
        model.font = axis.font
        model.fontColor = axis.fontColor
        model.isHide = axis.isHide
        model.min = axis.settings.min.toString()
        model.max = axis.settings.max.toString()
    }

    fun updateAxis() {
        val event = UpdateAxisEvent(
            id = axis.id,
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
            min = model.min.toDouble(),
            max = model.max.toDouble()
        )
        fire(event)
    }
}