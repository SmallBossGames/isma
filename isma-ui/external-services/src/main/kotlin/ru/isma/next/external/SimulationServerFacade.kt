package ru.isma.next.external

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import ru.isma.next.domain.models.SimulationProgress
import ru.isma.next.external.dtos.CachedSimulationResult
import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.CompilationErrorDto
import ru.isma.next.external.dtos.RunSimulationParams
import ru.isma.next.external.dtos.SyntaxTokenDto
import ru.isma.next.external.dtos.SyntaxTokenKind
import ru.isma.next.external.dtos.ValidationResult
import ru.nstu.isma.contracts.v1.compiler_service.*
import ru.nstu.isma.contracts.v1.simulation_service.*
import java.io.File
import java.nio.file.Path

class SimulationServerFacade(
    private val serverManager: SimulationServerManager,
) {
    private lateinit var grpcClient: GrpcSimulationClient
    private lateinit var httpClient: HttpSimulationClient
    private lateinit var compilerClient: GrpcLismaCompilerClient

    fun warmup() {
        val socketPaths = serverManager.start()
        grpcClient = GrpcSimulationClient(socketPaths.grpc)
        httpClient = HttpSimulationClient(socketPaths.http)
        compilerClient = GrpcLismaCompilerClient(socketPaths.grpc)
    }

    fun compileModel(lismaSourceCode: String): CompileResult {
        val request = CompileRequest.newBuilder()
            .setLismaSourceCode(lismaSourceCode)
            .build()
        val response = compilerClient.blockingStub.compile(request)
        return CompileResult(
            modelId = response.compiledModelId,
            errors = response.errorsList.map { it.toDto() },
            warnings = response.warningsList,
        )
    }

    fun validateModel(lismaSourceCode: String): ValidationResult {
        val request = ValidateRequest.newBuilder()
            .setLismaSourceCode(lismaSourceCode)
            .build()
        val response = compilerClient.blockingStub.validate(request)
        return ValidationResult(
            errors = response.errorsList.map { it.toDto() },
            warnings = response.warningsList,
        )
    }

    private fun CompilationError.toDto() = CompilationErrorDto(
        row = row,
        column = column,
        message = message,
    )

    fun highlightSource(lismaSourceCode: String): List<SyntaxTokenDto> {
        val response = compilerClient.highlight(lismaSourceCode)
        return response.tokensList.map { token ->
            SyntaxTokenDto(
                start = token.start,
                length = token.length,
                kind = when (token.kind) {
                    TokenKind.TOKEN_KIND_KEYWORD -> SyntaxTokenKind.KEYWORD
                    TokenKind.TOKEN_KIND_COMMENT -> SyntaxTokenKind.COMMENT
                    TokenKind.TOKEN_KIND_NUMBER -> SyntaxTokenKind.NUMBER
                    else -> SyntaxTokenKind.TEXT
                },
            )
        }
    }

    fun deleteCompiledModel(modelId: String): Boolean {
        val request = DeleteCompiledModelRequest.newBuilder()
            .setCompiledModelId(modelId)
            .build()
        return compilerClient.blockingStub.delete(request).success
    }

    fun runSimulation(params: RunSimulationParams): Long {
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

    fun getSimulationMethods(): List<String> {
        val response = grpcClient.blockingStub.listSimulationMethods(
            ListSimulationMethodsRequest.getDefaultInstance()
        )
        return response.methodsList.map { it.name }
    }

    fun shutdown() {
        grpcClient.shutdown()
        httpClient.close()
        compilerClient.shutdown()
        serverManager.stop()
    }
}
