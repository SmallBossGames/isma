package ru.nstu.grin.controller

import ru.nstu.grin.model.Direction
import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.view.AnalyticFunctionModel
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val pointsBuilder = PointsBuilder()

    fun addFunction(drawSize: DrawSize) {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)

        val xDirection = Direction.valueOf(model.xDirection)
        val yDirection = Direction.valueOf(model.yDirection)
        val deltaMarksGenerator = DeltaMarksGenerator()
        val functionDto = FunctionDTO(
            points = pointsBuilder.buildPoints(drawSize, model.text, model.delta),
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