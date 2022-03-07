package ru.isma.next.app.services.simualtion

import javafx.collections.FXCollections
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.views.dialogs.NamedPickerItem
import ru.isma.next.app.views.dialogs.NamedPickerModel
import ru.isma.next.app.views.dialogs.pickAxisVariables
import ru.nstu.grin.integration.FunctionModel
import ru.nstu.grin.integration.GrinIntegrationFacade
import ru.nstu.grin.integration.PointModel
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.models.IntgResultPoint
import java.io.File
import java.io.Writer

class SimulationResultService(private val grinIntegrationController: GrinIntegrationFacade) {

    val trackingTasksResults = FXCollections.observableArrayList<CompletedSimulationModel>()!!

    private val fileFilers = arrayOf(
        FileChooser.ExtensionFilter("Comma separate file", "*.csv")
    )

    suspend fun commitResult(result: CompletedSimulationModel) =
        withContext(Dispatchers.JavaFx) {
            trackingTasksResults.add(result)
        }

    suspend fun removeResult(result: CompletedSimulationModel) =
        withContext(Dispatchers.JavaFx) {
            trackingTasksResults.remove(result)
        }

    fun showChart(simulationResult: CompletedSimulationModel) = ResultServiceScope.launch {
        val headers = createColumnNamesArray(simulationResult)

        val headerColumnPairs = headers.mapIndexed{ i, header ->
            NamedPickerItem(header, i)
        }

        val pickerModel = NamedPickerModel(
            headerColumnPairs.find { it.name == "TIME" }!!,
            headerColumnPairs
        )

        val pickedItems = withContext(Dispatchers.JavaFx){
            pickAxisVariables(pickerModel)
        } ?: return@launch

        val selectedColumnIndices = pickedItems.yAxisItems.map { it.value }.toIntArray()
        val selectedColumns = createResultColumns(simulationResult, pickedItems.xAxisItem.value, selectedColumnIndices)

        val functions = pickedItems.yAxisItems.mapIndexed { i, value ->
            FunctionModel(value.name, selectedColumns[i])
        }

        withContext(Dispatchers.JavaFx){
            grinIntegrationController.openSimpleChart(functions)
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

    suspend fun exportToFileAsync(simulationResult: CompletedSimulationModel, file: File) = coroutineScope {
        launch(Dispatchers.IO) {
            file.bufferedWriter().use { writer ->
                val header = buildHeader(simulationResult)
                writer.write(header)
                writePoints(simulationResult, writer)
            }
        }
    }

    private suspend fun writePoints(result: CompletedSimulationModel, writer: Writer) = coroutineScope {
        result.resultPointProvider.results.collect { value ->
            writer.appendLine(value.toCsvLine())
        }
    }

    private fun IntgResultPoint.toCsvLine() : String {
        val builder = StringBuilder()

        builder.append(x).append(COMMA_AND_SPACE)

        // Дифференциальные переменные
        for (yForDe in yForDe) {
            builder.append(yForDe).append(COMMA_AND_SPACE)
        }

        // Алгебраические переменные
        for (yForAe in rhs[DaeSystem.RHS_AE_PART_IDX]) {
            builder.append(yForAe).append(COMMA_AND_SPACE)
        }

        // Правая часть
        for (f in rhs[DaeSystem.RHS_DE_PART_IDX]) {
            builder.append(f).append(COMMA_AND_SPACE)
        }

        // Удаляем последний пробел и запятую и заменяем на перенос строки
        builder.delete(builder.length - 2, builder.length)

        return builder.toString()
    }

    private fun createColumnNamesArray(result: CompletedSimulationModel) : Array<String> {
        val equationIndexProvider = result.equationIndexProvider
        val deCount = equationIndexProvider.getDifferentialEquationCount()
        val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
        val outputArray = Array(deCount*2 + aeCount) { "" }

        for (i in 0 until deCount) {
            outputArray[i] = equationIndexProvider.getDifferentialEquationCode(i) ?: ""
        }

        var offset = deCount

        for (i in 0 until aeCount) {
            outputArray[i + offset] = equationIndexProvider.getAlgebraicEquationCode(i) ?: ""
        }

        offset = aeCount + deCount

        for (i in 0 until deCount) {
            outputArray[i + offset] = "f${i}"
        }

        return outputArray
    }

     private suspend fun createResultColumns(
        result: CompletedSimulationModel,
        xAxisColumn: Int,
        yAxisColumns: IntArray,
    ) : List<List<PointModel>> {
         val tempResult = List(yAxisColumns.size) { mutableListOf<PointModel>() }

         result.resultPointProvider.results.collect {
             val row = it.yForDe + it.rhs[DaeSystem.RHS_AE_PART_IDX] + it.rhs[DaeSystem.RHS_DE_PART_IDX]

             yAxisColumns.forEachIndexed { index, item ->
                 tempResult[index].add(PointModel(row[xAxisColumn], row[item]))
             }
         }

         return tempResult
     }

    companion object {
        private const val COMMA_AND_SPACE = ", "

        private val ResultServiceScope = CoroutineScope(Dispatchers.Default)

        private fun buildHeader(result: CompletedSimulationModel): String {
            val header = StringBuilder()

            // x
            header.append("x").append(COMMA_AND_SPACE)
            val equationIndexProvider = result.equationIndexProvider

            // Дифференциальные переменные
            val deCount = equationIndexProvider.getDifferentialEquationCount()
            for (i in 0 until deCount) {
                header.append(equationIndexProvider.getDifferentialEquationCode(i)).append(COMMA_AND_SPACE)
            }

            // Алгебраические переменные
            val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
            for (i in 0 until aeCount) {
                header.append(equationIndexProvider.getAlgebraicEquationCode(i)).append(COMMA_AND_SPACE)
            }

            // Правая часть
            for (i in 0 until deCount) {
                header.append("f").append(i.toString()).append(COMMA_AND_SPACE)
            }

            // Удаляем последний пробел и запятую и заменяем на перенос строки
            header.delete(header.length - 2, header.length).appendLine()
            return header.toString()
        }
    }
}