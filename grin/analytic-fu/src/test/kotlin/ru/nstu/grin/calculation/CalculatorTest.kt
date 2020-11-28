package ru.nstu.grin.calculation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.nstu.grin.parser.ExpressionConverter
import ru.nstu.grin.parser.ExpressionParser

internal class CalculatorTest {

    private lateinit var converter: ExpressionConverter
    private lateinit var parser: ExpressionParser

    @BeforeEach
    fun setUp() {
        converter = ExpressionConverter()
        parser = ExpressionParser()
    }

    @Test
    fun simpleCalculation() {
        val str = "2+2"
        val res = converter.infixToInversePolish(parser.parse(str))

        val calculator = Calculator(res)
        assertEquals(4.0, calculator.calculate(0.0))
    }

    @Test
    fun diffCalculation() {
        val str = "2x+2.5x"
        val res = converter.infixToInversePolish(parser.parse(str))

        val calculator = Calculator(res)
        assertEquals(4.5, calculator.calculate(1.0))
    }
}