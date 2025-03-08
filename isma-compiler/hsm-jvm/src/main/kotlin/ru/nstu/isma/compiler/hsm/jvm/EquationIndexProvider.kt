package ru.nstu.isma.compiler.hsm.jvm

import com.google.common.collect.HashBiMap
import ru.nstu.isma.compiler.hsm.core.common.IndexProvider
import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.intg.api.calcmodel.DaeSystem

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
        return """
            $DE_ARRAY_NAME[${deIndices[code]}]
        """.trimIndent()
    }

    /** Возвращает строку вида а.getValue(index) для АУ, соответствующей уравнению по указанному код из HSM.  */
    override fun getAlgebraicArrayCode(code: String): String {
        return """
            $AE_RESULT_PROVIDER_NAME.getValue(${aeIndices[code]})
        """.trimIndent()
    }

    /** Возвращает строку вида rhs[rhsAeIndex][index] для АУ в ДУ, соответствующей уравнению по указанному коду из HSM.  */
    override fun getAlgebraicArrayCodeForDifferentialEquation(aeCode: String): String {
        return """
            $DE_RHS_ARRAY_NAME[${DaeSystem.RHS_AE_PART_IDX}][${aeIndices[aeCode]}]
        """.trimIndent()
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