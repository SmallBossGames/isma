package ru.nstu.grin.parser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExpressionParserTest {

    private lateinit var parser: ExpressionParser

    @BeforeEach
    fun setUp() {
        parser = ExpressionParser()
    }

    @Test
    fun testSimpleExpression() {
        val str = "2+2"
        val result = parser.parse(str)
        assertEquals(Number(2.0), result[0])
        assertEquals(PlusOperator, result[1])
        assertEquals(Number(2.0), result[2])
    }

    @Test
    fun testSimpleDoublePlus() {
        val str = "2+2+2"
        val result = parser.parse(str)
        assertEquals(Number(2.0), result[0])
        assertEquals(PlusOperator, result[1])
        assertEquals(Number(2.0), result[2])
        assertEquals(PlusOperator, result[3])
        assertEquals(Number(2.0), result[4])
    }

    @Test
    fun testWithDoubleAndX() {
        val str = "2x+2.5x"
        val result = parser.parse(str)
        assertEquals(Number(2.0), result[0])
        assertEquals(MultiplyOperator, result[1])
        assertEquals(X, result[2])
        assertEquals(PlusOperator, result[3])
        assertEquals(Number(2.5), result[4])
        assertEquals(MultiplyOperator, result[5])
        assertEquals(X, result[6])
    }
}