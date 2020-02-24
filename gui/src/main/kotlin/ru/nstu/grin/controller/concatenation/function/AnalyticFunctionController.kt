package ru.nstu.grin.controller.concatenation.function

import ru.nstu.grin.controller.DeltaMarksGenerator
import ru.nstu.grin.controller.DeltaSizeCalculator
import ru.nstu.grin.controller.PointsBuilder
import ru.nstu.grin.events.concatenation.ConcatenationFunctionEvent
import ru.nstu.grin.dto.concatenation.AxisDTO
import ru.nstu.grin.dto.concatenation.FunctionDTO
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.concatenation.function.AnalyticFunctionModel
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val pointsBuilder = PointsBuilder()

    fun addFunction(drawSize: DrawSize) {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)

        val deltaMarksGenerator = DeltaMarksGenerator()
        val functionDto = FunctionDTO(
            name = model.functionName,
            points = pointsBuilder.buildPoints(drawSize, model.text, model.delta),
            xAxis = AxisDTO(
                color = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = model.xDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.xDirection.direction)
            ),
            yAxis = AxisDTO(
                color = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = model.yDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.yDirection.direction)
            ),
            functionColor = model.functionColor
        )
        fire(
            ConcatenationFunctionEvent(
                functionDTO = functionDto,
                minAxisDelta = delta
            )
        )
    }
}