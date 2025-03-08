package ru.nstu.isma.intg.api.calcmodel

import ru.nstu.isma.compiler.hsm.jvm.calcmodel.AlgebraicEquation

class AlgebraicEquationCalculator(
    private val algebraicEquations: Array<AlgebraicEquation>
) {
    fun apply(y: DoubleArray): DoubleArray {
        val rhsForDe = DoubleArray(algebraicEquations.size)
        for (i in algebraicEquations.indices) {
            rhsForDe[i] = algebraicEquations[i].apply(y)
        }
        return rhsForDe
    }
}

