package ru.nstu.grin.parser

import java.util.*

/**
 * @author Konstantin Volivach
 */
class ExpressionConverter {

    fun infixToInversePolish(input: List<Litter>): List<Litter> {
        val outArray = mutableListOf<Litter>()
        val operationStack = Stack<Litter>()

        for (obj in input) {
            when (obj) {
                is Number -> {
                    outArray.add(obj)
                }
                X -> {
                    outArray.add(obj)
                }
                PlusOperator -> {
                    if (operationStack.isNotEmpty()) {
                        val peek = operationStack.peek()
                        if (peek == MultiplyOperator ||
                            peek == DelOperator || peek == MinusOperator
                        ) {
                            val pop = operationStack.pop()
                            outArray.add(pop)
                        }
                    }
                    operationStack.push(obj)
                }
                MinusOperator -> {
                    if (operationStack.isNotEmpty()) {
                        val peek = operationStack.peek()
                        if (peek == MultiplyOperator || peek == DelOperator ||
                            peek == PlusOperator
                        ) {
                            val pop = operationStack.pop()
                            outArray.add(pop)
                        }
                    }
                    operationStack.push(obj)
                }
                DelOperator -> {
                    if (operationStack.isNotEmpty()) {
                        val peek = operationStack.peek()
                        if (peek == MultiplyOperator) {
                            val pop = operationStack.pop()
                            outArray.add(pop)
                        }
                    }
                    operationStack.add(obj)
                }
                MultiplyOperator -> {
                    if (operationStack.isNotEmpty()) {
                        val peek = operationStack.peek()
                        if (peek == DelOperator) {
                            val pop = operationStack.pop()
                            outArray.add(pop)
                        }
                    }
                    operationStack.add(obj)
                }
                LeftBracket -> {
                    operationStack.add(obj)
                }
                RightBracket -> {
                    var op = operationStack.pop()
                    while (op != LeftBracket) {
                        outArray.add(op)
                        op = operationStack.pop()
                    }
                    operationStack.clear()
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