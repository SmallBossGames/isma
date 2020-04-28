package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.controller.PointsBuilder
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.function.model.AnalyticFunctionModel
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val pointsBuilder = PointsBuilder()

    fun addFunction(drawSize: DrawSize) {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)

        val deltaMarksGenerator = DeltaMarksGenerator()
        val function = ConcatenationFunctionDTO(
            name = model.functionName,
            points = pointsBuilder.buildPoints(drawSize, model.text, model.delta).mapIndexedNotNull { index, point ->
                if (index % model.step == 0) {
                    point
                } else {
                    null
                }
            },
            functionColor = model.functionColor
        )

        val cartesianSpace = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                name = model.xAxisName,
                backGroundColor = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = model.xDirection,
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
            ),
            yAxis = ConcatenationAxisDTO(
                name = model.yAxisName,
                backGroundColor = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = model.yDirection,
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace
            )
        )
    }
}