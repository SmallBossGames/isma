package ru.nstu.grin.concatenation.controller.function

import ru.nstu.grin.common.controller.PointsBuilder
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.controller.DeltaMarksGenerator
import ru.nstu.grin.concatenation.controller.DeltaSizeCalculator
import ru.nstu.grin.concatenation.dto.AxisDTO
import ru.nstu.grin.concatenation.dto.FunctionDTO
import ru.nstu.grin.concatenation.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.model.function.AnalyticFunctionModel
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
                backGroundColor = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = model.xDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.xDirection.direction)
            ),
            yAxis = AxisDTO(
                backGroundColor = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = model.yDirection,
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, model.yDirection.direction)
            ),
            functionColor = model.functionColor
        )
        fire(
            ConcatenationFunctionEvent(
                function = functionDto,
                minAxisDelta = delta
            )
        )
    }
}