package ru.isma.next.external

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import ru.isma.next.domain.models.SimulationProgress
import ru.isma.next.external.dtos.RunSimulationParams
import ru.nstu.isma.contracts.v1.simulation_service.AccuracyConfig
import ru.nstu.isma.contracts.v1.simulation_service.CancelSimulationRequest
import ru.nstu.isma.contracts.v1.simulation_service.ListSimulationMethodsRequest
import ru.nstu.isma.contracts.v1.simulation_service.MonitorSimulationRequest
import ru.nstu.isma.contracts.v1.simulation_service.RunSimulationRequest
import ru.nstu.isma.contracts.v1.simulation_service.StabilityConfig

class SimulationClient(
    private val grpcClient: GrpcSimulationClient,
) {
    fun run(params: RunSimulationParams): Long {
        val request = RunSimulationRequest.newBuilder()
            .setStartTime(params.startTime)
            .setEndTime(params.endTime)
            .setInitialStep(params.initialStep)
            .setMethodName(params.methodName)
            .setCompiledModelId(params.compiledModelId)
            .apply {
                if (params.isAccuracyInUse) {
                    setAccuracyConfig(
                        AccuracyConfig.newBuilder()
                            .setAccuracy(params.accuracy)
                            .build()
                    )
                }
                if (params.isStabilityControlInUse) {
                    setStabilityConfig(StabilityConfig.getDefaultInstance())
                }
            }
            .build()

        return grpcClient.blockingStub.runSimulation(request).simulationId
    }

    fun monitor(simulationId: Long, accuracy: Double): Flow<SimulationProgress> {
        val request = MonitorSimulationRequest.newBuilder()
            .setSimulationId(simulationId)
            .setAccuracy(accuracy)
            .build()

        val iterator = grpcClient.blockingStub.monitorSimulation(request)

        return kotlinx.coroutines.flow.flow {
            while (iterator.hasNext()) {
                currentCoroutineContext().ensureActive()
                val response = iterator.next()
                emit(SimulationProgress(
                    startTime = response.startTime,
                    endTime = response.endTime,
                    currentTime = response.currentTime,
                ))
            }
        }
    }

    fun cancel(simulationId: Long) {
        val request = CancelSimulationRequest.newBuilder()
            .setSimulationId(simulationId)
            .build()
        grpcClient.blockingStub.cancelSimulation(request)
    }

    fun listMethods(): List<String> {
        val response = grpcClient.blockingStub.listSimulationMethods(
            ListSimulationMethodsRequest.getDefaultInstance()
        )
        return response.methodsList.map { it.name }
    }
}
