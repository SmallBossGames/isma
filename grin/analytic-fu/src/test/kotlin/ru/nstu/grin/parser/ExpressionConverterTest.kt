package ru.nstu.grin.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExpressionConverterTest {
    private lateinit var expressionConverter: ExpressionConverter
    private lateinit var expressionParser: ExpressionParser

    @BeforeEach
    fun setUp() {
        expressionParser = ExpressionParser()
        expressionConverter = ExpressionConverter()
    }

    @Test
    fun testSimpleExpression() {
        val str = "2+2"
        val list = expressionParser.parse(str)
        val inversePolish = expressionConverter.infixToInversePolish(list)
        assertEquals(Number(2.0), inversePolish[0])
        assertEquals(Number(2.0), inversePolish[1])
        assertEquals(PlusOperator, inversePolish[2])
    }
}