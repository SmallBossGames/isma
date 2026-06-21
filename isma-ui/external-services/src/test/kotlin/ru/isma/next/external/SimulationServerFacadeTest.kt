package ru.isma.next.external

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.isma.next.domain.models.SimulationProgress
import ru.isma.next.external.dtos.CachedSimulationResult
import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.CompilationErrorDto
import ru.isma.next.external.dtos.RunSimulationParams
import ru.isma.next.external.dtos.ValidationResult
import ru.isma.next.external.dtos.SyntaxTokenDto
import ru.isma.next.external.dtos.SyntaxTokenKind
import java.io.File

class SimulationServerFacadeTest {

    private val serverManager = mockk<SimulationServerManager>()
    private val grpcClient = mockk<GrpcSimulationClient>()
    private val compilerClient = mockk<GrpcLismaCompilerClient>()
    private val compilationClient = mockk<CompilationClient>()
    private val simulationClient = mockk<SimulationClient>()
    private val downloadClient = mockk<DownloadClient>()

    private lateinit var facade: SimulationServerFacade

    @BeforeEach
    fun setUp() {
        facade = SimulationServerFacade(serverManager)
        facade.setClients(grpcClient, compilerClient, compilationClient, simulationClient, downloadClient)
    }

    @AfterEach
    fun tearDown() {
        facade.shutdown()
    }

    @Test
    fun `compile delegates to CompilationClient`() {
        val sourceCode = "model TestModel {}"
        val expectedResult = CompileResult(
            modelId = "compiled-model-1",
            errors = listOf(CompilationErrorDto(1, 0, "Test error")),
            warnings = emptyList()
        )

        every { compilationClient.compile(sourceCode) } returns expectedResult

        val result = facade.compileModel(sourceCode)

        assert(result.modelId == "compiled-model-1") { "Expected modelId 'compiled-model-1', got ${result.modelId}" }
        assert(result.errors.size == 1) { "Expected 1 error, got ${result.errors.size}" }
        assert(result.errors[0].message == "Test error") { "Expected 'Test error', got ${result.errors[0].message}" }
        verify { compilationClient.compile(sourceCode) }
    }

    @Test
    fun `runSimulation delegates to SimulationClient`() = runTest {
        val params = RunSimulationParams(
            startTime = 0.0,
            endTime = 10.0,
            initialStep = 0.01,
            methodName = "euler",
            accuracy = 0.001,
            isAccuracyInUse = true,
            isStabilityControlInUse = false,
            compiledModelId = "model-123"
        )
        val simulationId = 99L

        every { simulationClient.run(params) } returns simulationId

        val result = facade.runSimulation(params)

        assert(result == simulationId) { "Expected simulationId $simulationId, got $result" }
        verify { simulationClient.run(params) }
    }

    @Test
    fun `downloadSimulationResult delegates to DownloadClient`() = runTest {
        val simulationId = 42L
        val expectedFile = File("/tmp/simulation_42.bin")
        val expectedResult = CachedSimulationResult(
            file = expectedFile,
            columnNames = listOf("x", "y", "z")
        )

        coEvery { downloadClient.downloadResultToCache(simulationId) } returns expectedResult

        val result = facade.downloadResultToCache(simulationId)

        assert(result.file == expectedFile) { "Expected file $expectedFile, got ${result.file}" }
        assert(result.columnNames == listOf("x", "y", "z")) { "Expected column names, got ${result.columnNames}" }
        coVerify { downloadClient.downloadResultToCache(simulationId) }
    }

    @Test
    fun `monitorSimulation delegates to SimulationClient`() = runTest {
        val simulationId = 77L
        val progressFlow = kotlinx.coroutines.flow.flow<SimulationProgress> {
            emit(SimulationProgress(0.0, 10.0, 5.0))
            emit(SimulationProgress(0.0, 10.0, 10.0))
        }

        every { simulationClient.monitor(simulationId, 0.1) } returns progressFlow

        val flow = facade.monitorSimulation(simulationId, 0.1)

        var collected = 0
        flow.collect { collected++ }
        assert(collected == 2) { "Expected 2 progress updates, got $collected" }
        verify { simulationClient.monitor(simulationId, 0.1) }
    }

    @Test
    fun `cancelSimulation delegates to SimulationClient`() {
        val simulationId = 55L

        every { simulationClient.cancel(simulationId) } returns Unit

        facade.cancelSimulation(simulationId)

        verify { simulationClient.cancel(simulationId) }
    }

    @Test
    fun `validateModel delegates to CompilationClient`() {
        val sourceCode = "model Validate {}"
        val expectedResult = ValidationResult(
            errors = emptyList(),
            warnings = listOf("Warning 1")
        )

        every { compilationClient.validate(sourceCode) } returns expectedResult

        val result = facade.validateModel(sourceCode)

        assert(result.warnings.size == 1) { "Expected 1 warning, got ${result.warnings.size}" }
        verify { compilationClient.validate(sourceCode) }
    }

    @Test
    fun `getHighlighting delegates to CompilationClient`() {
        val sourceCode = "model Test {}"
        val expectedTokens = listOf(
            SyntaxTokenDto(0, 5, SyntaxTokenKind.KEYWORD),
            SyntaxTokenDto(6, 4, SyntaxTokenKind.TEXT)
        )

        every { compilationClient.highlight(sourceCode) } returns expectedTokens

        val result = facade.getHighlighting(sourceCode)

        assert(result.size == 2) { "Expected 2 tokens, got ${result.size}" }
        assert(result[0].kind == SyntaxTokenKind.KEYWORD) { "Expected KEYWORD, got ${result[0].kind}" }
        verify { compilationClient.highlight(sourceCode) }
    }

    @Test
    fun `deleteCompiledModel delegates to CompilationClient`() {
        val modelId = "model-to-delete"

        every { compilationClient.deleteModel(modelId) } returns true

        val result = facade.deleteCompiledModel(modelId)

        assert(result == true) { "Expected true, got $result" }
        verify { compilationClient.deleteModel(modelId) }
    }

    @Test
    fun `getSimulationMethods delegates to SimulationClient`() {
        val expectedMethods = listOf("euler", "rk4", "rkf45")

        every { simulationClient.listMethods() } returns expectedMethods

        val result = facade.getSimulationMethods()

        assert(result == expectedMethods) { "Expected $expectedMethods, got $result" }
        verify { simulationClient.listMethods() }
    }
}
