package ru.nstu.grin.controller

import ru.nstu.grin.Direction
import ru.nstu.grin.calculation.Calculator
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.AnalyticFunctionModel
import ru.nstu.grin.model.Point
import ru.nstu.grin.parser.ExpressionConverter
import ru.nstu.grin.parser.ExpressionParser
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val expressionConverter: ExpressionConverter = ExpressionConverter()
    private val expressionParser: ExpressionParser = ExpressionParser()

    fun addFunction() {
        val functionDto = FunctionDTO(
            points = buildPoints(),
            minX = model.minX,
            maxX = model.maxX,
            minY = model.minY,
            maxY = model.maxY,
            xDirection = Direction.valueOf(model.xDirection),
            yDirection = Direction.valueOf(model.yDirection),
            functionColor = model.functionColor,
            xAxisColor = model.xAxisColor,
            yAxisColor = model.yAxisColor
        )
        fire(
            AddFunctionEvent(
                functionDTO = functionDto
            )
        )
    }

    private fun buildPoints(): List<Point> {
        val parse = expressionParser.parse(model.text)
        val inversePolish = expressionConverter.infixToInversePolish(parse)
        val calculator = Calculator(inversePolish)
        val result = mutableListOf<Point>()
        var currentX = model.minX
        while (currentX < model.maxX) {
            result.add(Point(currentX, calculator.calculate(currentX)))
            currentX += model.delta
        }
        return result
    }
}