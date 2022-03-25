package ru.nstu.grin.concatenation.function.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData

class FunctionCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun addFunction(cartesianSpace: CartesianSpaceDTO) {
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

    fun copyFunction(originFunction: ConcatenationFunction, newName: String = originFunction.name) {
        val newFunction = originFunction.clone().copy(name = newName)
        val cartesianSpace = model.cartesianSpaces.first { it.functions.contains(originFunction) }
        cartesianSpace.functions.add(newFunction)

        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

    fun updateFunction(event: UpdateFunctionData) {
        event.function.apply {
            name = event.name
            functionColor = event.color
            isHide = event.isHide
            lineSize = event.lineSize
            lineType = event.lineType
            mirrorDetails = event.mirrorDetails
        }

        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

    fun deleteFunction(function: ConcatenationFunction) {
        model.cartesianSpaces.forEach {
            it.functions.remove(function)
        }

        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

    private fun ConcatenationAxisDTO.getOrder(): Int {
        return when (direction) {
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