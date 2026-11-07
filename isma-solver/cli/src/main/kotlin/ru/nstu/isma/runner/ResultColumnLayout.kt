package ru.nstu.isma.runner

import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.compiler.hsm.jvm.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.models.IntgResultPoint

/**
 * The ordered result-column layout for a model, derived from its
 * [EquationIndexProvider].
 *
 * The layout is a fixed protocol that the orchestrator and the UI both rely on:
 *   1. `TIME` — the integration time.
 *   2. `DE_<i>[-<code>]` — one per differential equation (state values).
 *   3. `AE_<i>[-<code>]` — one per algebraic equation (algebraic RHS).
 *   4. `f<i>` — one per differential equation (differential RHS).
 *
 * [names] and [flatten] always agree on this order: [names] names the columns,
 * [flatten] fills them from an [IntgResultPoint].
 */
class ResultColumnLayout(indexProvider: EquationIndexProvider) {
    val names: List<String> = buildNames(indexProvider)
    val columnCount: Int = names.size

    /**
     * Flattens [point] into the result row, in the order of [names]: the time,
     * the differential-equation state values, the algebraic RHS values, and the
     * differential RHS values.
     */
    fun flatten(point: IntgResultPoint): DoubleArray {
        val deValues = point.yForDe
        val aeValues = point.rhs[DaeSystem.RHS_AE_PART_IDX]
        val deRhs = point.rhs[DaeSystem.RHS_DE_PART_IDX]

        val row = DoubleArray(1 + deValues.size + aeValues.size + deRhs.size)
        var index = 0
        row[index++] = point.x
        System.arraycopy(deValues, 0, row, index, deValues.size); index += deValues.size
        System.arraycopy(aeValues, 0, row, index, aeValues.size); index += aeValues.size
        System.arraycopy(deRhs, 0, row, index, deRhs.size)
        return row
    }

    private fun buildNames(indexProvider: EquationIndexProvider): List<String> {
        val deCount = indexProvider.getDifferentialEquationCount()
        val aeCount = indexProvider.getAlgebraicEquationCount()

        val names = ArrayList<String>(1 + deCount + aeCount + deCount)
        names.add("TIME")
        for (i in 0 until deCount) {
            val code = indexProvider.getDifferentialEquationCode(i)
            names.add(code?.let { "DE_$i-$it" } ?: "DE_$i")
        }
        for (i in 0 until aeCount) {
            val code = indexProvider.getAlgebraicEquationCode(i)
            names.add(code?.let { "AE_$i-$it" } ?: "AE_$i")
        }
        for (i in 0 until deCount) {
            names.add("f$i")
        }
        return names
    }
}
