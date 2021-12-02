package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.enumerables.SaveTarget
import ru.isma.next.services.simulation.abstractions.interfaces.ISimulationSettingsProvider
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerFactory
import ru.nstu.isma.next.core.sim.controller.services.InFileSimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.InMemorySimulationRunner

class SimulationRunnerFactory(
    simulationSettingsProvider: ISimulationSettingsProvider,
    inMemoryRunner: InMemorySimulationRunner,
    inFileRunner: InFileSimulationRunner,
) : ISimulationRunnerFactory {

    private val runner = when (simulationSettingsProvider.simulationParameters.resultSavingParameters.savingTarget) {
        SaveTarget.MEMORY -> inMemoryRunner
        SaveTarget.FILE -> inFileRunner
    }

    override fun create() = runner
}