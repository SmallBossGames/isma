package ru.isma.next.services.simulation.abstractions.interfaces

import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel

interface ISimulationSettingsProvider {
    val simulationParameters: SimulationParametersModel
}