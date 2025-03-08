package ru.nstu.isma.intg.api.calcmodel

import ru.nstu.isma.compiler.hsm.jvm.calcmodel.DifferentialEquation

class DifferentialEquationsCalculator(
    private val differentialEquations: Array<DifferentialEquation>,
) {
    fun apply(y: DoubleArray, rhs: Array<DoubleArray>): DoubleArray {
        val rhsForDe = DoubleArray(differentialEquations.size)
        for (i in differentialEquations.indices) {
            rhsForDe[i] = differentialEquations[i].apply(y, rhs)
        }
        return rhsForDe
    }
}