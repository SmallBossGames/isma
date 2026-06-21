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
    private lateinit var grpcClient: GrpcSimulationClient
    private lateinit var compilerClient: GrpcLismaCompilerClient
    private lateinit var compilationClient: CompilationClient
    private lateinit var simulationClient: SimulationClient
    private lateinit var downloadClient: DownloadClient

    fun warmup() {
        val socketPaths = serverManager.start()
        grpcClient = GrpcSimulationClient(socketPaths.grpc)
        val httpClient = HttpSimulationClient(socketPaths.http)
        compilerClient = GrpcLismaCompilerClient(socketPaths.grpc)
        compilationClient = CompilationClient(compilerClient)
        simulationClient = SimulationClient(grpcClient)
        downloadClient = DownloadClient(grpcClient, httpClient)
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

    fun compileModel(lismaSourceCode: String): CompileResult =
        compilationClient.compile(lismaSourceCode)

    fun validateModel(lismaSourceCode: String): ValidationResult =
        compilationClient.validate(lismaSourceCode)

    fun getHighlighting(lismaSourceCode: String): List<SyntaxTokenDto> =
        compilationClient.highlight(lismaSourceCode)

    fun deleteCompiledModel(modelId: String): Boolean =
        compilationClient.deleteModel(modelId)

    fun runSimulation(params: RunSimulationParams): Long =
        simulationClient.run(params)

    fun monitorSimulation(simulationId: Long, accuracy: Double): Flow<SimulationProgress> =
        simulationClient.monitor(simulationId, accuracy)

    suspend fun downloadResultToCache(simulationId: Long): CachedSimulationResult =
        downloadClient.downloadResultToCache(simulationId)

    fun cancelSimulation(simulationId: Long) =
        simulationClient.cancel(simulationId)

    fun getSimulationMethods(): List<String> =
        simulationClient.listMethods()

    fun shutdown() {
        grpcClient.shutdown()
        compilerClient.shutdown()
        serverManager.stop()
    }
}
