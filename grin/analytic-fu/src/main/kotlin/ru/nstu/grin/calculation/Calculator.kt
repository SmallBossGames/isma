package ru.nstu.grin.calculation

import ru.nstu.grin.parser.*
import ru.nstu.grin.parser.Number
import java.util.*

class Calculator(private val list: List<Litter>) {

    fun calculate(x: Double): Double {
        val stack = Stack<Double>()
        list.forEach {
            when (it) {
                is Number -> {
                    stack.push(it.value)
                }
                X -> {
                    stack.push(x)
                }
                PlusOperator -> {
                    stack.push(stack.pop() + stack.pop())
                }
                MinusOperator -> {
                    stack.push(stack.pop() - stack.pop())
                }
                DelOperator -> {
                    stack.push(stack.pop() / stack.pop())
                }
                MultiplyOperator -> {
                    stack.push(stack.pop() * stack.pop())
                }
                else -> {}
            }
        }
        return stack.pop()
    }
}