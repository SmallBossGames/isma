package ru.isma.next.external

import kotlinx.coroutines.flow.Flow
import ru.isma.next.domain.models.SimulationProgress
import ru.isma.next.external.dtos.CachedSimulationResult
import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.RunSimulationParams
import ru.isma.next.external.dtos.SyntaxTokenDto
import ru.isma.next.external.dtos.ValidationResult

class SimulationServerFacade(
    private val serverManager: SimulationServerManager,
) {
    private var grpcClient: GrpcSimulationClient? = null
    private var compilerClient: GrpcLismaCompilerClient? = null
    private var compilationClient: CompilationClient? = null
    private var simulationClient: SimulationClient? = null
    private var downloadClient: DownloadClient? = null
    private var httpClient: HttpSimulationClient? = null

    fun warmup() {
        val socketPaths = serverManager.start()
        grpcClient = GrpcSimulationClient(socketPaths.grpc)
        httpClient = HttpSimulationClient(socketPaths.http)
        compilerClient = GrpcLismaCompilerClient(socketPaths.grpc)
        compilationClient = CompilationClient(compilerClient!!)
        simulationClient = SimulationClient(grpcClient!!)
        downloadClient = DownloadClient(grpcClient!!, httpClient!!)
    }

    internal fun setClients(
        grpcClient: GrpcSimulationClient,
        compilerClient: GrpcLismaCompilerClient,
        compilationClient: CompilationClient,
        simulationClient: SimulationClient,
        downloadClient: DownloadClient
    ) {
        this.grpcClient = grpcClient
        this.compilerClient = compilerClient
        this.compilationClient = compilationClient
        this.simulationClient = simulationClient
        this.downloadClient = downloadClient
    }

    private fun uninitialized(): Nothing = throw IllegalStateException("SimulationServerFacade is not initialized. Call warmup() first.")

    fun compileModel(lismaSourceCode: String): CompileResult =
        (compilationClient ?: uninitialized()).compile(lismaSourceCode)

    fun validateModel(lismaSourceCode: String): ValidationResult =
        (compilationClient ?: uninitialized()).validate(lismaSourceCode)

    fun getHighlighting(lismaSourceCode: String): List<SyntaxTokenDto> =
        (compilationClient ?: uninitialized()).highlight(lismaSourceCode)

    fun deleteCompiledModel(modelId: String): Boolean =
        (compilationClient ?: uninitialized()).deleteModel(modelId)

    fun runSimulation(params: RunSimulationParams): Long =
        (simulationClient ?: uninitialized()).run(params)

    fun monitorSimulation(simulationId: Long, accuracy: Double): Flow<SimulationProgress> =
        (simulationClient ?: uninitialized()).monitor(simulationId, accuracy)

    suspend fun downloadResultToCache(simulationId: Long): CachedSimulationResult =
        (downloadClient ?: uninitialized()).downloadResultToCache(simulationId)

    fun cancelSimulation(simulationId: Long) =
        (simulationClient ?: uninitialized()).cancel(simulationId)

    fun getSimulationMethods(): List<String> =
        (simulationClient ?: uninitialized()).listMethods()

    fun shutdown() {
        grpcClient?.shutdown()
        compilerClient?.shutdown()
        httpClient?.close()
        serverManager.stop()
    }
}
