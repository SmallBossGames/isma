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
    private val compilationClient: CompilationClient? = null,
    private val simulationClient: SimulationClient? = null,
    private val downloadClient: DownloadClient? = null,
) {
    private val _compilationClient = compilationClient ?: CompilationClient(GrpcLismaCompilerClient(""))
    private val _simulationClient = simulationClient ?: SimulationClient(GrpcSimulationClient(""))
    private val _downloadClient = downloadClient ?: DownloadClient(GrpcSimulationClient(""), HttpSimulationClient(""))

    fun warmup() {
        val socketPaths = serverManager.start()
        _compilationClient.compile("") // force initialization
    }

    fun compileModel(lismaSourceCode: String): CompileResult =
        _compilationClient.compile(lismaSourceCode)

    fun validateModel(lismaSourceCode: String): ValidationResult =
        _compilationClient.validate(lismaSourceCode)

    fun getHighlighting(lismaSourceCode: String): List<SyntaxTokenDto> =
        _compilationClient.highlight(lismaSourceCode)

    fun deleteCompiledModel(modelId: String): Boolean =
        _compilationClient.deleteModel(modelId)

    fun runSimulation(params: RunSimulationParams): Long =
        _simulationClient.run(params)

    fun monitorSimulation(simulationId: Long, accuracy: Double): Flow<SimulationProgress> =
        _simulationClient.monitor(simulationId, accuracy)

    suspend fun downloadResultToCache(simulationId: Long): CachedSimulationResult =
        _downloadClient.downloadResultToCache(simulationId)

    fun cancelSimulation(simulationId: Long) =
        _simulationClient.cancel(simulationId)

    fun getSimulationMethods(): List<String> =
        _simulationClient.listMethods()

    fun shutdown() {
        serverManager.stop()
    }
}
