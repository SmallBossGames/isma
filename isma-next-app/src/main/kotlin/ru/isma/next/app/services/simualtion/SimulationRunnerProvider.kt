package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.enumerables.SaveTarget
import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.next.core.sim.controller.services.runners.ISimulationRunnerProvider
import ru.nstu.isma.next.core.sim.controller.services.runners.InFileSimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.runners.InMemorySimulationRunner

class SimulationRunnerProvider(
    simulationParameters: SimulationParametersModel,
    inMemoryRunner: InMemorySimulationRunner,
    inFileRunner: InFileSimulationRunner,
) : ISimulationRunnerProvider {

    private val runner = when (simulationParameters.resultSavingParameters.savingTarget) {
        SaveTarget.MEMORY -> inMemoryRunner
        SaveTarget.FILE -> inFileRunner
    }

    override fun create() = runner
}