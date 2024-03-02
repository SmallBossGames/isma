package ru.nstu.isma.next.core.sim.controller.services.eventDetection

import ru.nstu.isma.intg.api.calcmodel.Guard
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import kotlin.math.max
import kotlin.math.min

class DefaultEventDetector(
    gamma: Double,
    private val stepLowBound: Double
): IEventDetector {
    private val eventDetectionIntegrationController = EventDetectionIntgController(gamma).apply {
        enabled = true
    }

    // TODO: сделать правильно, чтобы первый шаг тоже оценивался.
    override fun predictNextStep(point: IntgPoint, guards: List<Guard>): Double {
        val eventFunctionGroups = guards.map { it.eventFunctionGroup }
        val predictedStep = eventDetectionIntegrationController.predictNextStep(point, eventFunctionGroups)
        // TODO: если так, то шагов около 8500
        return min(max(predictedStep, stepLowBound), point.nextStep)
        // TODO: а если так, то 170
        // return min(max(predictedStep, eventDetectionStepBoundLow), simulationInitials.step)
    }
}