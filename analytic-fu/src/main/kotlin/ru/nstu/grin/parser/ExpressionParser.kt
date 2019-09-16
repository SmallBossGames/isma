package ru.nstu.grin.parser

/**
 * @author Konstantin Volivach
 */
class ExpressionParser {

    fun parse(str: String): List<Litter> {
        val memory = mutableListOf<Char>()
        val result = mutableListOf<Litter>()
        for (ch in str) {
            when {
                ch == Litters.PLUS.ch -> {
                    result.addfMemoryNotEmpty(memory)
                    result.add(PlusOperator)
                }
                ch == Litters.MINUS.ch -> {
                    result.addfMemoryNotEmpty(memory)
                    result.add(MinusOperator)
                }
                ch == Litters.DEL.ch -> {
                    result.addfMemoryNotEmpty(memory)
                    result.add(DelOperator)
                }
                ch == Litters.MULTIPLY.ch -> {
                    result.addfMemoryNotEmpty(memory)
                    result.add(MultiplyOperator)
                }
                ch == Litters.X.ch -> {
                    val last = result.last()
                    if (last is Number) {
                        result.add(MultiplyOperator)
                    }
                    result.add(X)
                }
                ch.isDigit() -> {
                    memory.add(ch)
                }
                ch == Litters.DOT.ch -> {
                    memory.add(ch)
                }
            }
        }
        return result
    }

    private fun MutableList<Litter>.addfMemoryNotEmpty(memory: List<Char>) {
        if (memory.isNotEmpty()) {
            val number = Number(
                memory.joinToString("").toDouble()
            )
            add(number)
        }
    }
}