package ru.isma.next.app.services.simualtion

import javafx.collections.FXCollections
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.InProgressSimulationModel
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.common.services.lisma.models.SuccessTranslation
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters

class SimulationService(
    private val projectService: ProjectService,
    private val lismaPdeService: LismaPdeService,
    private val simulationParametersService: SimulationParametersService,
    private val simulationResult: SimulationResultService,
    private val simulationController: ISimulationCoreController,
) {
    val trackingTasks = FXCollections.observableArrayList<InProgressSimulationModel>()

    private val currentSimulationJobs = mutableMapOf<InProgressSimulationModel, Job>()

    private var taskNumber = 1

    fun simulate() {
        val trackingTask = InProgressSimulationModel(
            taskNumber,
            simulationParametersService.snapshot()
        )

        taskNumber++

        val currentSimulationJob = SimulationScope.launch {
            try {
                val sourceCode = projectService.activeProject?.snapshot()
                    ?: return@launch

                val translationResult = lismaPdeService.translateLisma(sourceCode) as? SuccessTranslation
                    ?: return@launch

                val initials = createCauchyInitials()

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
                    result.equationIndexProvider,
                    result.metricData,
                    result.resultPointProvider,
                    trackingTask.parameters
                )

                simulationResult.commitResult(resultModel)
            }
            finally {
                SimulationScope.launch(Dispatchers.JavaFx) {
                    trackingTasks.remove(trackingTask)
                    currentSimulationJobs.remove(trackingTask)
                }
            }
        }

        currentSimulationJobs[trackingTask] = currentSimulationJob
    }

    fun stopSimulation(trackingTask: InProgressSimulationModel) {
        currentSimulationJobs.getOrDefault(trackingTask, null)?.cancel()
    }

    private fun createCauchyInitials(): CauchyInitials {
        return CauchyInitials(
            start = simulationParametersService.cauchyInitials.startTime,
            end = simulationParametersService.cauchyInitials.endTime,
            stepSize = simulationParametersService.cauchyInitials.step,
        )
    }

    companion object {
        private val SimulationSupervisorJob = SupervisorJob()

        val SimulationScope = CoroutineScope(Dispatchers.Default + SimulationSupervisorJob)

        private fun normalizeProgress(start: Double, end: Double, current: Double): Double {
            return ((current - start) / (end-start)).coerceIn(0.0, 1.0)
        }
    }
}