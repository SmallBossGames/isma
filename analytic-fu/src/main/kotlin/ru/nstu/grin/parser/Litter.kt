package ru.nstu.grin.parser

sealed class Litter

data class Number(
    val value: Double
) : Litter()

object PlusOperator : Litter()

object MinusOperator : Litter()

object DelOperator : Litter()

object MultiplyOperator : Litter()

object LeftBracket: Litter()

object RightBracket: Litter()