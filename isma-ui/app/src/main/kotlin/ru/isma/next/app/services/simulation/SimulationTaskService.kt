package ru.isma.next.app.services.simulation

import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.koin.core.component.KoinComponent
import ru.isma.javafx.extensions.coroutines.UiThreadExecutor
import ru.isma.next.app.models.ErrorViewModel
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import javafx.collections.ObservableList
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.project.IProjectService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.external.BinaryEquationIndexProvider
import ru.isma.next.external.SimulationServerFacade
import ru.isma.next.external.dtos.CachedSimulationResult
import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.CompilationErrorDto
import ru.isma.next.external.dtos.RunSimulationParams
import ru.isma.next.domain.models.MetricData
import ru.isma.next.domain.models.SimulationMetadata
import java.util.concurrent.Executors

class SimulationTaskService(
    private val serverFacade: SimulationServerFacade,
    private val modelErrorService: ModelErrorService,
    private val projectService: IProjectService,
    private val uiThreadExecutor: UiThreadExecutor,
) : ISimulationTaskService, KoinComponent {

    override val tasks: ObservableList<SimulationTask> = SimulationTask.ALL

    private val currentJobs = mutableMapOf<SimulationTask, Job>()
    private var nextId = 1L

    override fun submit(
        modelName: String,
        params: RunSimulationParams,
        simulationParameters: SimulationParametersModel,
    ): SimulationTask {
        val task = SimulationTask(nextId++, modelName, simulationParameters)

        val job = SimulationScope.launch {
            var compileResult: CompileResult? = null

            // Phase 1: Compile
            try {
                val sourceCode = projectService.activeProject?.snapshot()?.fullText ?: run {
                    uiThreadExecutor.executeOnUi {
                        task.setStatus(SimulationTaskStatus.FAILED)
                        task.setError("No active project")
                    }
                    return@launch
                }
                compileResult = serverFacade.compileModel(sourceCode)

                val errorViewModels = compileResult.errors.map { error: CompilationErrorDto ->
                    ErrorViewModel(error.row, error.column, "LISMA", error.message)
                }
                modelErrorService.putErrorList(errorViewModels)

                if (compileResult!!.errors.isNotEmpty()) {
                    uiThreadExecutor.executeOnUi {
                        task.setStatus(SimulationTaskStatus.FAILED)
                        task.setError("Compilation failed: ${compileResult.errors.joinToString("; ")}")
                    }
                    return@launch
                }
            } catch (e: Throwable) {
                uiThreadExecutor.executeOnUi {
                    task.setStatus(SimulationTaskStatus.FAILED)
                    task.setError("Compilation error: ${e.message}")
                }
                return@launch
            }

            // Phase 2: Run
            val runParams = simulationParameters.toRunSimulationParams(compileResult!!.modelId)
            val simulationId = serverFacade.runSimulation(runParams)

            uiThreadExecutor.executeOnUi { tasks.add(task) }

            // Phase 3: Monitor
            try {
                serverFacade.monitorSimulation(simulationId, 0.01).collect { progress ->
                    val normalized = ((progress.currentTime - progress.startTime) / (progress.endTime - progress.startTime)).coerceIn(0.0, 1.0)
                    uiThreadExecutor.executeOnUi { task.setProgress(normalized) }
                }
            } catch (e: Throwable) {
                uiThreadExecutor.executeOnUi {
                    task.setStatus(SimulationTaskStatus.FAILED)
                    task.setError("Monitor error: ${e.message}")
                }
                return@launch
            }

            // Phase 4: Download result
            try {
                val cachedResult: CachedSimulationResult = serverFacade.downloadResultToCache(simulationId)
                val resultModel = CompletedSimulationModel(
                    id = task.id.toInt(),
                    modelName = task.modelName,
                    equationIndexProvider = BinaryEquationIndexProvider(
                        SimulationMetadata(cachedResult.columnNames)
                    ),
                    metricData = MetricData(),
                    parameters = simulationParameters,
                    cachedFile = cachedResult.file,
                    cachedColumnNames = cachedResult.columnNames,
                )
                uiThreadExecutor.executeOnUi {
                    task.result = resultModel
                    task.setStatus(SimulationTaskStatus.COMPLETED)
                    task.setProgress(1.0)
                }
            } catch (e: Throwable) {
                uiThreadExecutor.executeOnUi {
                    task.setStatus(SimulationTaskStatus.FAILED)
                    task.setError("Download error: ${e.message}")
                }
            } finally {
                currentJobs.remove(task)
            }
        }.also { currentJobs[task] = it }

        return task
    }

    override fun cancelTask(task: SimulationTask) {
        task.id.let { serverFacade.cancelSimulation(it) }
        currentJobs.values.find { it == task }?.cancel()
    }

    companion object {
        private val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
        val SimulationScope = CoroutineScope(virtualThreadDispatcher + SupervisorJob())
    }
}

fun SimulationParametersModel.toRunSimulationParams(compiledModelId: String) = RunSimulationParams(
    startTime = cauchyInitials.startTime,
    endTime = cauchyInitials.endTime,
    initialStep = cauchyInitials.initialStep,
    methodName = integrationMethodParameters.selectedMethod,
    accuracy = integrationMethodParameters.accuracy,
    isAccuracyInUse = integrationMethodParameters.isAccuracyInUse,
    isStabilityControlInUse = integrationMethodParameters.isStableInUse,
    compiledModelId = compiledModelId,
    eventDetectionGamma = if (eventDetectionParameters.isEventDetectionInUse) eventDetectionParameters.gamma else null,
    eventDetectionLowBorder = if (eventDetectionParameters.isEventDetectionInUse) eventDetectionParameters.lowBorder else null,
)
