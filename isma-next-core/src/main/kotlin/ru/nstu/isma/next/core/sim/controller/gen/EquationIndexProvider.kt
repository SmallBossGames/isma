package ru.nstu.isma.next.core.sim.controller.gen

import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableMap
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import common.IndexProvider
import org.apache.commons.text.StringSubstitutor

/**
 * Генератор индексов для уравнений в расчетной модели.
 * Формирует независимые наборы индексов для дифференциальных (ДУ) и алгебраических уравнений (АУ).
 *
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class EquationIndexProvider(hsm: HSM) : IndexProvider {
    private val deIndices = HashBiMap.create<String, Int>() // TODO: СЛАУ
    private val aeIndices = HashBiMap.create<String, Int>()

    /** Возвращает строку вида y[index] для ДУ, соответствующую уравнению по указанному код из HSM.  */
    override fun getDifferentialArrayCode(code: String): String {
        val template = "\${deArrayName}[\${deIndex}]"
        val sub = StringSubstitutor(ImmutableMap.of(
            "deArrayName", DE_ARRAY_NAME,
            "deIndex", deIndices[code]
        ))
        return sub.replace(template)
    }

    /** Возвращает строку вида а.getValue(index) для АУ, соответствующей уравнению по указанному код из HSM.  */
    override fun getAlgebraicArrayCode(code: String): String {
        val template = "\${aeArrayName}.getValue(\${aeIndex})"
        val sub = StringSubstitutor(ImmutableMap.of(
            "aeArrayName", AE_RESULT_PROVIDER_NAME,
            "aeIndex", aeIndices[code]
        ))
        return sub.replace(template)
    }

    /** Возвращает строку вида rhs[rhsAeIndex][index] для АУ в ДУ, соответствующей уравнению по указанному коду из HSM.  */
    override fun getAlgebraicArrayCodeForDifferentialEquation(aeCode: String): String {
        val template = "\${deRhsArrayName}[\${rhsAeIndex}][\${aeIndex}]"
        val sub = StringSubstitutor(ImmutableMap.of(
            "deRhsArrayName", DE_RHS_ARRAY_NAME,
            "rhsAeIndex", DaeSystem.RHS_AE_PART_IDX,
            "aeIndex", aeIndices[aeCode]
        ))
        return sub.replace(template)
    }

    /** Возвращает сгенерированный индекс ДУ для указанного кода из HSM.  */
    fun getDifferentialEquationIndex(code: String): Int? {
        return deIndices[code]
    }

    /** Возвращает сгенерированный индекс АУ для указанного кода из HSM.  */
    fun getAlgebraicEquationIndex(code: String): Int? {
        return aeIndices[code]
    }

    /** Возвращает код ДУ из HSM для сгенерированного индекса.  */
    fun getDifferentialEquationCode(index: Int): String? {
        return deIndices.inverse()[index]
    }

    /** Возвращает код АУ из HSM для сгенерированного индекса.  */
    fun getAlgebraicEquationCode(index: Int): String? {
        return aeIndices.inverse()[index]
    }

    fun getDifferentialEquationCount(): Int {
        return deIndices.size
    }

    fun getAlgebraicEquationCount(): Int {
        return aeIndices.size
    }

    companion object {
        private const val DE_ARRAY_NAME = "y"
        private const val DE_RHS_ARRAY_NAME = "rhs"
        private const val AE_RESULT_PROVIDER_NAME = "a"
    }

    init {
        hsm.variableTable.odes
            .forEach {
                deIndices.putIfAbsent(it.code, deIndices.size)
            }

        hsm.variableTable.algs
            .forEach {
                aeIndices.putIfAbsent(it.code, aeIndices.size)
            }
    }
}