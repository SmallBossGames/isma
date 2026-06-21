package ru.isma.next.app.services.simulation

import javafx.collections.FXCollections
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.external.RunSimulationParams
import org.koin.core.component.KoinComponent

class SimulationService(
    private val projectService: ProjectService,
    private val simulationTaskService: SimulationTaskService,
    private val simulationParametersService: SimulationParametersService,
) : KoinComponent {

    fun simulate() {
        val simulationParameters = simulationParametersService.snapshot()
        val project = projectService.activeProject ?: return

        val runParams = simulationParameters.toRunSimulationParams("")

        simulationTaskService.submit(
            modelName = project.name,
            params = runParams,
            simulationParameters = simulationParameters,
        )
    }

    fun stopSimulation(task: SimulationTask) {
        simulationTaskService.cancelTask(task)
    }

    companion object {
        val SimulationScope = SimulationTaskService.SimulationScope
    }
}
