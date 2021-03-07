package ru.nstu.isma.hsm.service

import ru.nstu.isma.hsm.`var`.HMConst
import ru.nstu.isma.hsm.`var`.HMUnnamedConst
import ru.nstu.isma.hsm.exp.EXPFunctionOperand
import ru.nstu.isma.hsm.exp.EXPOperand
import ru.nstu.isma.hsm.exp.EXPOperator
import ru.nstu.isma.hsm.exp.HMExpression
import java.util.*
import kotlin.math.*

/**
 * by
 * Bessonov Alex.
 * Date: 14.11.13 Time: 0:21
 */
object PDEInitialValueCalculator {
    private val availableFunctions = HashSet<String?>()
    fun calculate(c: HMConst?): Double? {
        if (isCalculated(c) || c is HMUnnamedConst) {
            return c!!.value
        }
        val constExp = c!!.rightPart
        validateExpression(constExp)
        avoidLoopCheck(constExp, null)
        val value = calcExpression(constExp)!!
        c.setValue(value)
        return value
    }

    private fun validateExpression(exp: HMExpression?) {
        val tokens = exp!!.tokens
        // проверяем что все токены - константы или функции
        // проверяем что калькулятор "знает" применяемые функции
        for (t in tokens) {
            if (t is EXPFunctionOperand) {
                val f = t
                if (!availableFunctions.contains(f.name)) {
                    // TODO семантика
                    throw RuntimeException("Calculator don't undastand function " + f.name)
                }
                for (fExp in f.args) {
                    validateExpression(fExp)
                }
            } else if (t is EXPOperand) {
                val `var` = t.variable
                if (`var` !is HMConst) {
                    // TODO обработка ошибки (семантика)
                    throw RuntimeException("Calculator don't undastand variable " + `var`?.code)
                }
            }
        }
    }

    private fun avoidLoopCheck(expression: HMExpression?, prevCheck: HashSet<String?>?) {
        val current = HashSet<String?>()
        if (prevCheck != null) {
            for (s in prevCheck) {
                current.add(s)
            }
        }
        val tokens = expression!!.tokens
        for (token in tokens) {
            if (token is EXPFunctionOperand) {
                for (fExp in token.args) {
                    avoidLoopCheck(fExp, current)
                }
            } else if (token is EXPOperand) {
                val `var` = token.variable as HMConst
                if (current.contains(`var`.code)) {
                    // TODO получили петлю -  семантика обработка ошибки
                    throw RuntimeException("Const contain loop!")
                } else {
                    if (`var` !is HMUnnamedConst && `var`.value == null) {
                        avoidLoopCheck(`var`.rightPart, current)
                    }
                }
            }
        }
    }

    private fun calcExpression(expression: HMExpression?): Double? {
        val tokens = expression!!.tokens
        val stack = Stack<Double>()
        for (token in tokens) {
            when (token) {
                is EXPFunctionOperand -> {
                    stack.push(calculateFunction(token))
                }
                is EXPOperand -> {
                    val c = token.variable as HMConst
                    stack.push(calculate(c))
                }
                is EXPOperator -> {
                    doOperation(stack, token)
                }
            }
        }
        return stack.pop()
    }

    private fun calculateFunction(function: EXPFunctionOperand): Double {
        val args: MutableList<Double?> = LinkedList()
        for (exp in function.args) {
            args.add(calcExpression(exp))
        }
        return when (function.name) {
            "cos" -> {
                cos(Math.toRadians(args[0]!!))
            }
            "sin" -> {
                sin(Math.toRadians(args[0]!!))
            }
            "exp" -> {
                exp(args[0]!!)
            }
            "pow" -> {
                (args[0]!!).pow(args[1]!!)
            }
            "sqrt" -> {
                sqrt(args[0]!!)
            }
            else -> {
                throw RuntimeException("I undastand function " + function.name)
            }
        }
    }

    private fun doOperation(values: Stack<Double>, o: EXPOperator) {
        var result = 0.0
        if (o.type != EXPOperator.Type.ALGEBRAIC) {
            // TODO семантическая ошибка
        }
        if (o.arity == EXPOperator.ArityType.BINARY) {
            val v1 = values.pop()
            val v2 = values.pop()
            val c = o.code
            when (c) {
                EXPOperator.Code.ADDITION -> result = v1 + v2
                EXPOperator.Code.DIVISION -> result = v2 / v1
                EXPOperator.Code.SUBTRACTION -> result = v2 - v1
                EXPOperator.Code.MULTIPLICATION -> result = v1 * v2
                else -> {
                }
            }
        } else if (o.arity == EXPOperator.ArityType.UNARY) {
            val v1 = values.pop()
            val c = o.code
            when (c) {
                EXPOperator.Code.ADDITION -> {
                    result = +v1
                }
                EXPOperator.Code.SUBTRACTION -> {
                    result = -v1
                }
                else -> {}
            }
        }
        values.push(result)
    }

    private fun isCalculated(c: HMConst?): Boolean {
        return c?.value != null
    }

    init {
        availableFunctions.add("cos")
        availableFunctions.add("sin")
        availableFunctions.add("pow")
        availableFunctions.add("exp")
        availableFunctions.add("sqrt")
    }
}