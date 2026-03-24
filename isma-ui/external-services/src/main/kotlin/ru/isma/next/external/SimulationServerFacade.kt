package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nstu.isma.contracts.simulation.*
import ru.isma.next.domain.models.SimulationProgress

class SimulationServerFacade(
    private val serverManager: SimulationServerManager,
) {
    private var grpcClient: GrpcSimulationClient? = null
    private lateinit var httpClient: HttpSimulationClient

    fun warmup() {
        val socketPaths = serverManager.start()
        grpcClient = GrpcSimulationClient(socketPaths.grpc)
        httpClient = HttpSimulationClient(socketPaths.http)
        grpcClient!!.blockingStub.listSimulationMethods(ListSimulationMethodsRequest.getDefaultInstance())
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

        return grpcClient!!.blockingStub.runSimulation(requestBuilder.build()).simulationId
    }

    fun monitorSimulation(simulationId: Long, accuracy: Double): Flow<SimulationProgress> = flow {
        val request = MonitorSimulationRequest.newBuilder()
            .setSimulationId(simulationId)
            .setAccuracy(accuracy)
            .build()

        val iterator = grpcClient!!.blockingStub.monitorSimulation(request)
        while (iterator.hasNext()) {
            val response = iterator.next()
            emit(SimulationProgress(
                startTime = response.startTime,
                endTime = response.endTime,
                currentTime = response.currentTime,
            ))
        }
    }

    suspend fun getSimulationResult(simulationId: Long): ByteArray {
        val request = GetSimulationResultRequest.newBuilder()
            .setSimulationId(simulationId)
            .build()
        val downloadUrl = grpcClient!!.blockingStub.getSimulationResult(request).downloadUrl
        if (downloadUrl.isNullOrBlank()) {
            throw IllegalStateException("Download URL is empty")
        }
        return httpClient.download(downloadUrl)
    }

    fun shutdown() {
        grpcClient?.shutdown()
        httpClient.close()
        serverManager.stop()
    }
}
