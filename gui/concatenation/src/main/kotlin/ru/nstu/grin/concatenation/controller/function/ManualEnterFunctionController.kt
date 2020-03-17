package ru.nstu.grin.concatenation.controller.function

import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.controller.DeltaMarksGenerator
import ru.nstu.grin.concatenation.controller.DeltaSizeCalculator
import ru.nstu.grin.concatenation.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.model.function.ManualEnterFunctionViewModel
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

    fun addFunction(points: List<Point>, drawSize: DrawSize) {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)
        val deltaMarksGenerator = DeltaMarksGenerator()

        val function = ConcatenationFunctionDTO(
            name = model.functionName,
            points = points,
            functionColor = model.functionColor
        )
        val xAxis = ConcatenationAxisDTO(
            name = model.xAxisName,
            backGroundColor = model.xAxisColor,
            delimeterColor = model.xDelimiterColor,
            direction = model.xDirection,
            deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.xDirection.direction),
            zeroPoint = SettingsProvider.getCanvasWidth() / 2
        )
        val yAxis = ConcatenationAxisDTO(
            name = model.yAxisName,
            backGroundColor = model.yAxisColor,
            delimeterColor = model.yDelimeterColor,
            direction = model.yDirection,
            deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.yDirection.direction),
            zeroPoint = SettingsProvider.getCanvasHeight() / 2
        )
        val cartesianSpace = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = xAxis,
            yAxis = yAxis
        )

        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace,
                minAxisDelta = delta
            )
        )
    }
}