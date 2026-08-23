package ru.isma.next.app.services.simulation

import kotlinx.coroutines.flow.Flow
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask

interface ISimulationTaskService : AutoCloseable {

    val tasks: List<SimulationTask>

    val taskEvents: Flow<Unit>

    fun submit(
        modelName: String,
        sourceCode: String,
        simulationParameters: SimulationParametersModel,
    ): SimulationTask

    fun cancelTask(task: SimulationTask)

    fun removeTask(task: SimulationTask)
}
