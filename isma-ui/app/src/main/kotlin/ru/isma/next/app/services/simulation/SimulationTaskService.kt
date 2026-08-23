package ru.isma.next.app.services.simulation

import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.isma.javafx.extensions.coroutines.UiThreadExecutor
import ru.isma.next.app.models.CompilationErrorItem
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.services.ModelErrorService
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
    private val uiThreadExecutor: UiThreadExecutor,
) : ISimulationTaskService {

    private val tasksInternal = mutableListOf<SimulationTask>()

    private val taskEventsInternal = MutableSharedFlow<Unit>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val tasks: List<SimulationTask>
        get() = tasksInternal.toList()

    override val taskEvents: Flow<Unit> = taskEventsInternal

    private val currentJobs = mutableMapOf<SimulationTask, Job>()
    private var nextId = 1L

    private val virtualThreadDispatcher = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
    private val simulationScope = CoroutineScope(virtualThreadDispatcher + SupervisorJob())

    override fun close() {
        simulationScope.cancel()
        virtualThreadDispatcher.cancel()
    }

    override fun submit(
        modelName: String,
        sourceCode: String,
        simulationParameters: SimulationParametersModel,
    ): SimulationTask {
        val task = SimulationTask(nextId++, modelName, simulationParameters)

        val job = simulationScope.launch {
            try {
                runSimulationJob(task, sourceCode, simulationParameters)
            } finally {
                currentJobs.remove(task)
            }
        }.also { currentJobs[task] = it }

        return task
    }

    private suspend fun runSimulationJob(
        task: SimulationTask,
        sourceCode: String,
        simulationParameters: SimulationParametersModel,
    ) {
        val compileResult = try {
            serverFacade.compileModel(sourceCode)
        } catch (e: StatusException) {
            fail(task, "Compilation error: ${e.status.description}: ${e.message}")
            return
        } catch (e: StatusRuntimeException) {
            fail(task, "Compilation error: ${e.status.description}: ${e.message}")
            return
        } catch (e: Exception) {
            fail(task, "Compilation error: ${e.message}")
            return
        } catch (e: Error) {
            throw e
        }

        val errorItems = compileResult.errors.map { error: CompilationErrorDto ->
            CompilationErrorItem(error.row, error.column, "LISMA", error.message)
        }
        modelErrorService.putErrorList(errorItems)

        if (compileResult.errors.isNotEmpty()) {
            fail(task, "Compilation failed: ${compileResult.errors.joinToString("; ") { it.message }}")
            return
        }

        val runParams = simulationParameters.toRunSimulationParams(compileResult.modelId)
        val simulationId = serverFacade.runSimulation(runParams)

        onUi {
            tasksInternal.add(task)
            notifyChange()
        }

        try {
            serverFacade.monitorSimulation(simulationId, MONITORING_POLL_INTERVAL_SECONDS).collect { progress ->
                val normalized = ((progress.currentTime - progress.startTime) / (progress.endTime - progress.startTime)).coerceIn(0.0, 1.0)
                onUi {
                    task.progress = normalized
                    notifyChange()
                }
            }
        } catch (e: StatusException) {
            fail(task, "Monitor error: ${e.status.description}: ${e.message}")
            return
        } catch (e: StatusRuntimeException) {
            fail(task, "Monitor error: ${e.status.description}: ${e.message}")
            return
        } catch (e: Exception) {
            fail(task, "Monitor error: ${e.message}")
            return
        } catch (e: Error) {
            throw e
        }

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
            onUi {
                task.result = resultModel
                task.status = SimulationTaskStatus.COMPLETED
                task.progress = 1.0
                notifyChange()
            }
        } catch (e: StatusException) {
            fail(task, "Download error: ${e.status.description}: ${e.message}")
        } catch (e: StatusRuntimeException) {
            fail(task, "Download error: ${e.status.description}: ${e.message}")
        } catch (e: Exception) {
            fail(task, "Download error: ${e.message}")
        } catch (e: Error) {
            throw e
        }
    }

    override fun cancelTask(task: SimulationTask) {
        task.id.let { serverFacade.cancelSimulation(it) }
        currentJobs[task]?.cancel()
    }

    override fun removeTask(task: SimulationTask) {
        tasksInternal.remove(task)
        notifyChange()
    }

    private fun fail(task: SimulationTask, message: String) {
        onUi {
            task.status = SimulationTaskStatus.FAILED
            task.error = message
            notifyChange()
        }
    }

    private fun onUi(block: () -> Unit) {
        uiThreadExecutor.executeOnUi(block)
    }

    private fun notifyChange() {
        taskEventsInternal.tryEmit(Unit)
    }

    companion object {
        private const val MONITORING_POLL_INTERVAL_SECONDS = 0.01
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
