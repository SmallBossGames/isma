package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nstu.isma.contracts.simulation.*
import ru.isma.next.domain.models.SimulationProgress

class SimulationServerFacade(
    private val serverManager: SimulationServerManager,
) {
    private var client: GrpcSimulationClient? = null

    fun warmup() {
        val socketPath = serverManager.start()
        client = GrpcSimulationClient(socketPath)
        client!!.blockingStub.listSimulationMethods(ListSimulationMethodsRequest.getDefaultInstance())
    }

    fun runSimulation(params: RunSimulationParams): Long {
        val requestBuilder = RunSimulationRequest.newBuilder()
            .setStartTime(params.startTime)
            .setEndTime(params.endTime)
            .setInitialStep(params.initialStep)
            .setMethodName(params.methodName)
            .setLismaSourceCode(params.lismaSourceCode)

        if (params.isAccuracyInUse) {
            requestBuilder.setAccuracyConfig(
                AccuracyConfig.newBuilder()
                    .setAccuracy(params.accuracy)
                    .build()
            )
        }

        if (params.isStabilityControlInUse) {
            requestBuilder.setStabilityConfig(StabilityConfig.getDefaultInstance())
        }

        return client!!.blockingStub.runSimulation(requestBuilder.build()).simulationId
    }

    fun monitorSimulation(simulationId: Long, accuracy: Double): Flow<SimulationProgress> = flow {
        val request = MonitorSimulationRequest.newBuilder()
            .setSimulationId(simulationId)
            .setAccuracy(accuracy)
            .build()

        val iterator = client!!.blockingStub.monitorSimulation(request)
        while (iterator.hasNext()) {
            val response = iterator.next()
            emit(SimulationProgress(
                startTime = response.startTime,
                endTime = response.endTime,
                currentTime = response.currentTime,
            ))
        }
    }

    fun getSimulationResult(simulationId: Long): ByteArray {
        val request = GetSimulationResultRequest.newBuilder()
            .setSimulationId(simulationId)
            .build()
        return client!!.blockingStub.getSimulationResult(request).resultData.toByteArray()
    }

    fun shutdown() {
        client?.shutdown()
        serverManager.stop()
    }
}
