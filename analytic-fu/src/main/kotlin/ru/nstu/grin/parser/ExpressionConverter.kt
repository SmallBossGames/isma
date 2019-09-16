package ru.nstu.grin.parser

import java.util.*

/**
 * @author Konstantin Volivach
 */
class ExpressionConverter {

    fun infixToInversePolish(input: List<Any>): List<Any> {
        val outArray = mutableListOf<Any>()
        val operationStack = Stack<Char>()
        for (obj in input) {
            when (obj) {
                is Char -> {
                    when (obj) {
                        Litters.MULTIPLY.ch -> {
                            if (operationStack.isNotEmpty()) {
                                val peek = operationStack.peek()
                                if (peek == Litters.DEL.ch) {
                                    val pop = operationStack.pop()
                                    outArray.add(pop)
                                }
                            }
                            operationStack.add(obj)
                        }
                        Litters.DEL.ch -> {
                            if (operationStack.isNotEmpty()) {
                                val peek = operationStack.peek()
                                if (peek == Litters.MULTIPLY.ch) {
                                    val pop = operationStack.pop()
                                    outArray.add(pop)
                                }
                            }
                            operationStack.add(obj)
                        }
                        Litters.PLUS.ch -> {
                            if (operationStack.isNotEmpty()) {
                                val peek = operationStack.peek()
                                if (peek == Litters.MULTIPLY.ch ||
                                    peek == Litters.DEL.ch || peek == Litters.MINUS.ch
                                ) {
                                    val pop = operationStack.pop()
                                    outArray.add(pop)
                                }
                            }
                            operationStack.push(obj)
                        }
                        Litters.MINUS.ch -> {
                            if (operationStack.isNotEmpty()) {
                                val peek = operationStack.peek()
                                if (peek == Litters.MULTIPLY.ch || peek == Litters.DEL.ch ||
                                    peek == Litters.PLUS.ch
                                ) {
                                    val pop = operationStack.pop()
                                    outArray.add(pop)
                                }
                            }
                            operationStack.push(obj)
                        }
                        Litters.LEFT_BRACKET.ch -> {
                            operationStack.push(obj)
                        }
                        Litters.RIGHT_BRACKET.ch -> {
                            var op = operationStack.pop()
                            while (op != Litters.LEFT_BRACKET.ch) {
                                outArray.add(op)
                                op = operationStack.pop()
                            }
                            operationStack.clear()
                        }
                    }
                }
                is Double -> {
                    outArray.add(obj)
                }
            }
        }
        while (operationStack.isNotEmpty()) {
            val pop = operationStack.pop()
            outArray.add(pop)
        }
        return outArray
    }
}