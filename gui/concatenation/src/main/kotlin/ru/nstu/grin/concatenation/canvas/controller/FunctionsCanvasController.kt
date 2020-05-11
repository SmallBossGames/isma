package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import tornadofx.Controller

class FunctionsCanvasController : Controller() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()

    init {
        subscribe<ConcatenationFunctionEvent> { event ->
            addConcatenationFunction(event)
        }
        subscribe<UpdateFunctionEvent> {
            updateFunction(it)
            getAllFunctions()
        }
        subscribe<FunctionQuery> { event ->
            val function = model.cartesianSpaces.map {
                it.functions
            }.flatten().first { it.id == event.id }
            fire(GetFunctionEvent(function))
        }
        subscribe<GetAllFunctionsQuery> {
            getAllFunctions()
        }
        subscribe<DeleteFunctionQuery> {
            deleteFunction(it)
            getAllFunctions()
        }
    }

    private fun addConcatenationFunction(event: ConcatenationFunctionEvent) {
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

    private fun updateFunction(event: UpdateFunctionEvent) {
        println("Function updated")
        val function = model.cartesianSpaces.map {
            it.functions
        }.flatten().first { it.id == event.id }

        function.name = event.name
        function.functionColor = event.color
        function.isHide = event.isHide
        function.lineSize = event.lineSize
        function.lineType = event.lineType
        view.redraw()
    }

    private fun getAllFunctions() {
        val functions = model.cartesianSpaces.map {
            it.functions
        }.flatten()
        val event = GetAllFunctionsEvent(functions)
        fire(event)
    }

    private fun deleteFunction(event: DeleteFunctionQuery) {
        for (cartesianSpace in model.cartesianSpaces) {
            val function = cartesianSpace.functions.firstOrNull { it.id == event.id }
            if (function != null) {
                cartesianSpace.functions.remove(function)
            }
        }
        view.redraw()
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