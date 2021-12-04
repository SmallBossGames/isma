package ru.isma.next.app.views.toolbars

import javafx.beans.property.SimpleStringProperty
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.controlsfx.control.PopOver
import ru.isma.next.app.extentions.matIconAL
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.InProgressSimulationModel
import ru.isma.next.app.services.simualtion.SimulationResultService
import ru.isma.next.app.services.simualtion.SimulationService

class TasksPopOver(
    private val simulationResultService: SimulationResultService,
    private val simulationService: SimulationService,
): PopOver() {
    private val inProgressTasksContainer = VBox()
        .apply {
            spacing = 5.0
            padding = Insets(2.0)
        }

    private val completedTasksContainer = VBox()
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

    private var inProgressListChangeListener: ListChangeListener<InProgressSimulationModel>? = null

    private var completedListChangeListener: ListChangeListener<CompletedSimulationModel>? = null

    private var inProgressList: ObservableList<InProgressSimulationModel>? = null

    private var completedList: ObservableList<CompletedSimulationModel>? = null

    private val inProgressItemMap = mutableMapOf<InProgressSimulationModel, HBox>()

    private val completedItemMap = mutableMapOf<CompletedSimulationModel, HBox>()

    init {
        contentNode = VBox(
            Label("In progress"),
            inProgressTasksContainer,
            Separator(),
            Label("Completed"),
            completedTasksContainer
        ).apply {
            spacing = 5.0
            padding = Insets(10.0)
        }

        bindInProgressTasksList(simulationService.trackingTasks)
        bindCompletedSimulationModel(simulationResultService.trackingTasksResults)
    }

    fun bindInProgressTasksList(collection: ObservableList<InProgressSimulationModel>) {
        unbindInProgressTasksList()

        val listener = ListChangeListener<InProgressSimulationModel> {
            while (it.next()){
                if(it.wasAdded()) {
                    it.addedSubList.forEach { instance ->
                        val item = createInProgressTasksListItem(instance)

                        inProgressItemMap[instance] = item

                        inProgressTasksContainer.children.add(item)
                    }
                } else if(it.wasRemoved()) {
                    it.removed.forEach { instance ->
                        val item = inProgressItemMap[instance]

                        inProgressItemMap.remove(instance)

                        inProgressTasksContainer.children.remove(item)
                    }
                }
            }
        }

        inProgressList = collection
        inProgressListChangeListener = listener

        collection.addListener(listener)
    }

    fun bindCompletedSimulationModel(collection: ObservableList<CompletedSimulationModel>) {
        unbindCompletedTasksList()

        val listener = ListChangeListener<CompletedSimulationModel> {
            while (it.next()){
                if(it.wasAdded()) {
                    it.addedSubList.forEach { instance ->
                        val item = createCompletedTasksListItem(instance)

                        completedItemMap[instance] = item

                        completedTasksContainer.children.add(item)
                    }
                } else if(it.wasRemoved()) {
                    it.removed.forEach { instance ->
                        val item = completedItemMap[instance]

                        completedItemMap.remove(instance)

                        completedTasksContainer.children.remove(item)
                    }
                }
            }
        }

        completedList = collection
        completedListChangeListener = listener

        collection.addListener(listener)
    }

    fun unbindInProgressTasksList() {
        val listener = inProgressListChangeListener ?: return
        val collection = inProgressList ?: return

        collection.removeListener(listener)
    }

    fun unbindCompletedTasksList() {
        val listener = completedListChangeListener ?: return
        val collection = completedList ?: return

        collection.removeListener(listener)
    }

    private fun createCompletedTasksListItem(trackingTask: CompletedSimulationModel): HBox {
        return HBox(
            Label("Task #${trackingTask.id}"),
            Button("Show").apply {
                onAction = EventHandler {
                    simulationResultService.showChart(trackingTask)
                }
            },
            Button("Export").apply {
                onAction = EventHandler {
                    simulationResultService.exportToFile(trackingTask)
                }
            },
            Button("Remove").apply {
                onAction = EventHandler {
                    PopOverScope.launch {
                        simulationResultService.removeResult(trackingTask)
                    }
                }
            },
            Button().apply {
                tooltip = Tooltip("Details")
                graphic = matIconAL("chevron_right")
                onAction = EventHandler {
                    detailsTextProperty.value = trackingTask.toMultilineDetails()
                    detailsPopover.show(this)
                }
            }
        ).apply {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
        }
    }

    private fun createInProgressTasksListItem(trackingTask: InProgressSimulationModel): HBox {
        return HBox(
            Label("Task #${trackingTask.id}"),
            ProgressBar().apply {
                progressProperty().bind(trackingTask.progressProperty)
            },
            Button().apply {
                graphic = matIconAL("close")
                tooltip = Tooltip("Abort")
                onAction = EventHandler {
                    simulationService.stopSimulation(trackingTask)
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

            if(parameters.integrationMethodParameters.isAccuracyInUse){
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