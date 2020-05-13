package ru.nstu.grin.concatenation.function.service

import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.function.events.*
import tornadofx.Controller
import java.util.*

class FunctionCanvasService : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun addFunction(event: ConcatenationFunctionEvent) {
        val cartesianSpace = event.cartesianSpace
        val found = model.cartesianSpaces.firstOrNull {
            it.xAxis.name == cartesianSpace.xAxis.name
                && it.yAxis.name == cartesianSpace.yAxis.name
        }
        if (found == null) {
            val xAxis = cartesianSpace.xAxis.let { ConcatenationAxisConverter.merge(it, it.getOrder()) }
            val yAxis = cartesianSpace.yAxis.let { ConcatenationAxisConverter.merge(it, it.getOrder()) }
            val added = CartesianSpaceConverter.merge(cartesianSpace, xAxis, yAxis)
            model.cartesianSpaces.add(added)
        } else {
            model.cartesianSpaces.remove(found)

            val functions = cartesianSpace.functions.map { ConcatenationFunctionConverter.convert(it) }
            found.merge(functions)
            model.cartesianSpaces.add(found)
        }
    }

    fun copyFunction(event: FunctionCopyQuery) {
        val oldFunction = model.cartesianSpaces.map { it.functions }.flatten().first { it.id == event.id }
        val newFunction = oldFunction.clone().copy(id = UUID.randomUUID(), name = event.name)
        val cartesianSpace = model.cartesianSpaces.first { it.functions.firstOrNull { it.id == event.id } != null }
        cartesianSpace.functions.add(newFunction)
        getAllFunctions()
    }

    fun localizeFunction(event: LocalizeFunctionEvent) {
        val cartesianSpace = model.cartesianSpaces.first { it.functions.any { it.id == event.id } }
        val function = model.cartesianSpaces.map { it.functions }.flatten().first { it.id == event.id }
        val xPoints = function.points.map { it.x }
        val yPoints = function.points.map { it.y }

        val minY = yPoints.min() ?: return
        val maxY = yPoints.max() ?: return
        val minX = xPoints.min() ?: return
        val maxX = xPoints.max() ?: return

        cartesianSpace.yAxis.settings.min = minY
        cartesianSpace.yAxis.settings.max = maxY

        cartesianSpace.xAxis.settings.min = minX
        cartesianSpace.xAxis.settings.max = maxX

        view.redraw()
    }

    fun updateFunction(event: UpdateFunctionEvent) {
        println("Function updated")
        val function = model.cartesianSpaces.map {
            it.functions
        }.flatten().first { it.id == event.id }

        function.name = event.name
        function.functionColor = event.color
        function.isHide = event.isHide
        function.lineSize = event.lineSize
        function.lineType = event.lineType
        function.mirrorSettings = event.mirrorSettings
        view.redraw()
        getAllFunctions()
    }

    fun getFunction(event: FunctionQuery) {
        val function = model.cartesianSpaces.map {
            it.functions
        }.flatten().first { it.id == event.id }
        fire(GetFunctionEvent(function))
    }

    fun getAllFunctions() {
        val functions = model.cartesianSpaces.map {
            it.functions
        }.flatten()
        val event = GetAllFunctionsEvent(functions)
        fire(event)
    }

    fun deleteFunction(event: DeleteFunctionQuery) {
        for (cartesianSpace in model.cartesianSpaces) {
            val function = cartesianSpace.functions.firstOrNull { it.id == event.id }
            if (function != null) {
                cartesianSpace.functions.remove(function)
            }
        }
        view.redraw()
        getAllFunctions()
    }

    private fun ConcatenationAxisDTO.getOrder(): Int {
        return when (direction.direction) {
            Direction.LEFT -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.LEFT || it.yAxis.direction == Direction.LEFT }
                    .size
            }
            Direction.RIGHT -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.RIGHT || it.yAxis.direction == Direction.RIGHT }
                    .size
            }
            Direction.TOP -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.TOP || it.yAxis.direction == Direction.TOP }
                    .size
            }
            Direction.BOTTOM -> {
                model.cartesianSpaces.filter { it.xAxis.direction == Direction.BOTTOM || it.yAxis.direction == Direction.BOTTOM }
                    .size
            }
        }
    }
}