package ru.isma.next.app.services.simualtion

import javafx.application.Platform
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

import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.isma.next.external.BinaryFilePointProvider
import java.io.File
import java.io.Writer

class SimulationResultService(private val grinIntegrationController: GrinIntegrationFacade) {

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

        val xAxisName = pickedItems.xAxisItem.name
        val yAxisName = pickedItems.yAxisItems.joinToString(", ") { it.name }

        withContext(Dispatchers.JavaFx){
            grinIntegrationController.openSimpleChart(functions, xAxisName, yAxisName)
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

        // Дифференциальные переменные
        for (yForDe in yForDe) {
            builder.append(yForDe).append(COMMA_AND_SPACE)
        }

        // Алгебраические переменные
        for (yForAe in rhs[RHS_AE_PART_IDX]) {
            builder.append(yForAe).append(COMMA_AND_SPACE)
        }

        // Правая часть
        for (f in rhs[RHS_DE_PART_IDX]) {
            builder.append(f).append(COMMA_AND_SPACE)
        }

        // Удаляем последний пробел и запятую и заменяем на перенос строки
        builder.delete(builder.length - 2, builder.length)

        return builder.toString()
    }

    private fun createColumnNamesArray(result: CompletedSimulationModel) : Array<String> {
        val equationIndexProvider = result.equationIndexProvider
        val deCount = equationIndexProvider.getDifferentialEquationCount()
        val aeCount = equationIndexProvider.getAlgebraicEquationCount()
        val rhsDeCount = deCount
        val rhsAeCount = aeCount
        val outputArray = Array(1 + deCount + rhsDeCount + rhsAeCount) { "" }

        outputArray[0] = "TIME"

        for (i in 0 until deCount) {
            outputArray[1 + i] = equationIndexProvider.getDifferentialEquationCode(i) ?: "y$i"
        }

        var offset = 1 + deCount

        for (i in 0 until aeCount) {
            outputArray[offset + i] = equationIndexProvider.getAlgebraicEquationCode(i) ?: "a$i"
        }

        offset = 1 + deCount + aeCount

        for (i in 0 until rhsDeCount) {
            outputArray[offset + i] = "rhs_DE_$i"
        }

        offset = 1 + deCount + aeCount + rhsDeCount

        for (i in 0 until rhsAeCount) {
            outputArray[offset + i] = "rhs_AE_$i"
        }

        return outputArray
    }

     private suspend fun createResultColumns(
        result: CompletedSimulationModel,
        xAxisColumn: Int,
        yAxisColumns: IntArray,
    ) : List<List<PointModel>> {
         val tempResult = List(yAxisColumns.size) { mutableListOf<PointModel>() }
         val eqIdx = result.equationIndexProvider
         val deCount = eqIdx.getDifferentialEquationCount()
         val aeCount = eqIdx.getAlgebraicEquationCount()

         val pointProvider = BinaryFilePointProvider(result.cachedFile, result.cachedColumnNames)

         pointProvider.results.collect { point ->
             val row = DoubleArray(1 + deCount + deCount + aeCount)
             row[0] = point.x
             point.yForDe.copyInto(row, 1)
             point.rhs[RHS_DE_PART_IDX].copyInto(row, 1 + deCount)
             point.rhs[RHS_AE_PART_IDX].copyInto(row, 1 + deCount + deCount)

             yAxisColumns.forEachIndexed { index, colIdx ->
                 tempResult[index].add(PointModel(row[xAxisColumn], row[colIdx]))
             }
         }

         return tempResult
     }

    companion object {
        private const val COMMA_AND_SPACE = ", "
        private const val RHS_DE_PART_IDX = 0
        private const val RHS_AE_PART_IDX = 1

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