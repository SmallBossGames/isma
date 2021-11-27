package ru.isma.next.app.services.simualtion

import ru.isma.next.app.constants.SIMULATION_PARAMETERS_FILE
import javafx.stage.FileChooser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.isma.next.app.enumerables.SaveTarget
import ru.isma.next.app.models.simulation.SimulationParametersModel
import ru.isma.next.app.viewmodels.*
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import tornadofx.FileChooserMode
import tornadofx.asObservable
import tornadofx.chooseFile

class SimulationParametersService(library: IntegrationMethodsLibrary) {
    val integrationMethods = library.getIntegrationMethodNames()
            .asObservable()

    val simplifyMethods = listOf("Radial-Distance", "Douglas-Peucker")
            .asObservable()

    val cauchyInitials = CauchyInitialsViewModel()

    val integrationMethod = IntegrationMethodParametersViewModel()

    val resultSaving = ResultSavingParametersViewModel()

    val resultProcessing = ResultProcessingParametersViewModel()

    val eventDetection = EventDetectionParametersViewModel()

    init {
        integrationMethod.selectedMethod = integrationMethods.first()
        resultProcessing.selectedSimplifyMethod = simplifyMethods.first()

        cauchyInitials.step = 0.1
        cauchyInitials.startTime = 0.0
        cauchyInitials.endTime = 10.0

        integrationMethod.accuracy = 0.1
        integrationMethod.server = "localhost"
        integrationMethod.port = 7890

        resultSaving.savingTarget = SaveTarget.MEMORY

        resultProcessing.tolerance = 20.0

        eventDetection.gamma = 0.8
        eventDetection.lowBorder = 0.001
    }

    fun store() {
        val selectedFiles = chooseFile (filters = simulationParametersFileFilters, mode = FileChooserMode.Save)

        if(selectedFiles.isEmpty())
        {
            return
        }

        val file = selectedFiles.first()

        val fileOutput = Json.encodeToString(snapshot())

        file.writeText(fileOutput)
    }

    fun load() {
        val selectedFiles = chooseFile (filters = simulationParametersFileFilters, mode = FileChooserMode.Single)

        if(selectedFiles.isEmpty())
        {
            return
        }

        val file = selectedFiles.first()

        val inputText = file.readText()

        val simulationParameters = Json.decodeFromString<SimulationParametersModel>(inputText)

        commit(simulationParameters)
    }

    fun commit(model: SimulationParametersModel){
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