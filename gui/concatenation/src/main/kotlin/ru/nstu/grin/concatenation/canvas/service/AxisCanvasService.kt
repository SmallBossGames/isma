package ru.nstu.grin.concatenation.canvas.service

import ru.nstu.grin.concatenation.canvas.events.GetAllAxisesEvent
import ru.nstu.grin.concatenation.canvas.events.GetAxisEvent
import ru.nstu.grin.concatenation.canvas.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import tornadofx.Controller
import java.util.*

class AxisCanvasService: Controller() {
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

        axis.distanceBetweenMarks = event.distance
        axis.textSize = event.textSize
        axis.font = event.font
        axis.fontColor = event.fontColor
        axis.backGroundColor = event.axisColor
        view.redraw()
        getAllAxises()
    }
}