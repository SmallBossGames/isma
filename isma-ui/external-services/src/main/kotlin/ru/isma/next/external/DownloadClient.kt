package ru.isma.next.external

import ru.isma.next.external.dtos.CachedSimulationResult
import ru.nstu.isma.contracts.v1.simulation_service.GetSimulationResultRequest
import java.io.File
import java.nio.file.Path

class DownloadClient(
    private val grpcClient: GrpcSimulationClient,
    private val httpClient: HttpSimulationClient,
) {
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
}
