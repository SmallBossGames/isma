package ru.isma.next.app.viewmodels

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.FileChooser
import javafx.stage.Window
import ru.isma.next.app.constants.SIMULATION_PARAMETERS_FILE
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.services.simulation.SimulationParametersService

class SimulationParametersViewModel(
    private val parametersService: SimulationParametersService,
) {
    val integrationMethods: ObservableList<String> = FXCollections.observableArrayList(parametersService.methodNames)

    val cauchyInitials = CauchyInitialsViewModel()

    val integrationMethod = IntegrationMethodParametersViewModel()

    val resultSaving = ResultSavingParametersViewModel()

    val eventDetection = EventDetectionParametersViewModel()

    init {
        require(integrationMethods.isNotEmpty()) { "integrationMethods must not be empty" }

        integrationMethod.selectedMethod = integrationMethods.first()

        cauchyInitials.step = 0.1
        require(cauchyInitials.step > 0) { "Step must be > 0, was ${cauchyInitials.step}" }
        cauchyInitials.startTime = 0.0
        cauchyInitials.endTime = 10.0
        require(cauchyInitials.endTime > cauchyInitials.startTime) { "endTime must be > startTime" }

        integrationMethod.accuracy = 0.1
        integrationMethod.server = "localhost"
        integrationMethod.port = 7890

        resultSaving.savingTarget = SaveTarget.MEMORY

        eventDetection.gamma = 0.8
        eventDetection.lowBorder = 0.001
    }

    fun store(ownerWindow: Window? = null) {
        val file = FileChooser().run {
            title = "Save Simulation Parameters"
            extensionFilters.addAll(simulationParametersFileFilters)
            return@run showSaveDialog(ownerWindow)
        } ?: return

        file.writeText(parametersService.serialize(snapshot()))
    }

    fun load(ownerWindow: Window? = null) {
        val file = FileChooser().run {
            title = "Load Simulation Parameters"
            extensionFilters.addAll(simulationParametersFileFilters)
            return@run showOpenDialog(ownerWindow)
        } ?: return

        commit(parametersService.deserialize(file.readText()))
    }

    fun commit(model: SimulationParametersModel) {
        cauchyInitials.commit(model.cauchyInitials)
        eventDetection.commit(model.eventDetectionParameters)
        integrationMethod.commit(model.integrationMethodParameters)
        resultSaving.commit(model.resultSavingParameters)
    }

    fun snapshot() = SimulationParametersModel(
        cauchyInitials.snapshot(),
        eventDetection.snapshot(),
        integrationMethod.snapshot(),
        resultSaving.snapshot()
    )

    companion object {
        private val simulationParametersFileFilters = arrayOf(
            FileChooser.ExtensionFilter("Simulation Parameters File", SIMULATION_PARAMETERS_FILE),
        )
    }
}
