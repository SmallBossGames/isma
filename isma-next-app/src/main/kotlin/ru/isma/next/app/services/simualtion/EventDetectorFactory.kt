package ru.isma.next.app.services.simualtion

import ru.nstu.isma.next.core.sim.controller.services.eventDetection.DefaultEventDetector
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetector
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetectorFactory

class EventDetectorFactory(
    private val parametersService: SimulationParametersService,
): IEventDetectorFactory {
    override fun create(): IEventDetector? {
        if(parametersService.eventDetection.isEventDetectionInUse){
            val gamma = parametersService.eventDetection.gamma
            val lowerStepBound = if (parametersService.eventDetection.isStepLimitInUse){
                parametersService.eventDetection.lowBorder
            } else {
                0.0
            }

            return DefaultEventDetector(gamma, lowerStepBound)
        }

        return null
    }
}