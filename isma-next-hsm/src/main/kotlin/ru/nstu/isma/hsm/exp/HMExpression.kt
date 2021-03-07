package ru.nstu.isma.hsm.exp

import ru.nstu.isma.hsm.`var`.HMUnnamedConst
import ru.nstu.isma.hsm.`var`.HMVariable
import ru.nstu.isma.hsm.service.Poliz2InfixConverter
import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 1:00
 */
open class HMExpression : Serializable {
    var type: Type? = Type.INFIX
    val tokens: MutableList<EXPToken?> = LinkedList()

    fun add(t: EXPToken?): List<EXPToken?> {
        tokens.add(t)
        return tokens
    }

    fun addOperand(v: HMVariable?): List<EXPToken?> {
        tokens.add(EXPOperand(v))
        return tokens
    }

    fun addUnnamedConst(v: Double): List<EXPToken?> {
        tokens.add(EXPOperand(HMUnnamedConst(v)))
        return tokens
    }

    fun openParenthesis(): List<EXPToken?> {
        tokens.add(EXPParenthesis(EXPParenthesis.Type.OPEN))
        return tokens
    }

    fun closeParenthesis(): List<EXPToken?> {
        tokens.add(EXPParenthesis(EXPParenthesis.Type.CLOSE))
        return tokens
    }

    fun toString(sb: StringBuilder, toInfix: Boolean): StringBuilder {
        var exp = this
        if (toInfix == true) {
            val converter = Poliz2InfixConverter(exp)
            exp = converter.convert()
        }
        for (t in exp.tokens) {
            when (t) {
                is EXPOperator -> {
                    sb.append(t.toString())
                }
                is EXPParenthesis -> {
                    when (t.type) {
                        EXPParenthesis.Type.CLOSE -> sb.append(")")
                        EXPParenthesis.Type.OPEN -> sb.append("(")
                    }
                }
                is EXPFunctionOperand -> {
                    sb.append(t.name)
                    sb.append("(")
                    for ((cnt, e) in t.args.withIndex()) {
                        if (cnt > 0) {
                            sb.append(",")
                        }
                        val sbArg = StringBuilder()
                        sb.append(e!!.toString(sbArg, true).toString())
                    }
                    sb.append(")")
                }
                is EXPPDEOperand -> {
                    val e = t
                    sb.append("D(")
                    sb.append(e.variable!!.code)
                    sb.append(", ")
                    sb.append(e.firstSpatialVariable!!.code)
                    sb.append(")")
                }
                is EXPOperand -> {
                    val vv = t.variable
                    if (vv is HMUnnamedConst) {
                        sb.append(vv.value)
                    } else sb.append(vv!!.code)
                }
            }
            sb.append(" ")
        }
        return sb
    }

    enum class Type : Serializable {
        INFIX, POLIZ
    }

    companion object {
        fun getConst(value: Double): HMExpression {
            val exp = HMExpression()
            exp.add(EXPOperand(HMUnnamedConst(value)))
            return exp
        }

        val zero: HMExpression
            get() = getConst(0.0)
    }
}