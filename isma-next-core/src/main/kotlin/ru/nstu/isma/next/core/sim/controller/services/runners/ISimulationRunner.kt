package ru.nstu.isma.next.core.sim.controller.services.runners

import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters

interface ISimulationRunner {
    fun run(context: SimulationParameters): HybridSystemIntegrationResult
}

