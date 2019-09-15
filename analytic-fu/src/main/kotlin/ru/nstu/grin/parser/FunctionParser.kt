package ru.nstu.grin.parser

import java.util.*

/**
 * @author Konstantin Volivach
 */
class FunctionParser {


    fun parse(str: String) {
        val outStack = mutableListOf<Char>()
        val operationStack = Stack<Char>()
        for (ch in str) {
            when {
                ch.isDigit() -> {
                    outStack.add(ch)
                }
                ch == '*' -> {
                    operationStack.add(ch)
                }
                ch == '-' -> {
                    if (operationStack.isNotEmpty()) {
                        val peek = operationStack.peek()
                        if (peek == '*' || peek == '\\' || peek == '+') {
                            val pop = operationStack.pop()
                            outStack.add(pop)
                        }
                    }
                    operationStack.push(ch)
                }
                ch == '(' -> {
                    operationStack.push(ch)
                }
                ch == ')' -> {
                    var op = operationStack.pop()
                    while (op != '(') {
                        outStack.add(op)
                        op = operationStack.pop()
                    }
                    operationStack.clear()
                }
            }
        }
        while (operationStack.isNotEmpty()) {
            val pop = operationStack.pop()
            outStack.add(pop)
        }
    }
}