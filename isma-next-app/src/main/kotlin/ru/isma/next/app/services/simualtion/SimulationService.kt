package ru.isma.next.app.services.simualtion

import javafx.collections.FXCollections
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.koin.core.component.KoinComponent
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.InProgressSimulationModel
import ru.isma.next.app.services.koin.SimulationScope
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.common.services.lisma.models.SuccessTranslation
import ru.isma.next.services.simulation.abstractions.interfaces.ISimulationSettingsProvider
import ru.isma.next.services.simulation.abstractions.models.CauchyInitialsModel
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.next.core.sim.controller.services.controllers.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters

class SimulationService(
    private val projectService: ProjectService,
    private val lismaPdeService: LismaPdeService,
    private val simulationResult: SimulationResultService,
) : KoinComponent {
    val trackingTasks = FXCollections.observableArrayList<InProgressSimulationModel>()!!

    private val currentSimulationJobs = mutableMapOf<InProgressSimulationModel, Job>()

    private var taskNumber = 1

    fun simulate() {
        val simulationScope = getKoin().createScope<SimulationScope>()
        val simulationController: ISimulationCoreController = simulationScope.get()
        val parametersService: ISimulationSettingsProvider = simulationScope.get()
        val project = projectService.activeProject ?: return

        val trackingTask = InProgressSimulationModel(
            taskNumber,
            project.name,
            parametersService.simulationParameters
        )

        taskNumber++

        val currentSimulationJob = SimulationScope.launch {
            try {
                val sourceCode = project.snapshot()

                val translationResult = lismaPdeService.translateLisma(sourceCode) as? SuccessTranslation
                    ?: return@launch

                val initials = parametersService.simulationParameters.cauchyInitials.toCauchyInitials()

                val hsm = translationResult.hsm.apply {
                    initTimeEquation(initials.start)
                }

                val context = IntegratorApiParameters(
                    hsm = hsm,
                    initials = initials,
                    stepChangeHandlers = arrayListOf(
                        {
                            val progress = normalizeProgress(initials.start, initials.end, it)
                            trackingTask.commitProgress(progress)
                        }
                    )
                )

                withContext(Dispatchers.JavaFx) {
                    trackingTasks.add(trackingTask)
                }

                val result = simulationController.simulateAsync(context)

                val resultModel = CompletedSimulationModel(
                    trackingTask.id,
                    trackingTask.model,
                    result.equationIndexProvider,
                    result.metricData,
                    result.resultPointProvider,
                    trackingTask.parameters
                )

                simulationResult.commitResult(resultModel)
            }
            finally {
                simulationScope.close()

                currentSimulationJobs.remove(trackingTask)

                SimulationScope.launch(Dispatchers.JavaFx) {
                    trackingTasks.remove(trackingTask)
                }
            }
        }

        currentSimulationJobs[trackingTask] = currentSimulationJob
    }

    fun stopSimulation(trackingTask: InProgressSimulationModel) {
        currentSimulationJobs.getOrDefault(trackingTask, null)?.cancel()
    }

    companion object {
        private val SimulationSupervisorJob = SupervisorJob()

        val SimulationScope = CoroutineScope(Dispatchers.Default + SimulationSupervisorJob)

        private fun normalizeProgress(start: Double, end: Double, current: Double): Double {
            return ((current - start) / (end-start)).coerceIn(0.0, 1.0)
        }

        private fun CauchyInitialsModel.toCauchyInitials() = CauchyInitials(startTime, endTime, initialStep)
    }
}