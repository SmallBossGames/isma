package ru.nstu.isma.next.intg.core.solvers

import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquationCalculator
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquationsCalculator
import ru.nstu.isma.intg.api.methods.IntgController
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.methods.StageCalculator
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import java.util.*

/**
 * @author Mariya Nasyrova
 * @since 01.09.14
 */
class DefaultDaeSystemStepSolver(
    override val intgMethod: IntgMethod,
    daeSystem: DaeSystem
) : DaeSystemStepSolver {
    var daeSystem: DaeSystem = daeSystem
        private set
    var stepCalculationCount: Long = 0
        private set
    var rhsCalculationCount: Long = 0
        private set
    private var deCalculator: DifferentialEquationsCalculator? = null
    private var aeCalculator: AlgebraicEquationCalculator? = null

    init {
        commitDaeSystem(daeSystem)
    }

    override fun calculateRhs(yForDe: DoubleArray): Array<DoubleArray> {
        rhsCalculationCount++
        val rhs = daeSystem.createEmptyRhs()
        rhs[DaeSystem.RHS_AE_PART_IDX] = calculateRhsForAlgebraicEquations(yForDe)
        rhs[DaeSystem.RHS_DE_PART_IDX] = calculateRhsForDifferentialEquations(yForDe, rhs)
        return rhs
    }

    override fun apply(changeSet: DaeSystemChangeSet?) {
        if (changeSet != null && !changeSet.isEmpty) {
            commitDaeSystem(changeSet.apply(daeSystem))
        }
    }

    override fun step(fromPoint: IntgPoint): IntgPoint {
        val initialStep = fromPoint.step

        var stages = stages(fromPoint)
        var accuracyNextStep: Double? = null

        val accuracyController = intgMethod.accuracyController

        if (accuracyController != null && isControllerEnabled(accuracyController)) {
            val accuracyResults = accuracyController.tune(fromPoint, stages, this)
            stages = accuracyResults.tunedStages
            accuracyNextStep = accuracyResults.tunedStep
            fromPoint.step = accuracyResults.tunedStep
        }

        stepCalculationCount++

        val nextY = nextY(fromPoint, stages)
        val nextRhs = calculateRhs(nextY)
        val toPoint = IntgPoint(fromPoint.step, nextY, nextRhs, stages, fromPoint.step)
        var stabilityNextStep: Double? = null

        val stabilityController = intgMethod.stabilityController

        if (stabilityController != null && isControllerEnabled(stabilityController)) {
            stabilityNextStep = stabilityController.predictNextStepSize(toPoint)
        }

        // Выбираем следующий шаг по формуле h_new = max(h, min(h_acc, h_st)), где
        // min(h_acc, h_st) - минимальный из шагов, прогнозируемых по точности и устойчивости;
        // h - текущий шаг.
        var minPredictedNextStep = accuracyNextStep
        if (minPredictedNextStep == null || stabilityNextStep != null && stabilityNextStep < minPredictedNextStep) {
            minPredictedNextStep = stabilityNextStep
        }

        var nextStep = initialStep
        if (minPredictedNextStep != null && minPredictedNextStep > initialStep) {
            nextStep = minPredictedNextStep
        }

        toPoint.nextStep = nextStep

        return toPoint
    }

    override fun stages(fromPoint: IntgPoint): Array<DoubleArray> {
        val stageCalculators = intgMethod.stageCalculators

        if (stageCalculators.isNullOrEmpty()) {
            return emptyArray()
        }
        
        val stageCount = stageCalculators.size
        val stages = Array(daeSystem.differentialVariableCount) { DoubleArray(stageCount) }
        for (stageIdx in 0 until stageCount) {
            val stageCalc = stageCalculators[stageIdx]
            val yk = yk(stageCalc, fromPoint, stages)

            // Оптимизация для первой стадии.
            val fk: DoubleArray = if (stageIdx == 0 && Arrays.equals(yk, fromPoint.y)) {
                fromPoint.rhs[DaeSystem.RHS_DE_PART_IDX]
            } else {
                calculateRhs(yk)[DaeSystem.RHS_DE_PART_IDX]
            }

            val k = k(stageCalc, fromPoint, stages, yk, fk)
            for (i in 0 until daeSystem.differentialVariableCount) {
                stages[i][stageIdx] = k[i]
            }
        }
        return stages
    }

    private fun calculateRhsForAlgebraicEquations(yForDe: DoubleArray): DoubleArray {
        return aeCalculator!!.apply(yForDe)
    }

    private fun calculateRhsForDifferentialEquations(
        yForDe: DoubleArray,
        rhs: Array<DoubleArray>
    ): DoubleArray {
        return deCalculator!!.apply(yForDe, rhs)
    }

    private fun nextY(fromPoint: IntgPoint, stages: Array<DoubleArray>): DoubleArray {
        val nextY = DoubleArray(fromPoint.y.size)
        var odeStages = DoubleArray(0)
        for (odeIdx in 0 until daeSystem.differentialEquationCount) {

            if (stages.isNotEmpty()) {
                odeStages = stages[odeIdx]
            }

            nextY[odeIdx] = intgMethod.nextY(
                fromPoint.step,
                odeStages,
                fromPoint.y[odeIdx],
                fromPoint.rhs[DaeSystem.RHS_DE_PART_IDX][odeIdx]
            )
        }
        return nextY
    }

    private fun yk(stageCalc: StageCalculator, fromPoint: IntgPoint, stages: Array<DoubleArray>): DoubleArray {
        val yk = DoubleArray(fromPoint.y.size)
        for (odeIdx in 0 until daeSystem.differentialVariableCount) {
            yk[odeIdx] = stageCalc.yk(
                fromPoint.step,
                fromPoint.y[odeIdx],
                fromPoint.rhs[DaeSystem.RHS_DE_PART_IDX][odeIdx],
                stages[odeIdx]
            )
        }
        return yk
    }

    private fun k(
        stageCalc: StageCalculator,
        fromPoint: IntgPoint,
        stages: Array<DoubleArray>,
        yk: DoubleArray,
        fk: DoubleArray
    ): DoubleArray {
        val k = DoubleArray(daeSystem.differentialEquationCount)
        for (odeIdx in 0 until daeSystem.differentialEquationCount) {
            k[odeIdx] = stageCalc.k(
                fromPoint.step,
                fromPoint.y[odeIdx],
                fromPoint.rhs[DaeSystem.RHS_DE_PART_IDX][odeIdx],
                stages[odeIdx],
                yk[odeIdx],
                fk[odeIdx]
            )
        }
        return k
    }

    private fun commitDaeSystem(daeSystem: DaeSystem) {
        this.daeSystem = daeSystem
        aeCalculator = AlgebraicEquationCalculator(daeSystem.algebraicEquations)
        deCalculator = DifferentialEquationsCalculator(daeSystem.differentialEquations)
    }

    private fun isControllerEnabled(intgController: IntgController?): Boolean {
        return intgController != null && intgController.isEnabled
    }

    override fun dispose() {}
}