package ru.isma.next.app.services.simualtion

import ru.isma.next.app.enumerables.SaveTarget
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerProvider
import ru.nstu.isma.next.core.sim.controller.services.InFileSimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.InMemorySimulationRunner

class SimulationRunnerProvider(
    private val simulationParametersService: SimulationParametersService,
    private val inMemoryRunner: InMemorySimulationRunner,
    private val inFileRunner: InFileSimulationRunner,
) : ISimulationRunnerProvider {
    override val runner: ISimulationRunner
        get() {
            return when(simulationParametersService.resultSaving.savingTarget){
                SaveTarget.MEMORY -> inMemoryRunner
                SaveTarget.FILE -> inFileRunner
            }
        }
}