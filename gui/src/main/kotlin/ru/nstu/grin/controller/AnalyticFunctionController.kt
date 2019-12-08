package ru.nstu.grin.controller

import ru.nstu.grin.model.Direction
import ru.nstu.grin.calculation.Calculator
import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.view.AnalyticFunctionModel
import ru.nstu.grin.model.Point
import ru.nstu.grin.model.drawable.Axis
import ru.nstu.grin.parser.ExpressionConverter
import ru.nstu.grin.parser.ExpressionParser
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val expressionConverter: ExpressionConverter = ExpressionConverter()
    private val expressionParser: ExpressionParser = ExpressionParser()

    fun addFunction(drawSize: DrawSize) {
        val functionDto = FunctionDTO(
            points = buildPoints(drawSize),
            xAxis = AxisDTO(
                color = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = Direction.valueOf(model.xDirection)
            ),
            yAxis = AxisDTO(
                color = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = Direction.valueOf(model.yDirection)
            ),
            functionColor = model.functionColor
        )
        fire(
            AddFunctionEvent(
                functionDTO = functionDto,
                minAxisDelta = calculateDeltas(drawSize)
            )
        )
    }

    private fun calculateDeltas(drawSize: DrawSize): Double {
        return (drawSize.maxX - drawSize.minX) / 100
    }

    private fun buildPoints(drawSize: DrawSize): List<Point> {
        val parse = expressionParser.parse(model.text)
        val inversePolish = expressionConverter.infixToInversePolish(parse)
        val calculator = Calculator(inversePolish)
        val result = mutableListOf<Point>()
        var current = drawSize.minX
        while (current < drawSize.maxX) {
            result.add(Point(current, calculator.calculate(current)))
            current += model.delta
        }
        return result
    }
}