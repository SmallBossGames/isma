package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.interfaces.ISimulationSettingsProvider

class SnapshotSimulationSettingsProvider(
    simulationParametersService: SimulationParametersService
): ISimulationSettingsProvider {
    override val simulationParameters = simulationParametersService.snapshot()
}