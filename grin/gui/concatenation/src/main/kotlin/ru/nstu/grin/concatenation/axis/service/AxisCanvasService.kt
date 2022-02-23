package ru.nstu.grin.concatenation.axis.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import tornadofx.Controller

class AxisCanvasService : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun getAllAxises() {
        coroutineScope.launch {
            model.reportAxesListUpdate()
        }
    }

    fun updateAxis(event: UpdateAxisEvent) {
        val axis = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten().first { it.id == event.id }

        axis.settings.isLogarithmic = event.axisMarkType == AxisMarkType.LOGARITHMIC

        if (event.axisMarkType == AxisMarkType.LINEAR) {
            axis.settings.isLogarithmic = false
            axis.settings.isOnlyIntegerPow = false
        }

        axis.axisMarkType = event.axisMarkType
        axis.distanceBetweenMarks = event.distance
        axis.textSize = event.textSize
        axis.font = event.font
        axis.fontColor = event.fontColor
        axis.backGroundColor = event.axisColor
        axis.settings.logarithmBase = event.logarithmBase
        axis.settings.isOnlyIntegerPow = event.isOnlyIntegerPow
        axis.settings.integerStep = event.integerStep
        axis.settings.min = event.min
        axis.settings.max = event.max
        axis.isHide = event.isHide
        view.redraw()
        getAllAxises()
    }
}