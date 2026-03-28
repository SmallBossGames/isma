package ru.isma.next.app.services.simualtion

import javafx.application.Platform
import javafx.collections.FXCollections
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import java.util.concurrent.Executors
import org.koin.core.component.KoinComponent
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.InProgressSimulationModel
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.external.BinaryEquationIndexProvider
import ru.isma.next.external.BinaryInputStreamPointProvider
import ru.isma.next.external.RunSimulationParams
import ru.isma.next.external.SimulationServerFacade
import ru.nstu.isma.intg.api.models.IntgMetricData
import ru.nstu.isma.intg.api.utilities.BinaryParser

class SimulationService(
    private val projectService: ProjectService,
    private val simulationResult: SimulationResultService,
    private val simulationParametersService: SimulationParametersService,
    private val serverFacade: SimulationServerFacade,
) : KoinComponent {
    val trackingTasks = FXCollections.observableArrayList<InProgressSimulationModel>()!!

    private val currentSimulationJobs = mutableMapOf<InProgressSimulationModel, Job>()
    private var taskNumber = 1

    fun simulate() {
        val simulationParameters = simulationParametersService.snapshot()
        val project = projectService.activeProject ?: return

        val trackingTask = InProgressSimulationModel(
            taskNumber,
            project.name,
            simulationParameters
        )
        taskNumber++

        SimulationScope.launch {
            try {
                val sourceCode = project.snapshot().fullText
                val params = simulationParameters.toRunSimulationParams(sourceCode)

                val simulationId = serverFacade.runSimulation(params)

                trackingTask.simulationId = simulationId

                Platform.runLater {
                    trackingTasks.add(trackingTask)
                }

                serverFacade.monitorSimulation(simulationId, 0.01).collect { progress ->
                    val normalized = ((progress.currentTime - progress.startTime) / (progress.endTime - progress.startTime)).coerceIn(0.0, 1.0)

                    trackingTask.commitProgress(normalized)
                }

                val inputStream = serverFacade.getSimulationResultAsInputStream(simulationId)
                val parseResult = BinaryParser.parseAll(inputStream)
                val metricData = IntgMetricData()

                val resultModel = CompletedSimulationModel(
                    trackingTask.id,
                    trackingTask.model,
                    BinaryEquationIndexProvider(parseResult.metadata),
                    metricData,
                    BinaryInputStreamPointProvider(parseResult.dataInputStream, parseResult.metadata.columnNames),
                    trackingTask.parameters
                )

                simulationResult.commitResult(resultModel)
            } catch (e: Throwable)
            {
                throw e
            }
            finally {
                currentSimulationJobs.remove(trackingTask)
                SimulationScope.launch(Dispatchers.JavaFx) {
                    trackingTasks.remove(trackingTask)
                }
            }
        }.also { job -> currentSimulationJobs[trackingTask] = job }
    }

    fun stopSimulation(trackingTask: InProgressSimulationModel) = SimulationScope.launch {
        trackingTask.simulationId?.let { serverFacade.cancelSimulation(it) }
        currentSimulationJobs[trackingTask]?.cancel()
    }

    companion object {
        private val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
        val SimulationScope = CoroutineScope(virtualThreadDispatcher + SupervisorJob())
    }
}

private fun SimulationParametersModel.toRunSimulationParams(lismaSourceCode: String) = RunSimulationParams(
    startTime = cauchyInitials.startTime,
    endTime = cauchyInitials.endTime,
    initialStep = cauchyInitials.initialStep,
    methodName = integrationMethodParameters.selectedMethod,
    accuracy = integrationMethodParameters.accuracy,
    isAccuracyInUse = integrationMethodParameters.isAccuracyInUse,
    isStabilityControlInUse = integrationMethodParameters.isStableInUse,
    lismaSourceCode = lismaSourceCode,
    eventDetectionGamma = if (eventDetectionParameters.isEventDetectionInUse) eventDetectionParameters.gamma else null,
    eventDetectionLowBorder = if (eventDetectionParameters.isEventDetectionInUse) eventDetectionParameters.lowBorder else null,
)
