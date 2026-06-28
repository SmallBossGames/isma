package ru.isma.next.app.views.toolbars

import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.controlsfx.control.PopOver
import ru.isma.javafx.extensions.coroutines.flow.changeAsFlow
import ru.isma.next.app.extensions.matIconAL
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.services.simulation.SimulationResultService
import ru.isma.next.app.services.simulation.ISimulationTaskService

class TasksPopOver(
    private val simulationTaskService: ISimulationTaskService,
    private val simulationResultService: SimulationResultService,
) : PopOver() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)

    private val inProgressContainer = VBox()
        .apply {
            spacing = 5.0
            padding = Insets(2.0)
        }

    private val completedContainer = VBox()
        .apply {
            spacing = 5.0
            padding = Insets(2.0)
        }

    private val failedContainer = VBox()
        .apply {
            spacing = 5.0
            padding = Insets(2.0)
        }

    private val detailsTextProperty = SimpleStringProperty("")

    private val detailsPopover = PopOver().apply {
        arrowLocation = ArrowLocation.LEFT_BOTTOM
        contentNode = VBox(
            Label("").apply {
                textProperty().bind(detailsTextProperty)
            }
        ).apply {
            padding = Insets(5.0)
        }
    }

    private val itemMap = mutableMapOf<SimulationTask, HBox>()

    init {
        contentNode = VBox(
            Label("In progress"),
            inProgressContainer,
            Separator(),
            Label("Completed"),
            completedContainer,
            Separator(),
            Label("Failed"),
            failedContainer
        ).apply {
            spacing = 5.0
            padding = Insets(10.0)
        }

        bindTasksList()
    }

    private fun bindTasksList() {
        coroutineScope.launch {
            simulationTaskService.tasks.changeAsFlow()
                .cancellable()
                .collect {
                    while (it.next()) {
                        if (it.wasAdded()) {
                            it.addedSubList.forEach { task ->
                                val node = renderTask(task)
                                itemMap[task] = node
                                addToContainer(task, node)
                                observeStatusChanges(task, node)
                            }
                        } else if (it.wasRemoved()) {
                            it.removed.forEach { task ->
                                val node = itemMap[task]
                                itemMap.remove(task)
                                removeFromContainer(task, node)
                            }
                        }
                    }
                }
        }
    }

    private fun renderTask(task: SimulationTask): HBox {
        return when (task.statusValue) {
            SimulationTaskStatus.RUNNING -> createInProgressItem(task)
            SimulationTaskStatus.COMPLETED -> createCompletedItem(task)
            SimulationTaskStatus.FAILED, SimulationTaskStatus.CANCELLED -> createFailedItem(task)
        }
    }

    private fun observeStatusChanges(task: SimulationTask, node: HBox) {
        task.status.addListener { _, _, newStatus ->
            removeFromContainer(task, node)
            val newNode = renderTask(task)
            itemMap[task] = newNode
            addToContainer(task, newNode)
        }
    }

    private fun addToContainer(task: SimulationTask, node: HBox) {
        when (task.statusValue) {
            SimulationTaskStatus.RUNNING -> inProgressContainer.children.add(node)
            SimulationTaskStatus.COMPLETED -> completedContainer.children.add(node)
            SimulationTaskStatus.FAILED, SimulationTaskStatus.CANCELLED -> failedContainer.children.add(node)
        }
    }

    private fun removeFromContainer(task: SimulationTask, node: HBox?) {
        inProgressContainer.children.remove(node)
        completedContainer.children.remove(node)
        failedContainer.children.remove(node)
    }

    fun dispose() {
        coroutineScope.cancel()
    }

    private fun createInProgressItem(task: SimulationTask): HBox {
        return HBox(
            Label("Task #${task.id}"),
            ProgressBar().apply {
                progressProperty().bind(task.progress)
            },
            Button().apply {
                graphic = matIconAL("close")
                tooltip = Tooltip("Abort")
                onAction = EventHandler {
                    simulationTaskService.cancelTask(task)
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }

    private fun createCompletedItem(task: SimulationTask): HBox {
        val result = task.result ?: return HBox()
        return HBox(
            Label("Task #${task.id}"),
            Button("Show").apply {
                onAction = EventHandler {
                    simulationResultService.showChart(task)
                }
            },
            Button("Export").apply {
                onAction = EventHandler {
                    simulationResultService.exportToFile(task)
                }
            },
            Button("Remove").apply {
                onAction = EventHandler {
                    PopOverScope.launch {
                        simulationResultService.removeResult(task)
                    }
                }
            },
            Button().apply {
                tooltip = Tooltip("Details")
                graphic = matIconAL("chevron_right")
                onAction = EventHandler {
                    detailsTextProperty.value = result.toMultilineDetails()
                    detailsPopover.show(this)
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }

    private fun createFailedItem(task: SimulationTask): HBox {
        return HBox(
            Label("Task #${task.id}"),
            Label(task.errorValue ?: "Unknown error").apply {
                style = "-fx-text-fill: red;"
            },
            Button("Remove").apply {
                onAction = EventHandler {
                    PopOverScope.launch {
                        simulationResultService.removeResult(task)
                    }
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }

    companion object {
        val PopOverScope = CoroutineScope(Dispatchers.JavaFx)

        private fun CompletedSimulationModel.toMultilineDetails(): String {
            val builder = StringBuilder()

            builder
                .appendLine("Model")
                .appendLine("Name: $modelName")
                .appendLine()

            builder
                .appendLine("Cauchy Initials")
                .appendLine("Start: ${parameters.cauchyInitials.startTime}")
                .appendLine("End: ${parameters.cauchyInitials.endTime}")
                .appendLine("Initial step: ${parameters.cauchyInitials.initialStep}")
                .appendLine()

            builder
                .appendLine("Integration Method")
                .appendLine("Method: ${parameters.integrationMethodParameters.selectedMethod}")
                .appendLine("Is accurate: ${parameters.integrationMethodParameters.isAccuracyInUse}")

            if (parameters.integrationMethodParameters.isAccuracyInUse) {
                builder.appendLine("Accuracy: ${parameters.integrationMethodParameters.accuracy}")
            }

            builder
                .appendLine("Is stable: ${parameters.integrationMethodParameters.isStableInUse}")
                .appendLine()

            builder
                .appendLine("Statistic")
                .appendLine("Simulation time: ${metricData.simulationTime}ms")
                .appendLine()

            return builder.toString()
        }

    }
}
