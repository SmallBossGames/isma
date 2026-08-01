package ru.isma.next.app.services.simulation

import javafx.collections.FXCollections
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.services.project.IProjectService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simulation.ISimulationService
import ru.isma.next.app.services.simulation.ISimulationTaskService
import ru.isma.next.external.dtos.RunSimulationParams
import org.koin.core.component.KoinComponent

class SimulationService(
    private val projectService: IProjectService,
    private val simulationTaskService: ISimulationTaskService,
    private val simulationParametersService: SimulationParametersService,
) : ISimulationService, KoinComponent {

    override fun simulate() {
        val simulationParameters = simulationParametersService.snapshot()
        val project = projectService.activeProject ?: return

        simulationTaskService.submit(
            modelName = project.name,
            simulationParameters = simulationParameters,
        )
    }

    override fun stopSimulation(task: SimulationTask) {
        simulationTaskService.cancelTask(task)
    }


}
