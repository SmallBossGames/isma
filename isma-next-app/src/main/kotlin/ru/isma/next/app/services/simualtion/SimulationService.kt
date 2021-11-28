package ru.isma.next.app.services.simualtion

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
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
import tornadofx.getValue
import tornadofx.setValue
import kotlin.coroutines.EmptyCoroutineContext

class SimulationService(
    private val projectService: ProjectService,
    private val lismaPdeService: LismaPdeService,
    private val simulationParametersService: SimulationParametersService,
    private val simulationResult: SimulationResultService,
    private val simulationController: ISimulationCoreController,
) {
    val trackingTasks = FXCollections.observableArrayList<InProgressSimulationModel>()

    private val progressProperty = SimpleDoubleProperty()
    fun progressProperty() = progressProperty
    var progress by progressProperty

    private val isSimulationInProgressProperty = SimpleBooleanProperty()
    fun isSimulationInProgressProperty() = isSimulationInProgressProperty
    var isSimulationInProgress by isSimulationInProgressProperty

    private var currentSimulationJob: Job? = null

    fun simulate() {
        currentSimulationJob = SimulationScope.launch {
            try {
                simulateAsyncInternal()
            }
            finally {
                resetState()
            }
        }
    }

    private suspend fun simulateAsyncInternal() = coroutineScope {
        val trackingTask = InProgressSimulationModel(1)

        val sourceCode = projectService.activeProject?.snapshot() ?: return@coroutineScope

        val translationResult = lismaPdeService.translateLisma(sourceCode) as? SuccessTranslation ?: return@coroutineScope

        val initials = createCauchyInitials()

        translationResult.hsm.initTimeEquation(initials.start)

        val context = IntegratorApiParameters(
            hsm = translationResult.hsm,
            initials = createCauchyInitials(),
            stepChangeHandlers = arrayListOf(
                {
                    withContext(Dispatchers.JavaFx) {
                        progress = normalizeProgress(initials.start, initials.end, it)
                    }
                }
            )
        )

        withContext(Dispatchers.JavaFx) {
            isSimulationInProgress = true
            trackingTasks.add(trackingTask)
        }

        val result = simulationController.simulateAsync(context)

        withContext(Dispatchers.JavaFx) {
            trackingTasks.remove(trackingTask)
            simulationResult.simulationResult = result
        }

        val resultModel = CompletedSimulationModel(
            1,
            result.equationIndexProvider,
            result.metricData,
            result.resultPointProvider
        )

        simulationResult.commitSimulationResult(resultModel)
    }

    fun stopCurrentSimulation() {
        currentSimulationJob?.cancel()
    }

    private fun resetState() {
        currentSimulationJob = null
        isSimulationInProgress = false
        progress = 0.0
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

        val SimulationScope = CoroutineScope(EmptyCoroutineContext + SimulationSupervisorJob)

        private fun normalizeProgress(start: Double, end: Double, current: Double): Double {
            return ((current - start) / (end-start)).coerceIn(0.0, 1.0)
        }
    }
}