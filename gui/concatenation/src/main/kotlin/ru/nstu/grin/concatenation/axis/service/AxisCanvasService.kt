package ru.nstu.grin.concatenation.axis.service

import ru.nstu.grin.concatenation.axis.events.GetAllAxisesEvent
import ru.nstu.grin.concatenation.axis.events.GetAxisEvent
import ru.nstu.grin.concatenation.axis.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import tornadofx.Controller
import java.util.*

class AxisCanvasService : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun getAxis(id: UUID) {
        val axis = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten().first { it.id == id }

        val event = GetAxisEvent(axis)
        fire(event)
    }

    fun getAllAxises() {
        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val event = GetAllAxisesEvent(axises)
        fire(event)
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
        axis.isHide = event.isHide
        view.redraw()
        getAllAxises()
    }
}