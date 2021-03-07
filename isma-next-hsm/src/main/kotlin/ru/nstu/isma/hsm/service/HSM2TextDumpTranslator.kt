package ru.nstu.isma.hsm.service

import java.lang.StringBuilder
import ru.nstu.isma.hsm.HSM
import ru.nstu.isma.hsm.`var`.*
import ru.nstu.isma.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.hsm.exp.HMExpression
import ru.nstu.isma.hsm.hybrid.HMPseudoState
import ru.nstu.isma.hsm.hybrid.HMState
import ru.nstu.isma.hsm.linear.HMLinearEquation
import ru.nstu.isma.hsm.linear.HMLinearVar
import java.util.*
import java.util.function.Consumer

/**
 * Created by Bessonov Alex
 * Date: 05.12.13
 * Time: 13:28
 */
object HSM2TextDumpTranslator {
    fun convert(model: HSM): String {
        val sb = StringBuilder()
        printVariableTable("GLOBAL", model.variableTable, sb)
        for (s in model.automata.states.keys) {
            val ss = model.automata.getState(s)
            printState(ss, sb)
        }
        sb.append("=== Transactions ========================\n")
        for (ts in model.automata.transactions) {
            sb.append("from ").append(ts.source?.code)
            sb.append(" to ").append(ts.target?.code)
            sb.append(" when ")
            printExpression(ts.condition, sb)
            sb.append("\n")
        }
        sb.append("=== Pseudo states ========================\n")
        model.automata.allPseudoStates.stream().forEach { ps: HMPseudoState? ->
            sb.append("if ")
            printExpression(ps?.condition, sb)
            sb.append(" then ")
            printState(ps, sb)
        }
        sb.append("=== Linear system  ========================\n")
        model.linearSystem.vars.values.forEach { v -> printLinearVar(v, sb) }
        if (model.linearSystem.equations != null) {
            model.linearSystem.equations!!.forEach { e: HMLinearEquation? -> printLinearEq(model, e, sb) }
        }
        return sb.toString()
    }

    private fun printVariableTable(name: String?, vt: HMVariableTable?, sb: StringBuilder): StringBuilder {
        sb.append("=== Variable table dump: ").append(name).append("========================\n")
        printUnnamedConsts(vt, sb)
        printConsts(vt, sb)
        printEquations(vt, sb)
        return sb
    }

    private fun printState(st: HMState?, sb: StringBuilder): StringBuilder {
        printVariableTable(st?.code, st?.variables, sb)
        return sb
    }

    private fun printConsts(vt: HMVariableTable?, sb: StringBuilder): StringBuilder {
        sb.append("// --- Constants: --------------------\n")
        for (vk in vt!!.keySet()) {
            val vv = vt[vk]
            if (vv is HMConst && vv !is HMUnnamedConst) {
                printConst(vv, sb)
                sb.append("\n")
            }
        }
        sb.append("\n")
        return sb
    }

    private fun printUnnamedConsts(vt: HMVariableTable?, sb: StringBuilder): StringBuilder {
        sb.append("// --- Unnamed constants: -------------------- \n")
        for (vk in vt!!.keySet()) {
            val vv = vt[vk]
            if (vv is HMUnnamedConst) {
                printUConst(vv, sb)
                sb.append("\n")
            }
        }
        sb.append("\n")
        return sb
    }

    private fun printEquations(vt: HMVariableTable?, sb: StringBuilder): StringBuilder {
        sb.append("// --- Equations: -------------------- \n")
        for (vk in vt!!.keySet()) {
            val vv = vt[vk]
            if (vv is HMAlgebraicEquation) {
                printAlgebraicEquation(vv, sb)
                sb.append("\n")
            } else if (vv is HMPartialDerivativeEquation) {
                printPartialDerEquation(vv, sb)
                sb.append("\n")
            } else if (vv is HMDerivativeEquation) {
                printDerivativeEquation(vv, sb)
                sb.append("\n")
            }
        }
        sb.append("\n")
        return sb
    }

    private fun printVariable(v: HMVariable, sb: StringBuilder): StringBuilder {
        when (v) {
            is HMUnnamedConst -> {
                printUConst(v, sb)
            }
            is HMConst -> {
                printConst(v, sb)
            }
            is HMAlgebraicEquation -> {
                printAlgebraicEquation(v, sb)
            }
            is HMDerivativeEquation -> {
                printDerivativeEquation(v, sb)
            }
            is HMPartialDerivativeEquation -> {
                printPartialDerEquation(v, sb)
            }
        }
        return sb
    }

    private fun printConst(c: HMConst, sb: StringBuilder): StringBuilder {
        return sb.append("[const] ").append(c.code).append(" = ").append(c.value)
    }

    private fun printUConst(c: HMUnnamedConst, sb: StringBuilder): StringBuilder {
        return sb.append("[unnamed_const] ").append(c.value).append(" // \"").append(c.code).append("\"")
    }

    private fun printAlgebraicEquation(eq: HMAlgebraicEquation, sb: StringBuilder): StringBuilder {
        sb.append("[equation] ")
        sb.append(eq.code)
        sb.append(" = ")
        printExpression(eq.rightPart, sb)
        return sb
    }

    private fun printDerivativeEquation(eq: HMDerivativeEquation, sb: StringBuilder): StringBuilder {
        sb.append("[dae] ").append(eq.code).append(" = ")
        printExpression(eq.rightPart, sb)
        sb.append("   // initial = ")
        printExpression(eq.initial?.rightPart, sb)
        return sb
    }

    private fun printPartialDerEquation(eq: HMPartialDerivativeEquation, sb: StringBuilder): StringBuilder {
        var sb = sb
        sb.append("[pde] ")
        for (v in eq.variables) {
            sb.append("[").append(v?.code).append(" ").append(eq.getVariableOrder(v)).append("] ")
        }
        sb.append(eq.code).append(" = ")
        printExpression(eq.rightPart, sb)
        sb.append("   //  initial = ")
        printExpression(eq.initial?.rightPart, sb)
        for (b in eq.boundaries) {
            sb.append("; edge ")
            sb.append(b.equation?.code).append(" = ")
            sb = printExpression(b.value, sb)
            sb.append(" at ").append(b.spatialVar.code).append(" ").append(b.side.toString())
        }
        return sb
    }

    private fun printExpression(exp: HMExpression?, sb: StringBuilder): StringBuilder {
        if (exp == null) {
            sb.append("null")
            return sb
        }
        exp.toString(sb, false)
        return sb
    }

    private fun printLinearVar(`var`: HMLinearVar, sb: StringBuilder): StringBuilder {
        sb.append("var ").append(`var`.code)
            .append("[").append(`var`.columnIndex).append("]\n")
        return sb
    }

    private fun printLinearEq(model: HSM, eq: HMLinearEquation?, sb: StringBuilder): StringBuilder {
        val vars: MutableList<HMLinearVar> = LinkedList()
        model.linearSystem.vars.values.forEach(Consumer { e: HMLinearVar -> vars.add(e) })
        sb.append("lin eq: ")
        for (i in eq!!.leftPart.indices) {
            sb.append(getByIdx(vars, i)?.code).append(" [")
            val converter = Poliz2InfixConverter(eq.leftPart[i])
            printExpression(converter.convert(), sb)
            sb.append("]")
            sb.append("    ")
        }
        sb.append("\n")
        return sb
    }

    private fun getByIdx(vars: List<HMLinearVar>, idx: Int): HMLinearVar? {
        for (v in vars) {
            if (v.columnIndex == idx) return v
        }
        return null
    }
}