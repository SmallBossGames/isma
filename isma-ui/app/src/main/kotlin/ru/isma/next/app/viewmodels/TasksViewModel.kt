package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.isma.next.app.constants.SIMULATION_RESULTS_FILE
import ru.isma.next.app.models.simulation.NamedPickerItem
import ru.isma.next.app.models.simulation.NamedPickerModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.services.simulation.ISimulationTaskService
import ru.isma.next.app.services.simulation.SimulationResultService

class TaskItemViewModel(val task: SimulationTask) {
    val statusProperty = SimpleObjectProperty<SimulationTaskStatus>(task.status)
    val progressProperty = SimpleDoubleProperty(task.progress)
    val errorProperty = SimpleObjectProperty<String?>(task.error)

    val result get() = task.result

    fun refresh() {
        statusProperty.value = task.status
        progressProperty.value = task.progress
        errorProperty.value = task.error
    }

    fun detailsText(): String {
        val result = task.result ?: return ""

        val builder = StringBuilder()

        builder
            .appendLine("Model")
            .appendLine("Name: ${result.modelName}")
            .appendLine()

        builder
            .appendLine("Cauchy Initials")
            .appendLine("Start: ${result.parameters.cauchyInitials.startTime}")
            .appendLine("End: ${result.parameters.cauchyInitials.endTime}")
            .appendLine("Initial step: ${result.parameters.cauchyInitials.initialStep}")
            .appendLine()

        builder
            .appendLine("Integration Method")
            .appendLine("Method: ${result.parameters.integrationMethodParameters.selectedMethod}")
            .appendLine("Is accurate: ${result.parameters.integrationMethodParameters.isAccuracyInUse}")

        if (result.parameters.integrationMethodParameters.isAccuracyInUse) {
            builder.appendLine("Accuracy: ${result.parameters.integrationMethodParameters.accuracy}")
        }

        builder
            .appendLine("Is stable: ${result.parameters.integrationMethodParameters.isStableInUse}")
            .appendLine()

        builder
            .appendLine("Statistic")
            .appendLine("Simulation time: ${result.metricData.simulationTime}ms")
            .appendLine()

        return builder.toString()
    }
}

class TasksViewModel(
    private val taskService: ISimulationTaskService,
    private val resultService: SimulationResultService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.JavaFx,
) : AutoCloseable {
    val inProgress: ObservableList<TaskItemViewModel> = FXCollections.observableArrayList()
    val completed: ObservableList<TaskItemViewModel> = FXCollections.observableArrayList()
    val failed: ObservableList<TaskItemViewModel> = FXCollections.observableArrayList()

    private val items = mutableMapOf<SimulationTask, TaskItemViewModel>()

    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    init {
        resync()

        scope.launch {
            taskService.taskEvents.collect {
                resync()
            }
        }
    }

    fun cancel(task: SimulationTask) {
        taskService.cancelTask(task)
    }

    fun prepareChartPicker(task: SimulationTask): NamedPickerModel<Int>? {
        val result = task.result ?: return null

        val columnItems = result.cachedColumnNames.mapIndexed { index, header ->
            NamedPickerItem(header, index)
        }

        val timeItem = columnItems.find { it.name == "TIME" } ?: return null

        return NamedPickerModel(timeItem, columnItems)
    }

    fun launchChart(task: SimulationTask, picked: NamedPickerModel<Int>) {
        val result = task.result ?: return

        resultService.launchChart(
            result,
            picked.xAxisItem.name,
            picked.yAxisItems.map { it.name },
        )
    }

    fun exportToFile(task: SimulationTask, ownerWindow: Window? = null) {
        val result = task.result ?: return

        val file = FileChooser().run {
            title = "Export Results"
            extensionFilters.addAll(resultFileFilters)
            return@run showSaveDialog(ownerWindow)
        } ?: return

        resultService.exportToFile(result, file)
    }

    fun removeResult(task: SimulationTask) {
        resultService.removeResult(task)
    }

    private fun resync() {
        val current = taskService.tasks

        val currentIds = current.map { it.id }.toSet()

        items.keys.filter { it.id !in currentIds }.forEach { task ->
            items.remove(task)?.let { removeFromAllLists(it) }
        }

        current.forEach { task ->
            items.getOrPut(task) { TaskItemViewModel(task) }.refresh()
        }

        current.forEach { task ->
            moveToCorrectList(items.getValue(task))
        }
    }

    private fun moveToCorrectList(item: TaskItemViewModel) {
        val target = when (item.task.status) {
            SimulationTaskStatus.RUNNING -> inProgress
            SimulationTaskStatus.COMPLETED -> completed
            SimulationTaskStatus.FAILED, SimulationTaskStatus.CANCELLED -> failed
        }

        if (target.contains(item)) return

        removeFromAllLists(item)
        target.add(item)
    }

    private fun removeFromAllLists(item: TaskItemViewModel) {
        inProgress.remove(item)
        completed.remove(item)
        failed.remove(item)
    }

    override fun close() {
        scope.cancel()
    }

    companion object {
        private val resultFileFilters = arrayOf(
            FileChooser.ExtensionFilter("Comma separate file", SIMULATION_RESULTS_FILE)
        )
    }
}
