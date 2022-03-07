package ru.nstu.isma.intg.api.calcmodel

class AlgebraicEquationCalculator(
    private val algebraicEquations: Array<AlgebraicEquation>
) {
    fun apply(y: DoubleArray): DoubleArray {
        val rhsForDe = DoubleArray(algebraicEquations.size)
        for (i in algebraicEquations.indices) {
            rhsForDe[i] = algebraicEquations[i].apply(y, Provider)
        }
        return rhsForDe
    }

    //TODO: Workaround, because we cannot remove this method
    object Provider: IAlgebraicEquationResultProvider{
        override fun getValue(index: Int): Double {
            return 0.0;
        }
    }
}

