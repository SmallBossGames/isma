package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.DefaultEventDetector
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetectorFactory

class EventDetectorFactory(
    simulationParameters: SimulationParametersModel,
): IEventDetectorFactory {
    private val parameters = simulationParameters.eventDetectionParameters

    private val detector = if(parameters.isEventDetectionInUse){
        val gamma = parameters.gamma
        val lowerStepBound = if (parameters.isStepLimitInUse){
            parameters.lowBorder
        } else {
            0.0
        }

        DefaultEventDetector(gamma, lowerStepBound)
    } else {
        null
    }

    override fun create() = detector
}