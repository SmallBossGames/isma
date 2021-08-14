package ru.isma.next.math.engine.implicitDifferentialEquations

import ru.isma.next.math.common.JacobiMatrixSolver
import ru.isma.next.math.common.Matrix2D
import ru.isma.next.math.common.StationaryODE
import ru.isma.next.math.engine.shared.zeroSafetyNorm
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitEvaluationsException
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitStepsException
import kotlin.math.*

const val a = 0.29289321881
const val p1 = 0.29289321881
const val p2 = 0.5/ a
const val beta = 0.29289321881
const val alpha = -2* a

class MK22Integrator (
    val accuracy: Double,
    val maxFreezeSteps: Int,
    val stepGrowthCoefficient: Double
) : ImplicitIntegrator() {

    @Throws(ExceedingLimitStepsException::class, ExceedingLimitEvaluationsException::class)
    fun integrate(
        startTime: Double,
        endTime: Double,
        defaultStepSize: Double,
        y0: DoubleArray,
        rVector: DoubleArray,
        outY: DoubleArray,
        equations: StationaryODE
    ) : IImplicitMethodStatistic {

        if (y0.size != outY.size)
            throw IllegalArgumentException()

        y0.copyInto(outY)

        val vectorBuffer1 = DoubleArray(y0.size)
        val vectorBuffer2 = DoubleArray(y0.size)
        val k1 = DoubleArray(y0.size)
        val k2 = DoubleArray(y0.size)

        val matrixBuffer = Matrix2D(y0.size)
        val dMatrix = Matrix2D(y0.size)
        val jacobiMatrix = Matrix2D(y0.size)

        var step = defaultStepSize
        var time = startTime
        var isNeedFindJacobi = true

        val statistic = ImplicitMethodStatistic(
            stepsCount = 0,
            evaluationsCount = 0,
            jacobiEvaluationsCount = 0,
            returnsCount = 0
        )

        val state = ImplicitMethodStepState(
            jacobiMatrix = jacobiMatrix,
            isLowStepSizeReached = isLowStepSizeReached(step),
            isHighStepSizeReached = isHighStepSizeReached(step),
            freezeJacobiStepsCount = 0
        )

        val jacobiSolver = JacobiMatrixSolver(y0.size)

        executeStepHandlers(time, outY, state, statistic)

        while (time < endTime) {
            step = normalizeStep(step, time, endTime)

            checkStepCount(statistic.stepsCount)

            if(state.freezeJacobiStepsCount == 0){
                if(isNeedFindJacobi){
                    checkEvaluationCount(statistic.evaluationsCount)
                    jacobiSolver.solve(outY, jacobiMatrix, equations)
                    statistic.jacobiEvaluationsCount++
                } else{
                    isNeedFindJacobi = true
                }

                for (i in dMatrix.indices)
                    for(j in dMatrix.indices)
                        dMatrix[i, j] = (if (i == j) 1.0 else 0.0) - a *step*(jacobiMatrix[i,j])

                dMatrix.makeLU()
            }

            for (i in vectorBuffer1.indices){
                vectorBuffer1[i] = outY[i]
            }
            checkEvaluationCount(statistic.evaluationsCount)
            equations(outY, vectorBuffer2)
            statistic.evaluationsCount++
            for (i in vectorBuffer2.indices){
                vectorBuffer2[i] = step*vectorBuffer2[i]
            }
            dMatrix.solveLU(vectorBuffer2, k1)
            for (i in vectorBuffer1.indices){
                vectorBuffer1[i] = outY[i] + beta *k1[i]
            }
            checkEvaluationCount(statistic.evaluationsCount)
            equations(vectorBuffer1, vectorBuffer2)
            statistic.evaluationsCount++
            for (i in vectorBuffer2.indices){
                vectorBuffer2[i] = step*vectorBuffer2[i] + alpha *k1[i]
            }
            dMatrix.solveLU(vectorBuffer2, k2)

            for (i in vectorBuffer1.indices){
                vectorBuffer1[i] = k2[i] + (2* a -1.0)*k1[i]
            }

            val e1 = zeroSafetyNorm(vectorBuffer1, outY, rVector)
            val q1 = sqrt(accuracy / e1)

            val e2: Double
            val q2: Double
            if(q1 < 1.0){
                dMatrix.inverseLU(matrixBuffer)
                matrixBuffer.multiply(vectorBuffer1, vectorBuffer2)
                e2 = zeroSafetyNorm(vectorBuffer2, outY, rVector)
                q2 = sqrt(accuracy / e2)
            } else {
                e2 = e1
                q2 = q1
            }

            if (q2 < 1.0){
                if(!state.isLowStepSizeReached && !state.isHighStepSizeReached){
                    if(state.freezeJacobiStepsCount == 0) {
                        isNeedFindJacobi = false
                    } else {
                        state.freezeJacobiStepsCount = 0
                    }

                    step *= q2

                    state.isLowStepSizeReached = isLowStepSizeReached(step)
                    state.isHighStepSizeReached = isHighStepSizeReached(step)

                    statistic.returnsCount++

                    continue
                }
            } else {
                state.isHighStepSizeReached = false
                state.isLowStepSizeReached = false
            }

            for (i in outY.indices) {
                outY[i] = outY[i] + p1 *k1[i] + p2 *k2[i]
            }

            time += step
            statistic.stepsCount++

            executeStepHandlers(time, outY, state, statistic)

            val stepNew = min(q1, q2)*step

            state.freezeJacobiStepsCount++

            if(!(state.freezeJacobiStepsCount < maxFreezeSteps
                        && stepNew < step * stepGrowthCoefficient && e1 <= e2)){
                step = stepNew
                
                state.freezeJacobiStepsCount = 0
                state.isLowStepSizeReached = isLowStepSizeReached(step)
                state.isHighStepSizeReached = isHighStepSizeReached(step)
            }
        }
        return statistic
    }
}