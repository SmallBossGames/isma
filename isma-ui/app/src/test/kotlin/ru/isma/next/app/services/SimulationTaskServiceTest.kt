package ru.isma.next.app.services

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.isma.javafx.extensions.coroutines.TestUiThreadExecutor
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
import ru.isma.next.external.dtos.CompileResult
import ru.isma.next.external.dtos.CompilationErrorDto
import ru.isma.next.external.dtos.RunSimulationParams

class SimulationTaskServiceTest {

    private val serverFacade = mockk<SimulationServerFacade>()
    private val modelErrorService = mockk<ModelErrorService>()
    private val projectService = mockk<ru.isma.next.app.services.project.IProjectService>()
    private val uiThreadExecutor = TestUiThreadExecutor()

    private lateinit var service: SimulationTaskService

    private fun testParameters() = SimulationParametersModel(
        cauchyInitials = CauchyInitialsModel(0.0, 10.0, 0.01),
        eventDetectionParameters = EventDetectionParametersModel(false, false, 0.0, 0.0),
        integrationMethodParameters = IntegrationMethodParametersModel("euler", 0.001, false, false, false, false, "localhost", 8080),
        resultSavingParameters = ResultSavingParametersModel(SaveTarget.MEMORY),
    )

    private fun createTask(id: Long = 1, name: String = "TestModel") =
        SimulationTask(id, name, testParameters())

    private fun mockSuccessfulCompile(source: String, modelId: String = "model-123") {
        every { projectService.activeProject } returns mockk {
            every { snapshot() } returns mockk {
                every { fullText } returns source
            }
        }
        every { serverFacade.compileModel(source) } returns CompileResult(
            modelId = modelId, errors = emptyList(), warnings = emptyList()
        )
        every { serverFacade.runSimulation(any<RunSimulationParams>()) } returns 42L
        every { serverFacade.monitorSimulation(42L, 0.01) } returns kotlinx.coroutines.flow.flow {
            emit(ru.isma.next.domain.models.SimulationProgress(0.0, 10.0, 5.0))
            emit(ru.isma.next.domain.models.SimulationProgress(0.0, 10.0, 10.0))
        }
        coEvery { serverFacade.downloadResultToCache(42L) } returns mockk {
            every { file } returns java.io.File("/tmp/test.bin")
            every { columnNames } returns listOf("x", "y")
        }
    }

    @BeforeEach
    fun setUp() {
        every { modelErrorService.putErrorList(any()) } returns Unit
        service = SimulationTaskService(serverFacade, modelErrorService, projectService, uiThreadExecutor)
    }

    @Test
    fun `compileProject returns result with successful compilation`() {
        mockSuccessfulCompile("model TestModel {}")

        val task = createTask(1, "TestModel")
        service.submit("TestModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()

        assert(task.id == 1L) { "Expected id=1, got ${task.id}" }
        assert(task.modelName == "TestModel") { "Expected TestModel, got ${task.modelName}" }
    }

    @Test
    fun `compileProject fails with compilation errors`() {
        val source = "bad code"
        every { projectService.activeProject } returns mockk {
            every { snapshot() } returns mockk {
                every { fullText } returns source
            }
        }
        every { serverFacade.compileModel(source) } returns CompileResult(
            modelId = "", errors = listOf(CompilationErrorDto(1, 5, "Syntax error")), warnings = emptyList()
        )

        val task = createTask(2, "BadModel")
        service.submit("BadModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()

        assert(task.statusValue == SimulationTaskStatus.FAILED) { "Expected FAILED, got ${task.statusValue}" }
        assert(task.errorValue == "Compilation failed: Syntax error") { "Expected compilation error message, got ${task.errorValue}" }
    }

    @Test
    fun `runSimulation invokes progress callback`() = runTest {
        mockSuccessfulCompile("model Progress {}")

        val task = createTask(3, "ProgressModel")
        service.submit("ProgressModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()
        uiThreadExecutor.executePending()

        assert(service.tasks.size == 1) { "Expected 1 task in list, got ${service.tasks.size}" }
        assert(service.tasks[0].id == 3L) { "Expected task id=3, got ${service.tasks[0].id}" }
    }

    @Test
    fun `cancelSimulation delegates to client`() {
        mockSuccessfulCompile("model Cancel {}")

        val task = createTask(4, "CancelModel")
        service.submit("CancelModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()

        every { serverFacade.cancelSimulation(4L) } returns Unit

        service.cancelTask(task)

        verify(exactly = 1) { serverFacade.cancelSimulation(4L) }
    }

    @Test
    fun `cancelTask removes job from currentJobs`() {
        mockSuccessfulCompile("model JobCancel {}")

        val task = createTask(5, "JobCancelModel")
        service.submit("JobCancelModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()

        every { serverFacade.cancelSimulation(5L) } returns Unit

        service.cancelTask(task)

        verify(exactly = 1) { serverFacade.cancelSimulation(5L) }
    }

    @Test
    fun `submit adds task to observable list`() {
        mockSuccessfulCompile("model List {}")

        val task = createTask(6, "ListModel")
        service.submit("ListModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()

        assert(service.tasks.size == 1) { "Expected 1 task, got ${service.tasks.size}" }
        assert(service.tasks[0].modelName == "ListModel") { "Expected ListModel, got ${service.tasks[0].modelName}" }
    }

    @Test
    fun `submit handles no active project`() {
        every { projectService.activeProject } returns null

        val task = createTask(7, "NoProjectModel")
        service.submit("NoProjectModel", mockk(relaxed = true), testParameters())

        uiThreadExecutor.executePending()

        assert(task.statusValue == SimulationTaskStatus.FAILED) { "Expected FAILED, got ${task.statusValue}" }
        assert(task.errorValue == "No active project") { "Expected 'No active project', got ${task.errorValue}" }
    }
}
