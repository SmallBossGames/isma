package ru.nstu.isma.next.core.sim.controller.model

import ru.nstu.isma.core.hsm.linear.HMLinearSystem
import ru.nstu.isma.core.hsm.linear.HMLinearVar
import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquation
import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquationCalculator
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
// TODO: Реализовать поддержку и перенести в calclmodel
class DaeSystemWithSlae(differentialEquations: Array<DifferentialEquation?>?, algebraicEquations: Array<AlgebraicEquation?>?) : DaeSystem(differentialEquations, algebraicEquations) {
    private var linearSystem: HMLinearSystem? = null
    fun getLinearSystemVarCount(): Int {
        return linearSystem?.vars?.size ?: 0
    }

    override fun getRhsPartCount(): Int {
        return RHS_PART_COUNT
    }

    // TODO: при реализации поддержки  СЛАУ перенести в stepSolver
    fun calculateRhs(yForDe: DoubleArray?): Array<DoubleArray> {
        val rhs: Array<DoubleArray> = createEmptyRhs()

        // Вычисляем алгебраические функции
        val calc = AlgebraicEquationCalculator(yForDe, algebraicEquations)
        val rhsForAe: DoubleArray = calc.values
        rhs[DaeSystem.RHS_AE_PART_IDX] = rhsForAe

        // Вычисялем СЛАУ
        var rhsForSlae = DoubleArray(0)
        if (linearSystem != null && !linearSystem!!.isEmpty) {
            val slae: DoubleArray = linearSystem!!.calculate(yForDe)
            rhsForSlae = DoubleArray(slae.size)
            for (lss in slae.indices) {
                val `var`: HMLinearVar = linearSystem!!.getLinearVariable(lss)
                val idx: Int = linearSystem!!.getVarCalulationIndex(`var`)
                rhsForSlae[idx] = slae[lss] // TODO: разобраться с индексами.
            }
        }
        rhs[RHS_SLAE_PART_IDX] = rhsForSlae

        // Вычисляем ОДУ
        val rhsForOde = DoubleArray(differentialEquationCount)
        for (i in 0 until differentialEquationCount) {
            rhsForOde[i] = differentialEquations.get(i).apply(yForDe, rhs)
        }
        rhs[DaeSystem.RHS_DE_PART_IDX] = rhsForOde
        return rhs
    }

    fun getLinearSystem(): HMLinearSystem? {
        return linearSystem
    }

    fun setLinearSystem(linearSystem: HMLinearSystem?) {
        this.linearSystem = linearSystem
    }

    companion object {
        private const val RHS_PART_COUNT = 3
        const val RHS_SLAE_PART_IDX = 2
    }
}