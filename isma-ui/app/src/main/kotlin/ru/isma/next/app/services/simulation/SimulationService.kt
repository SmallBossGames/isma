package ru.isma.next.app.services.simulation

import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.services.project.ProjectEditorPort

class SimulationService(
    private val editorPort: ProjectEditorPort,
    private val simulationTaskService: ISimulationTaskService,
) : ISimulationService {

    override fun simulate(project: IProjectModel, simulationParameters: SimulationParametersModel) {
        val sourceCode = editorPort.content(project).fullText

        simulationTaskService.submit(
            modelName = project.name,
            sourceCode = sourceCode,
            simulationParameters = simulationParameters,
        )
    }

    override fun stopSimulation(task: SimulationTask) {
        simulationTaskService.cancelTask(task)
    }
}
