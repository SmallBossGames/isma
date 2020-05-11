package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import tornadofx.Controller
import java.util.*

class AxisCanvasController: Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    init {
        subscribe<UpdateAxisEvent> {
            updateAxis(it)
            getAllAxises()
        }
        subscribe<AxisQuery> {
            val axis = findAxisById(it.id)
            val event = GetAxisEvent(axis)
            fire(event)
        }
        subscribe<GetAllAxisQuery> {
            getAllAxises()
        }
    }

    private fun getAllAxises() {
        val axises = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten()
        val event = GetAllAxisesEvent(axises)
        fire(event)
    }

    private fun findAxisById(id: UUID): ConcatenationAxis {
        return model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten().first { it.id == id }
    }

    private fun updateAxis(event: UpdateAxisEvent) {
        val axis = model.cartesianSpaces.map {
            listOf(it.xAxis, it.yAxis)
        }.flatten().first { it.id == event.id }

        axis.distanceBetweenMarks = event.distance
        axis.textSize = event.textSize
        axis.font = event.font
        axis.fontColor = event.fontColor
        axis.backGroundColor = event.axisColor
        view.redraw()
    }
}