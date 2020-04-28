package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.function.model.ManualEnterFunctionViewModel
import tornadofx.Controller

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionController : Controller() {
    private val model: ManualEnterFunctionViewModel by inject()

    private companion object {
        const val DELIMITER = ","
    }

    fun parsePoints(): List<Point> {
        val x = model.xPoints.split(DELIMITER)
        val y = model.yPoints.split(DELIMITER)
        return x.zip(y) { a, b ->
            Point(a.toDouble(), b.toDouble())
        }
    }

    fun addFunction(points: List<Point>) {
        val function = ConcatenationFunctionDTO(
            name = model.functionName,
            points = points.mapIndexedNotNull { index, point ->
                if (index % model.step == 0) {
                    point
                } else {
                    null
                }
            },
            functionColor = model.functionColor
        )
        val xAxis = ConcatenationAxisDTO(
            name = model.xAxisName,
            backGroundColor = model.xAxisColor,
            delimeterColor = model.xDelimiterColor,
            direction = model.xDirection,
            zeroPoint = SettingsProvider.getCanvasWidth() / 2
        )
        val yAxis = ConcatenationAxisDTO(
            name = model.yAxisName,
            backGroundColor = model.yAxisColor,
            delimeterColor = model.yDelimeterColor,
            direction = model.yDirection,
            zeroPoint = SettingsProvider.getCanvasHeight() / 2
        )
        val cartesianSpace = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = xAxis,
            yAxis = yAxis
        )

        fire(
            ConcatenationFunctionEvent(cartesianSpace = cartesianSpace)
        )
    }
}