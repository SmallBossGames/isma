package ru.isma.next.app.services.simualtion

import ru.isma.next.app.enumerables.SaveTarget
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerFactory
import ru.nstu.isma.next.core.sim.controller.services.InFileSimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.InMemorySimulationRunner

class SimulationRunnerFactory(
    private val simulationParametersService: SimulationParametersService,
    private val inMemoryRunner: InMemorySimulationRunner,
    private val inFileRunner: InFileSimulationRunner,
) : ISimulationRunnerFactory {
    override fun create(): ISimulationRunner {
        return when (simulationParametersService.resultSaving.savingTarget) {
            SaveTarget.MEMORY -> inMemoryRunner
            SaveTarget.FILE -> inFileRunner
        }
    }
}