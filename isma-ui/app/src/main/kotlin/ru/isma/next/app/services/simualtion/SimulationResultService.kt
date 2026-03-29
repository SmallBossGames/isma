package ru.isma.next.app.services.simualtion

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.app.launcher.GrinProcessLauncher
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.views.dialogs.NamedPickerItem
import ru.isma.next.app.views.dialogs.NamedPickerModel
import ru.isma.next.app.views.dialogs.pickAxisVariables

import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.isma.next.external.BinaryFilePointProvider
import java.io.File
import java.io.Writer

class SimulationResultService(
    private val grinProcessLauncher: GrinProcessLauncher,
) {

    val trackingTasksResults = FXCollections.observableArrayList<CompletedSimulationModel>()!!

    private val fileFilers = arrayOf(
        FileChooser.ExtensionFilter("Comma separate file", "*.csv")
    )

    fun commitResult(result: CompletedSimulationModel) = Platform.runLater {
        trackingTasksResults.add(result)
    }

    fun removeResult(result: CompletedSimulationModel) = Platform.runLater {
        trackingTasksResults.remove(result)
    }

    fun showChart(simulationResult: CompletedSimulationModel) = ResultServiceScope.launch {
        val headerColumnPairs = simulationResult.cachedColumnNames.mapIndexed { i, header ->
            NamedPickerItem(header, i)
        }

        val pickerModel = NamedPickerModel(
            headerColumnPairs.find { it.name == "TIME" }!!,
            headerColumnPairs
        )

        val pickedItems = withContext(Dispatchers.JavaFx){
            pickAxisVariables(pickerModel)
        } ?: return@launch

        val selectedColumnNames = pickedItems.yAxisItems.map { it.name }
        val xAxisName = pickedItems.xAxisItem.name
        val allChartColumns = listOf(xAxisName) + selectedColumnNames

        withContext(Dispatchers.JavaFx){
            grinProcessLauncher.launch(simulationResult.cachedFile, allChartColumns)
        }
    }

    fun exportToFile(simulationResult: CompletedSimulationModel, ownerWindow: Window? = null){
        val file = FileChooser().run {
            title = "Export Results"
            extensionFilters.addAll(fileFilers)
            return@run showSaveDialog(ownerWindow)
        } ?: return

        ResultServiceScope.launch {
            exportToFileAsync(simulationResult, file)
        }
    }

    suspend fun exportToFileAsync(simulationResult: CompletedSimulationModel, file: File) = withContext(Dispatchers.IO) {
        file.bufferedWriter().use { writer ->
            val header = buildHeader(simulationResult)
            writer.write(header)
            writePoints(simulationResult, writer)
        }
    }

    private suspend fun writePoints(result: CompletedSimulationModel, writer: Writer) = coroutineScope {
        val pointProvider = BinaryFilePointProvider(result.cachedFile, result.cachedColumnNames)

        pointProvider.results.collect { value ->
            writer.appendLine(value.toCsvLine())
        }
    }

    private fun IntgResultPoint.toCsvLine() : String {
        val builder = StringBuilder()

        builder.append(x).append(COMMA_AND_SPACE)

        for (yForDe in yForDe) {
            builder.append(yForDe).append(COMMA_AND_SPACE)
        }

        for (yForAe in rhs[RHS_AE_PART_IDX]) {
            builder.append(yForAe).append(COMMA_AND_SPACE)
        }

        for (f in rhs[RHS_DE_PART_IDX]) {
            builder.append(f).append(COMMA_AND_SPACE)
        }

        builder.delete(builder.length - 2, builder.length)

        return builder.toString()
    }

    companion object {
        private const val COMMA_AND_SPACE = ", "
        private const val RHS_DE_PART_IDX = 0
        private const val RHS_AE_PART_IDX = 1

        private val ResultServiceScope = CoroutineScope(Dispatchers.Default)

        private fun buildHeader(result: CompletedSimulationModel): String {
            val header = StringBuilder()

            header.append("x").append(COMMA_AND_SPACE)
            val equationIndexProvider = result.equationIndexProvider

            val deCount = equationIndexProvider.getDifferentialEquationCount()
            for (i in 0 until deCount) {
                header.append(equationIndexProvider.getDifferentialEquationCode(i)).append(COMMA_AND_SPACE)
            }

            val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
            for (i in 0 until aeCount) {
                header.append(equationIndexProvider.getAlgebraicEquationCode(i)).append(COMMA_AND_SPACE)
            }

            for (i in 0 until deCount) {
                header.append("f").append(i.toString()).append(COMMA_AND_SPACE)
            }

            header.delete(header.length - 2, header.length).appendLine()
            return header.toString()
        }
    }
}