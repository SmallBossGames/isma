package ru.nstu.isma.intg.core.methods

import org.apache.commons.lang3.builder.ToStringBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.nstu.isma.intg.api.methods.AccuracyIntgController
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.utils.maxOrThrow

import kotlin.math.abs

/**
 * @author Mariya Nasyrova
 * @since 14.08.14
 */
abstract class BaseAccuracyIntgController : AccuracyIntgController() {

    private val logger: Logger = LoggerFactory.getLogger(BaseAccuracyIntgController::class.java)

    override fun tune(
        fromPoint: IntgPoint,
        stages: Array<DoubleArray>,
        stepSolver: DaeSystemStepSolver
    ): AccuracyResults {
        val step = fromPoint.step
        val y = fromPoint.y

        var localError = getLocalError(stages, y)
        var actualAccuracy = getActualAccuracy(step, localError)
        val targetAccuracy = accuracy
        var accurate = actualAccuracy <= targetAccuracy

        logger.debug(
            "Accuracy controller. Step size: {}, isAccurate: {}, localError: {}, actualAccuracy: {}, targetAccuracy: {} \nstages: {}",
            step, accurate, localError, actualAccuracy, targetAccuracy,
            ToStringBuilder.reflectionToString(stages)
        )

        if (accurate) {
            return AccuracyResults(step, stages)
        }

        val point = fromPoint.copyLight()
        var tunedStep = step
        var tunedStages = stages

        var cycleCount: Long = 0
        while (!accurate) {
            if (cycleCount > 5) {
                logger.warn(
                    "Failed to make accurate. Accuracy cycle #{}. Initial step size: {}, Tuned step size: {}",
                    cycleCount, step, tunedStep
                )
                // Return original stages and step.
                return AccuracyResults(step, stages)
            }

            tunedStep = getTunedStepSize(getQ(targetAccuracy, actualAccuracy), tunedStep)
            point.step = tunedStep
            tunedStages = stepSolver.stages(point)

            localError = getLocalError(tunedStages, y)
            actualAccuracy = getActualAccuracy(tunedStep, localError)
            accurate = actualAccuracy <= targetAccuracy

            logger.debug(
                "Accuracy cycle #{}. Initial step size: {}, Tuned step size: {}, Is Accurate: {}",
                cycleCount, step, tunedStep, accurate
            )

            cycleCount++
        }

        return AccuracyResults(tunedStep, tunedStages)
    }

    private fun getLocalError(stages: Array<DoubleArray>, y: DoubleArray): Double {
        val deVarCount = stages.size
        val norms = DoubleArray(deVarCount)
        var localErrorEstimation: Double

        for (i in 0 until deVarCount) {
            localErrorEstimation = getLocalErrorEstimation(stages[i])
            norms[i] = abs(localErrorEstimation) / (abs(y[i]) + R)
        }

        return norms.maxOrThrow()
    }

    private fun getTunedStepSize(q: Double, step: Double): Double {
        return q * step
    }

    companion object {

        private const val R = 1.0
    }
}