package ru.nstu.isma.hsm.common

import ru.nstu.isma.print.MatrixPrint.print
import java.lang.StringBuilder
import java.lang.RuntimeException
import java.util.stream.Collectors
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject
import java.text.SimpleDateFormat
import java.util.function.BiConsumer
import kotlin.jvm.JvmOverloads
import kotlin.Throws
import org.apache.commons.lang3.SerializationUtils
import ru.nstu.isma.hsm.`var`.HMAlgebraicEquation
import ru.nstu.isma.hsm.`var`.HMConst
import ru.nstu.isma.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.hsm.exp.*
import ru.nstu.isma.hsm.linear.HMLinearVar
import ru.nstu.isma.hsm.service.Poliz2InfixConverter
import ru.nstu.isma.print.MatrixPrint
import java.util.*

/**
 * @author Maria Nasyrova
 * @since 05.10.2015
 */
class HMExpressionBuilder(private val context: IndexProvider) {
    @JvmOverloads
    fun buildExpression(exp: HMExpression?, needDeepAlgCalc: Boolean = false, forOde: Boolean = false): String {
        var exp = exp
        val str = StringBuilder()
        if (exp?.type == HMExpression.Type.POLIZ) {
            val converter = Poliz2InfixConverter(exp)
            exp = converter.convert()
        }
        exp!!.tokens.stream().forEach { t: EXPToken? ->
            when (t) {
                is EXPOperator -> {
                    doOperator(str, t)
                }
                is EXPParenthesis -> {
                    str.append(t.toString())
                }
                is EXPFunctionOperand -> {
                    doFunction(str, t, forOde)
                }
                is EXPOperand -> {
                    val `var` = t.variable
                    when {
                        `var` is HMConst -> {
                            str.append(`var`.value) // todo формат double
                        }
                        `var` is HMAlgebraicEquation && needDeepAlgCalc -> {
                            str.append(context.getAlgebraicArrayCode(`var`.code))
                        }
                        `var` is HMDerivativeEquation -> {
                            str.append(context.getDifferentialArrayCode(`var`.code))
                        }
                        `var` is HMLinearVar || `var` is HMAlgebraicEquation && !needDeepAlgCalc -> {
                            if (forOde) {
                                str.append(context.getAlgebraicArrayCodeForDifferentialEquation(`var`.code))
                            } else {
                                str.append(context.getAlgebraicArrayCode(`var`.code))
                            }
                        }
                    } /*else if (var instanceof HMAlgebraicEquation) {
                            str.append("(").append(buildExpression(((HMAlgebraicEquation) var).getRightPart())).append(")");
                        }*/
                }
            }
            str.append(" ")
        }
        return str.toString()
    }

    private fun doOperator(sb: StringBuilder, o: EXPOperator) {
        when (o.code) {
            EXPOperator.Code.AND -> sb.append("&&")
            EXPOperator.Code.OR -> sb.append("||")
            else -> sb.append(o.toString())
        }
    }

    private fun doFunction(sb: StringBuilder, funct: EXPFunctionOperand, forOde: Boolean) {
        val name = funct.name
        var javaName: String? = null
        val args: MutableList<String> = LinkedList()
        when (name) {
            "abs" -> javaName = doOneArgFunct(name, "Math.abs", funct, args, forOde)
            "sin" -> javaName = doOneArgFunct(name, "Math.sin", funct, args, forOde)
            "cos" -> javaName = doOneArgFunct(name, "Math.cos", funct, args, forOde)
            "tg" -> javaName = doOneArgFunct(name, "Math.tan", funct, args, forOde)
            "exp" -> javaName = doOneArgFunct(name, "Math.exp", funct, args, forOde)
            "sqrt" -> javaName = doOneArgFunct(name, "Math.sqrt", funct, args, forOde)
            "pow" -> javaName = doTwoArgFunct(name, "Math.pow", funct, args, forOde)
            "max" -> javaName = doTwoArgFunct(name, "Math.max", funct, args, forOde)
            "min" -> javaName = doTwoArgFunct(name, "Math.min", funct, args, forOde)
            "sign" -> javaName = doOneArgFunct(name, "Math.signum", funct, args, forOde)
        }
        sb.append(" ").append(javaName).append("(")
        args.stream().forEach { e: String ->
            if (e != args[0]) sb.append(", ")
            sb.append(e)
        }
        sb.append(") ")
    }

    private fun doOneArgFunct(
        name: String,
        javaName: String,
        funct: EXPFunctionOperand,
        args: MutableList<String>,
        forOde: Boolean
    ): String {
        val argSize = funct.args.size
        assertTrue(argSize == 1, name)
        args.add(buildExpression(funct.args[0], false, forOde))
        return javaName
    }

    private fun doTwoArgFunct(
        name: String,
        javaName: String,
        funct: EXPFunctionOperand,
        args: MutableList<String>,
        forOde: Boolean
    ): String {
        val argSize = funct.args.size
        assertTrue(argSize == 2, name)
        args.add(buildExpression(funct.args[0], false, forOde))
        args.add(buildExpression(funct.args[1], false, forOde))
        return javaName
    }

    private fun assertTrue(cond: Boolean, name: String) {
        if (!cond) {
            throw RuntimeException("Incorrect arg count for function: $name")
        }
    }
}