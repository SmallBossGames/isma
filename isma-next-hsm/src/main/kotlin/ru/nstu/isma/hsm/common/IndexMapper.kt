package ru.nstu.isma.hsm.common

import ru.nstu.isma.hsm.HSM
import ru.nstu.isma.hsm.`var`.HMAlgebraicEquation
import ru.nstu.isma.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.hsm.linear.HMLinearVar
import java.io.Serializable
import java.util.*
import java.util.function.Consumer

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class IndexMapper(val hsm: HSM) : IndexProvider, Serializable {
    // HSM equation name -> ODE index
    private val innerIndexMap: MutableMap<String?, Int?> = HashMap()
    private val codeMap: MutableMap<Int, String?> = HashMap()

    val indexMap: Map<String?, Int?> get()  = innerIndexMap

    private fun prepeare() {
        hsm.variableTable.odes.stream()
            .forEach { de: HMDerivativeEquation? ->
                if (de is HMPartialDerivativeEquation) {
                    throw RuntimeException("not supported") // todo изменить на списщк IsmaErrorList
                }
                addCode(de!!.code)
            }
        hsm.variableTable.algs.stream()
            .forEach { a: HMAlgebraicEquation? -> addCode(a!!.code) }
        hsm.linearSystem.vars.values
            .forEach(Consumer { v: HMLinearVar -> addCode(v.code) })
    }

    private fun addCode(code: String?) {
        if (!innerIndexMap.containsKey(code)) {
            val idx = innerIndexMap.size
            innerIndexMap[code] = idx
            codeMap[idx] = code
        }
    }

    override fun getDifferentialArrayCode(code: String?): String {
        return odeArrayName + "[" + innerIndexMap[code] + "]"
    }

    override fun getAlgebraicArrayCode(code: String?): String {
        return "a.Y(" + innerIndexMap[code] + ")"
    }

    override fun getAlgebraicArrayCodeForDifferentialEquation(aeCode: String?): String {
        return getAlgebraicArrayCode(aeCode)
    }

    fun index(code: String?): Int? {
        return innerIndexMap[code]
    }

    fun code(idx: Int): String? {
        return codeMap[idx]
    }

    companion object {
        const val odeArrayName = "y"
    }

    init {
        prepeare()
    }
}