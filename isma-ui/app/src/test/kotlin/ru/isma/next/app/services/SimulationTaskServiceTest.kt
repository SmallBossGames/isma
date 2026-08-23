package ru.isma.next.app.services

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.isma.javafx.extensions.coroutines.UiThreadExecutor
import ru.isma.next.app.models.LismaTextModel
import ru.isma.next.app.models.simulation.CauchyInitialsModel
import ru.isma.next.app.models.simulation.EventDetectionParametersModel
import ru.isma.next.app.models.simulation.IntegrationMethodParametersModel
import ru.isma.next.app.models.simulation.ResultSavingParametersModel
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.services.simulation.SimulationTaskService
import ru.isma.next.external.SimulationServerFacade
import ru.isma.next.external.dtos.CachedSimulationResult
import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.CompilationErrorDto
import ru.isma.next.external.dtos.RunSimulationParams
import ru.isma.next.domain.models.SimulationProgress
import java.io.File

class SimulationTaskServiceTest {

    private class QueueUiThreadExecutor : UiThreadExecutor {
        private val queue = ArrayDeque<() -> Unit>()
        private val lock = Any()

        override fun executeOnUi(runnable: () -> Unit) {
            synchronized(lock) { queue.addLast(runnable) }
        }

        fun drain() {
            while (true) {
                val runnable = synchronized(lock) { queue.removeFirstOrNull() } ?: break
                runnable()
            }
        }
    }

    private val serverFacade = mockk<SimulationServerFacade>()
    private val modelErrorService = mockk<ModelErrorService>()
    private val uiExecutor = QueueUiThreadExecutor()

    private lateinit var service: SimulationTaskService

    private fun testParameters() = SimulationParametersModel(
        cauchyInitials = CauchyInitialsModel(0.0, 10.0, 0.01),
        eventDetectionParameters = EventDetectionParametersModel(false, false, 0.0, 0.0),
        integrationMethodParameters = IntegrationMethodParametersModel("euler", 0.001, false, false, false, false, "localhost", 8080),
        resultSavingParameters = ResultSavingParametersModel(SaveTarget.MEMORY),
    )

    private fun mockSuccessfulCompile(source: String, modelId: String = "model-123") {
        every { serverFacade.compileModel(source) } returns CompileResult(
            modelId = modelId, errors = emptyList(), warnings = emptyList()
        )
        every { serverFacade.runSimulation(any<RunSimulationParams>()) } returns 42L
        every { serverFacade.monitorSimulation(42L, 0.01) } returns flow {
            emit(SimulationProgress(0.0, 10.0, 5.0))
            emit(SimulationProgress(0.0, 10.0, 10.0))
        }
        coEvery { serverFacade.downloadResultToCache(42L) } returns CachedSimulationResult(
            file = File("/tmp/test.bin"),
            columnNames = listOf("x", "y")
        )
    }

    private fun awaitUi(timeoutMs: Long = 5000, condition: () -> Boolean) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            uiExecutor.drain()
            if (condition()) return
            Thread.sleep(10)
        }
        uiExecutor.drain()
        assert(condition()) { "Condition not met within ${timeoutMs}ms" }
    }

    @BeforeEach
    fun setUp() {
        every { modelErrorService.putErrorList(any()) } returns Unit
        service = SimulationTaskService(serverFacade, modelErrorService, uiExecutor)
    }

    @Test
    fun `submit returns task with correct id and modelName`() {
        mockSuccessfulCompile("model TestModel {}")

        val task = service.submit("TestModel", LismaTextModel("model TestModel {}"), testParameters())

        assert(task.id == 1L) { "Expected id=1, got ${task.id}" }
        assert(task.modelName == "TestModel") { "Expected TestModel, got ${task.modelName}" }
    }

    @Test
    fun `submit fails with compilation errors`() {
        val source = "bad code"
        every { serverFacade.compileModel(source) } returns CompileResult(
            modelId = "", errors = listOf(CompilationErrorDto(1, 5, "Syntax error")), warnings = emptyList()
        )

        val task = service.submit("BadModel", LismaTextModel(source), testParameters())

        awaitUi { task.status == SimulationTaskStatus.FAILED }

        assert(task.error == "Compilation failed: Syntax error") { "Expected compilation error message, got ${task.error}" }
    }

    @Test
    fun `submit adds task to list and completes on success`() {
        mockSuccessfulCompile("model Progress {}")

        val task = service.submit("ProgressModel", LismaTextModel("model Progress {}"), testParameters())

        awaitUi { service.tasks.any { it.id == task.id } && task.status == SimulationTaskStatus.COMPLETED }

        assert(service.tasks.size == 1) { "Expected 1 task in list, got ${service.tasks.size}" }
        assert(service.tasks[0].id == task.id) { "Expected task id=${task.id}, got ${service.tasks[0].id}" }
        assert(task.progress == 1.0) { "Expected progress 1.0, got ${task.progress}" }
    }

    @Test
    fun `cancelTask delegates to serverFacade`() {
        val task = SimulationTask(4L, "CancelModel", testParameters())
        every { serverFacade.cancelSimulation(4L) } returns Unit

        service.cancelTask(task)

        verify(exactly = 1) { serverFacade.cancelSimulation(4L) }
    }

    @Test
    fun `removeTask removes task from list`() {
        mockSuccessfulCompile("model List {}")

        val task = service.submit("ListModel", LismaTextModel("model List {}"), testParameters())

        awaitUi { service.tasks.any { it.id == task.id } }

        service.removeTask(task)

        assert(service.tasks.none { it.id == task.id }) { "Expected task removed from list" }
    }
}
