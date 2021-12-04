package ru.nstu.isma.next.core.fdm

import ru.nstu.isma.core.hsm.`var`.HMConst
import ru.nstu.isma.core.hsm.`var`.HMEquation
import ru.nstu.isma.core.hsm.`var`.HMVariable
import ru.nstu.isma.core.hsm.`var`.HMVariableTable
import ru.nstu.isma.core.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.core.hsm.`var`.pde.HMSampledSpatialVariable
import ru.nstu.isma.core.hsm.`var`.pde.HMSpatialVariable
import ru.nstu.isma.core.hsm.exp.EXPOperand
import ru.nstu.isma.core.hsm.exp.HMExpression
import java.lang.StringBuilder
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 12.12.13
 * Time: 0:49
 */
class FDMContext {
    private val variables: LinkedList<HMSampledSpatialVariable> = LinkedList<HMSampledSpatialVariable>()
    private val equations: HashSet<HMEquation> = HashSet<HMEquation>()

    // маппинг (отображение) имен - проекция имени уравнение и набора индексов для аппроксимируемых переменных на новое имя
    // например уравнение F(x,y,t) и xi=5; hi = 35;  yi=23; ->  F___apx_5_0_23;
    protected fun variableNameMapping(equation: HMVariable, indexes: Map<String?, FDMIndexedApxVar>): String {
        if (!contains(equation.code)) {
            throw RuntimeException("FDMContext doesn't contain " + equation.code)
        }
        val newName: StringBuilder = StringBuilder(equation.code)
        newName.append(APX_PREFIX)
        for (v in variables) {
            newName.append("_")
            if (indexes.containsKey(v.code) && isEqContains(equation, v)) {
                newName.append(indexes[v.code]?.index)
            } else if (!indexes.containsKey(v.code)) {
                newName.append("E")
            } else {
                newName.append("0")
            }
        }
        return newName.toString()
    }

    protected fun variableNameMapping(equation: HMVariable, indexes: List<FDMIndexedApxVar>): String {
        val mVars: MutableMap<String?, FDMIndexedApxVar> = HashMap()
        for (vv in indexes) {
            mVars[vv.code] = vv
        }
        return variableNameMapping(equation, mVars)
    }

    // Добавление новой аппроксимируеммой переменной
    // примеч.: от порядка вставки зависит порядок индексации при маппинге имен
    fun addVariable(apx: HMSampledSpatialVariable): Boolean {
        variables.add(apx)
        return true
    }

    // Добавление нового уравнения контекста
    fun addEquation(eq: HMEquation): Boolean {
        if (containsEquation(eq.code)) {
            return false
        }
        equations.add(eq)
        return true
    }

    fun getVariables(): LinkedList<HMSampledSpatialVariable> {
        return variables
    }

    fun getEquations(): HashSet<HMEquation> {
        return equations
    }

    fun containsEquation(eqCode: String): Boolean {
        for (e in equations) {
            if (e.code == eqCode) {
                return true
            }
        }
        return false
    }

    operator fun contains(code: String): Boolean {
        return containsApxVar(code) || containsEquation(code)
    }

    fun containsApxVar(vCode: String): Boolean {
        for (v in variables) {
            if (v.code == vCode) {
                return true
            }
        }
        return false
    }

    private fun isEqContains(eq: HMVariable, v: HMSpatialVariable): Boolean {
        return if (eq is HMPartialDerivativeEquation) {
            eq.isContains(v)
        } else false
    }

    companion object {
        // Парсинг таблицы переменных и наполнение контекста
        fun prepare(variables: HMVariableTable): FDMContext {
            val context = FDMContext()
            val keys: Set<String> = variables.keySet()
            for (s in keys) {
                val vv: HMVariable = variables.get(s)
                if (vv is HMPartialDerivativeEquation) {
                    context.addEquation(vv)
                } else if (vv is HMSampledSpatialVariable) {
                    context.addVariable(vv)
                }
            }
            var contextReady = false
            var eqNeedApx: Boolean
            while (!contextReady) {
                // пробегаем все уравнения в которых есть другие аппроксимируемые уравнения или переменные
                contextReady = true
                for (s in keys) {
                    val `var`: HMVariable = variables.get(s)
                    if (`var` is HMEquation
                            && `var` !is HMConst
                            && !context.containsEquation(`var`.getCode())) {
                        // проверяем правую часть на аппроксимируемые элементы
                        eqNeedApx = false
                        val right: HMExpression = `var`.rightPart
                        for (t in right.tokens) {
                            // проверяем только уравнения и только если ранее не было найдено (eqNeedApx)
                            if (t is EXPOperand && !eqNeedApx) {
                                val code: String = t.variable.code
                                if (context.containsEquation(code) || context.containsApxVar(code)) {
                                    contextReady = false
                                    context.addEquation(`var`)
                                    eqNeedApx = true
                                }
                            }
                        }
                    }
                }
            }
            return context
        }
    }
}