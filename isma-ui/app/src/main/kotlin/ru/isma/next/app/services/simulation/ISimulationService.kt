package ru.isma.next.app.services.simulation

import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask

interface ISimulationService {

    fun simulate(project: IProjectModel, simulationParameters: SimulationParametersModel)

    fun stopSimulation(task: SimulationTask)
}
