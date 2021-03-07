package ru.nstu.isma.hsm.service

import java.lang.RuntimeException
import ru.nstu.isma.hsm.`var`.HMConst
import ru.nstu.isma.hsm.`var`.HMUnnamedConst
import ru.nstu.isma.hsm.error.IsmaError
import ru.nstu.isma.hsm.error.IsmaErrorList
import ru.nstu.isma.hsm.exp.EXPFunctionOperand
import ru.nstu.isma.hsm.exp.EXPOperand
import ru.nstu.isma.hsm.exp.EXPOperator
import ru.nstu.isma.hsm.exp.HMExpression
import java.lang.Exception
import java.util.*
import kotlin.math.*

/**
 * by
 * Bessonov Alex.
 * Date: 14.11.13 Time: 0:21
 */
class ConstValueCalculator(private val errors: IsmaErrorList?) {
    fun calculateConst(expression: HMExpression?): Double? {
        val c = HMConst("cc")
        c.rightPart = expression
        return calculateConst(c)
    }

    fun calculateConst(c: HMConst?): Double? {
        if (isCalculated(c)) {
            return c?.value
        }
        var value: Double?
        try {
            validateExpression(c?.rightPart)
            avoidLoopCheck(c!!, null)
            value = runCaltulator(c.rightPart)
            c.setValue(value!!)
        } catch (e: Exception) {
            e.printStackTrace()
            val err = IsmaError("ошибка при рассчете константы: " + c!!.code + "  " + e.message)
            err.type = IsmaError.Type.SEM
            errors?.add(err)
            value = 0.0
        }
        return value
    }

    fun validateExpression(expression: HMExpression?) {
        // проверяем что все токены - константы
        for (t in expression!!.tokens) {
            if (t is EXPFunctionOperand) {
                t.args.stream()
                    .forEach { exp: HMExpression? -> validateExpression(exp) }
            } else if (t is EXPOperand) {
                val `var` = t.variable
                if (`var` !is HMConst) {
                    val c = if (`var` != null) `var`.code else "NULL"
                    throw RuntimeException("в выражении содержится не константное значение: $c")
                }
            }
        }
    }

    private fun avoidLoopCheck(c: HMConst, prevCheck: HashSet<String?>?) {
        if (c is HMUnnamedConst) {
            return
        }
        val current = HashSet<String?>()
        current.add(c.code)
        if (prevCheck != null) {
            for (s in prevCheck) {
                current.add(s)
            }
        }
        val tokens = c.rightPart!!.tokens
        for (token in tokens) {
            if (token is EXPOperand && token !is EXPFunctionOperand) {
                var `var` = token.variable as HMConst
                if (current.contains(`var`.code)) {
                    // получили петлю -  семантика обработка ошибки
                    // TODO тщательно проверить
                    if (`var` is HMUnnamedConst) `var` = c
                    throw RuntimeException("в выражении содержится петля из-за переменной " + `var`.code)
                } else {
//                    current.add(var.getCode());
                    avoidLoopCheck(`var`, current)
                }
            }
        }
    }

    private fun runCaltulator(expression: HMExpression?): Double? {
        val stack = Stack<Double>()
        for (token in expression!!.tokens) {
            if (token is EXPFunctionOperand) {
                val f = calculateFunction(token)
                stack.push(f)
            } else if (token is EXPOperand) {
                val `var` = token.variable
                stack.push(calculateConst(`var` as HMConst))
            } else if (token is EXPOperator) {
                doOperation(stack, token)
            }
        }
        return stack.pop()
    }

    private fun doOperation(values: Stack<Double>, o: EXPOperator) {
        var result = 0.0
        if (o.type != EXPOperator.Type.ALGEBRAIC) {
            // семантическая ошибка
            throw RuntimeException("в выражении содержится не алгебраический оператор $o")
        }
        if (o.arity == EXPOperator.ArityType.BINARY) {
            val v1 = values.pop()
            val v2 = values.pop()
            val c = o.code
            result = when (c) {
                EXPOperator.Code.ADDITION -> v1 + v2
                EXPOperator.Code.DIVISION -> v2 / v1
                EXPOperator.Code.SUBTRACTION -> v2 - v1
                EXPOperator.Code.MULTIPLICATION -> v1 * v2
                else -> throw RuntimeException("в выражении содержится неизвестный  оператор $o")
            }
        } else if (o.arity == EXPOperator.ArityType.UNARY) {
            val v1 = values.pop()
            when (o.code) {
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

    private fun calculateFunction(function: EXPFunctionOperand): Double {
        val args: MutableList<Double?> = LinkedList()
        for (exp in function.args) {
            args.add(runCaltulator(exp))
        }
        val result = when (function.name) {
            "cos" -> {
                cos(args[0]!!) //Math.cos(Math.toRadians(args.get(0)));
            }
            "sin" -> {
                sin(args[0]!!) //Math.sin(Math.toRadians(args.get(0)));
            }
            "exp" -> {
                exp(args[0]!!)
            }
            "pow" -> {
                (args[0]!!).pow(args[1]!!)
            }
            "abs" -> {
                abs(args[0]!!)
            }
            "sqrt" -> {
                sqrt(args[0]!!)
            }
            else -> {
                throw RuntimeException("I understand function " + function.name)
            }
        }
        return result
    }

    companion object {
        private fun isCalculated(c: HMConst?): Boolean {
            if (c == null) println()
            return c!!.value != null
        }
    }
}