package ru.isma.next.app.views.toolbars

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.control.Separator
import javafx.scene.control.Tooltip
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.controlsfx.control.PopOver
import ru.isma.next.app.extensions.matIconAL
import ru.isma.next.app.models.simulation.SimulationTaskStatus
import ru.isma.next.app.viewmodels.TaskItemViewModel
import ru.isma.next.app.viewmodels.TasksViewModel
import ru.isma.next.app.views.dialogs.pickAxisVariables

class TasksPopOver(
    private val viewModel: TasksViewModel,
) : PopOver() {
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

    private val detailsTextProperty = javafx.beans.property.SimpleStringProperty("")

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

    private val itemNodes = mutableMapOf<TaskItemViewModel, HBox>()

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

        observeList(viewModel.inProgress, inProgressContainer)
        observeList(viewModel.completed, completedContainer)
        observeList(viewModel.failed, failedContainer)
    }

    private fun observeList(list: ObservableList<TaskItemViewModel>, container: VBox) {
        list.addListener(ListChangeListener { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    change.addedSubList.forEach { item ->
                        val node = renderItem(item)
                        itemNodes[item] = node
                        container.children.add(node)
                    }
                }
                if (change.wasRemoved()) {
                    change.removed.forEach { item ->
                        val node = itemNodes.remove(item)
                        if (node != null) {
                            container.children.remove(node)
                        }
                    }
                }
            }
        })
    }

    private fun renderItem(item: TaskItemViewModel): HBox = when (item.task.status) {
        SimulationTaskStatus.RUNNING -> createInProgressItem(item)
        SimulationTaskStatus.COMPLETED -> createCompletedItem(item)
        SimulationTaskStatus.FAILED, SimulationTaskStatus.CANCELLED -> createFailedItem(item)
    }

    private fun createInProgressItem(item: TaskItemViewModel): HBox {
        return HBox(
            Label("Task #${item.task.id}"),
            ProgressBar().apply {
                progressProperty().bind(item.progressProperty)
            },
            Button().apply {
                graphic = matIconAL("close")
                tooltip = Tooltip("Abort")
                onAction = EventHandler {
                    viewModel.cancel(item.task)
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }

    private fun createCompletedItem(item: TaskItemViewModel): HBox {
        return HBox(
            Label("Task #${item.task.id}"),
            Button("Show").apply {
                onAction = EventHandler {
                    val picked = viewModel.prepareChartPicker(item.task) ?: return@EventHandler
                    val model = pickAxisVariables(picked) ?: return@EventHandler
                    viewModel.launchChart(item.task, model)
                }
            },
            Button("Export").apply {
                onAction = EventHandler {
                    viewModel.exportToFile(item.task, scene?.window)
                }
            },
            Button("Remove").apply {
                onAction = EventHandler {
                    viewModel.removeResult(item.task)
                }
            },
            Button().apply {
                tooltip = Tooltip("Details")
                graphic = matIconAL("chevron_right")
                onAction = EventHandler {
                    detailsTextProperty.value = item.detailsText()
                    detailsPopover.show(this)
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }

    private fun createFailedItem(item: TaskItemViewModel): HBox {
        return HBox(
            Label("Task #${item.task.id}"),
            Label(item.task.error ?: "Unknown error").apply {
                style = "-fx-text-fill: red;"
            },
            Button("Remove").apply {
                onAction = EventHandler {
                    viewModel.removeResult(item.task)
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }
}
