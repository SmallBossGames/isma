package ru.isma.next.external

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import ru.isma.next.domain.models.SimulationProgress
import ru.nstu.isma.contracts.simulation.*
import java.io.File
import java.nio.file.Path

class SimulationServerFacade(
    private val serverManager: SimulationServerManager,
) {
    private lateinit var grpcClient: GrpcSimulationClient
    private lateinit var httpClient: HttpSimulationClient

    fun warmup() {
        val socketPaths = serverManager.start()
        grpcClient = GrpcSimulationClient(socketPaths.grpc)
        httpClient = HttpSimulationClient(socketPaths.http)
        runCatching {
            grpcClient.blockingStub.listSimulationMethods(
                ListSimulationMethodsRequest.getDefaultInstance()
            )
        }
    }

    fun runSimulation(params: RunSimulationParams): Long {
        val request = RunSimulationRequest.newBuilder()
            .setStartTime(params.startTime)
            .setEndTime(params.endTime)
            .setInitialStep(params.initialStep)
            .setMethodName(params.methodName)
            .setLismaSourceCode(params.lismaSourceCode)
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

    fun monitorSimulation(simulationId: Long, accuracy: Double): Flow<SimulationProgress> {
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

    suspend fun downloadResultToCache(simulationId: Long): CachedSimulationResult {
        val request = GetSimulationResultRequest.newBuilder()
            .setSimulationId(simulationId)
            .build()
        val downloadUrl = grpcClient.blockingStub.getSimulationResult(request).downloadUrl
        if (downloadUrl.isNullOrBlank()) {
            throw IllegalStateException("Download URL is empty")
        }

        val cacheDir = getCacheDirectory()
        val cachedFile = File(cacheDir.toFile(), "simulation_$simulationId.bin")

        httpClient.downloadToFile(downloadUrl, cachedFile)

        val metadata = BinaryFilePointProvider.readMetadata(cachedFile)

        return CachedSimulationResult(cachedFile, metadata.columnNames)
    }

    private fun getCacheDirectory(): Path {
        val cacheDir = File(System.getProperty("java.io.tmpdir"), "isma-simulation-cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir.toPath()
    }

    fun cancelSimulation(simulationId: Long) {
        val request = CancelSimulationRequest.newBuilder()
            .setSimulationId(simulationId)
            .build()
        grpcClient.blockingStub.cancelSimulation(request)
    }

    fun shutdown() {
        grpcClient.shutdown()
        httpClient.close()
        serverManager.stop()
    }
}

data class CachedSimulationResult(
    val file: File,
    val columnNames: List<String>,
)
