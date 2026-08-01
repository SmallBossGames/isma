package ru.isma.next.app.services.simulation

import javafx.collections.ObservableList
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask

interface ISimulationTaskService : AutoCloseable {

    val tasks: ObservableList<SimulationTask>

    fun submit(
        modelName: String,
        simulationParameters: SimulationParametersModel,
    ): SimulationTask

    fun cancelTask(task: SimulationTask)
}
