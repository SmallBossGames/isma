package ru.nstu.isma.intg.core.methods

import org.apache.commons.lang3.builder.ToStringBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.api.methods.StabilityIntgController

/**
 * @author Mariya Nasyrova
 * @since 29.08.14
 */
abstract class BaseStabilityIntgController : StabilityIntgController() {
    private val logger: Logger = LoggerFactory.getLogger(BaseStabilityIntgController::class.java)

    override fun predictNextStepSize(toPoint: IntgPoint): Double {
        var nextStepSize = toPoint.nextStep

        val maxJacobiEigenValue = getMaxJacobiEigenValue(toPoint)
        val isStable = maxJacobiEigenValue <= getStabilityInterval()

        logger.debug(
            "Stability controller. Step: {}, isStable: {}, maxJacobiEigenValue: {}, stabilityInterval: {} \npoint: {}",
            toPoint.step, isStable, maxJacobiEigenValue, getStabilityInterval(),
            ToStringBuilder.reflectionToString(toPoint)
        )


        if (!isStable) {
            nextStepSize = getTunedStepSize(maxJacobiEigenValue, toPoint.nextStep)

            logger.debug("Stability controller predicted a next step size: {}", nextStepSize)
        }

        return nextStepSize
    }

    private fun getTunedStepSize(maxJacobiEigenValue: Double, step: Double): Double {
        return getStabilityInterval() * step / maxJacobiEigenValue
    }
}
