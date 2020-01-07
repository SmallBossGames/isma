package ru.nstu.grin.controller

import ru.nstu.grin.controller.events.AddFunctionEvent
import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.view.ManualEnterFunctionViewModel
import ru.nstu.grin.model.Point
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
        val xDirection = Direction.valueOf(model.xDirection)
        val yDirection = Direction.valueOf(model.yDirection)
        val deltaMarksGenerator = DeltaMarksGenerator()

        val functionDto = FunctionDTO(
            points = points,
            xAxis = AxisDTO(
                color = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = xDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, xDirection)
            ),
            yAxis = AxisDTO(
                color = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = yDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, yDirection)
            ),
            functionColor = model.functionColor
        )
        fire(AddFunctionEvent(functionDTO = functionDto, minAxisDelta = delta))
    }
}