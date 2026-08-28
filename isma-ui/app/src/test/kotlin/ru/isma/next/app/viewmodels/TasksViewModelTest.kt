package ru.isma.next.app.viewmodels

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.jupiter.api.Test
import ru.isma.next.app.models.LismaTextModel
import ru.isma.next.app.models.simulation.CauchyInitialsModel
import ru.isma.next.app.models.simulation.EventDetectionParametersModel
import ru.isma.next.app.models.simulation.IntegrationMethodParametersModel
import ru.isma.next.app.models.simulation.ResultSavingParametersModel
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.services.simulation.ISimulationTaskService
import ru.isma.next.app.services.simulation.SimulationResultService

class TasksViewModelTest {

    private class FakeTaskService : ISimulationTaskService {
        val tasksInternal = mutableListOf<SimulationTask>()
        val eventsInternal = MutableSharedFlow<Unit>(extraBufferCapacity = 64)

        override val tasks: List<SimulationTask>
            get() = tasksInternal.toList()

        override val taskEvents: Flow<Unit>
            get() = eventsInternal

        override fun submit(
            modelName: String,
            lisma: LismaTextModel,
            simulationParameters: SimulationParametersModel,
        ): SimulationTask = throw UnsupportedOperationException()

        override fun cancelTask(task: SimulationTask) {}

        override fun removeTask(task: SimulationTask) {
            tasksInternal.remove(task)
            eventsInternal.tryEmit(Unit)
        }

        override fun close() {}
    }

    private fun testParameters() = SimulationParametersModel(
        cauchyInitials = CauchyInitialsModel(0.0, 10.0, 0.01),
        eventDetectionParameters = EventDetectionParametersModel(false, false, 0.0, 0.0),
        integrationMethodParameters = IntegrationMethodParametersModel("euler", 0.001, false, false, false, false, "localhost", 8080),
        resultSavingParameters = ResultSavingParametersModel(SaveTarget.MEMORY),
    )

    private fun createViewModel(
        fake: FakeTaskService,
        resultService: SimulationResultService = mockk(),
    ) = TasksViewModel(fake, resultService, Dispatchers.Unconfined)

    @Test
    fun `completed task appears in completed list`() {
        val fake = FakeTaskService()
        val task = SimulationTask(1L, "Model", testParameters())
        task.status = SimulationTaskStatus.COMPLETED
        fake.tasksInternal.add(task)

        val viewModel = createViewModel(fake)

        assert(viewModel.completed.size == 1) { "Expected 1 completed task, got ${viewModel.completed.size}" }
        assert(viewModel.inProgress.isEmpty()) { "Expected no in-progress tasks" }
    }

    @Test
    fun `failed task appears in failed list`() {
        val fake = FakeTaskService()
        val task = SimulationTask(1L, "Model", testParameters())
        task.status = SimulationTaskStatus.FAILED
        task.error = "boom"
        fake.tasksInternal.add(task)

        val viewModel = createViewModel(fake)

        assert(viewModel.failed.size == 1) { "Expected 1 failed task, got ${viewModel.failed.size}" }
    }

    @Test
    fun `status change moves task between lists`() {
        val fake = FakeTaskService()
        val task = SimulationTask(1L, "Model", testParameters())
        task.status = SimulationTaskStatus.RUNNING
        fake.tasksInternal.add(task)

        val viewModel = createViewModel(fake)

        assert(viewModel.inProgress.size == 1) { "Expected 1 in-progress task" }

        task.status = SimulationTaskStatus.COMPLETED
        fake.eventsInternal.tryEmit(Unit)

        assert(viewModel.completed.size == 1) { "Expected 1 completed task, got ${viewModel.completed.size}" }
        assert(viewModel.inProgress.isEmpty()) { "Expected no in-progress tasks" }
    }

    @Test
    fun `removeResult removes task from completed list`() {
        val fake = FakeTaskService()
        val task = SimulationTask(1L, "Model", testParameters())
        task.status = SimulationTaskStatus.COMPLETED
        fake.tasksInternal.add(task)

        val resultService = mockk<SimulationResultService>()
        every { resultService.removeResult(task) } answers {
            fake.removeTask(task)
        }

        val viewModel = TasksViewModel(fake, resultService, Dispatchers.Unconfined)

        assert(viewModel.completed.size == 1) { "Expected 1 completed task before removal" }

        viewModel.removeResult(task)

        assert(viewModel.completed.isEmpty()) { "Expected task removed from completed list" }
        assert(viewModel.inProgress.isEmpty()) { "Expected task removed from in-progress list" }
        assert(viewModel.failed.isEmpty()) { "Expected task removed from failed list" }
    }

    @Test
    fun `removeResult removes task from failed list`() {
        val fake = FakeTaskService()
        val task = SimulationTask(1L, "Model", testParameters())
        task.status = SimulationTaskStatus.FAILED
        fake.tasksInternal.add(task)

        val resultService = mockk<SimulationResultService>()
        every { resultService.removeResult(task) } answers {
            fake.removeTask(task)
        }

        val viewModel = TasksViewModel(fake, resultService, Dispatchers.Unconfined)

        assert(viewModel.failed.size == 1) { "Expected 1 failed task before removal" }

        viewModel.removeResult(task)

        assert(viewModel.failed.isEmpty()) { "Expected task removed from failed list" }
    }
}
