package ru.nstu.grin.common.controller

import ru.nstu.grin.calculation.Calculator
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.parser.ExpressionConverter
import ru.nstu.grin.parser.ExpressionParser

class PointsBuilder {
    private val expressionConverter: ExpressionConverter = ExpressionConverter()
    private val expressionParser: ExpressionParser = ExpressionParser()

    fun buildPoints(drawSize: DrawSize, text: String, delta: Double): List<Point> {
        val parse = expressionParser.parse(text)
        val inversePolish = expressionConverter.infixToInversePolish(parse)
        val calculator = Calculator(inversePolish)
        val result = mutableListOf<Point>()
        var current = drawSize.minX
        while (current < drawSize.maxX) {
            result.add(Point(current, calculator.calculate(current)))
            current += delta
        }
        return result
    }
}