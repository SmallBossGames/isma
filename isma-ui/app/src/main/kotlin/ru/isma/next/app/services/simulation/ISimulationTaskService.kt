package ru.isma.next.app.services.simulation

import javafx.collections.ObservableList
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.external.dtos.RunSimulationParams

interface ISimulationTaskService {

    val tasks: ObservableList<SimulationTask>

    fun submit(
        modelName: String,
        params: RunSimulationParams,
        simulationParameters: SimulationParametersModel,
    ): SimulationTask

    fun cancelTask(task: SimulationTask)
}
