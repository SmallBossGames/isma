package ru.isma.next.app.services.simualtion

import javafx.collections.FXCollections
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.views.dialogs.NamedPickerItem
import ru.isma.next.app.views.dialogs.pickItems
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

    fun showChart(simulationResult: CompletedSimulationModel) {
        val headers = createColumnNamesArray(simulationResult)

        val headerColumnPairs = headers.mapIndexed{i, header -> NamedPickerItem(header, i) }

        val pickedItems = pickItems(headerColumnPairs)

        if(pickedItems.isEmpty()){
            return
        }

        val selectedColumnIndices = pickedItems.map { it.value }.toIntArray()
        val selectedColumns = createResultColumns(simulationResult, selectedColumnIndices)

        val functions = pickedItems.mapIndexed { i, value ->
            FunctionModel(value.name, selectedColumns[i])
        }

        grinIntegrationController.openSimpleChart(functions)
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

    private fun createResultColumns(
        result: CompletedSimulationModel,
        orderedColumnNumbers: IntArray,
    ) : List<List<PointModel>> = runBlocking {
        val tempResult = List(orderedColumnNumbers.size) { mutableListOf<PointModel>() }

        var indicesInitialized = false
        var regularIndices = intArrayOf()
        var aeIndices = intArrayOf()
        var deIndices = intArrayOf()

        result.resultPointProvider.results.collect {
            if(!indicesInitialized) {
                val minRegularIndex = 0
                val minAeIndex = it.yForDe.size
                val minDeIndex = it.yForDe.size + it.rhs[DaeSystem.RHS_AE_PART_IDX].size
                val fullLength = it.yForDe.size + it.rhs[DaeSystem.RHS_AE_PART_IDX].size + it.rhs[DaeSystem.RHS_DE_PART_IDX].size

                regularIndices = orderedColumnNumbers
                    .filter { x -> x in minRegularIndex until minAeIndex }
                    .map { x -> x - minRegularIndex }
                    .toIntArray()

                aeIndices = orderedColumnNumbers
                    .filter { x -> x in minAeIndex until minDeIndex }
                    .map { x -> x - minAeIndex }
                    .toIntArray()

                deIndices = orderedColumnNumbers
                    .filter { x -> x in minDeIndex until fullLength }
                    .map { x -> x - minDeIndex }
                    .toIntArray()

                indicesInitialized = true
            }

            var resultArrayColumn = 0

            for (j in regularIndices) {
                tempResult[resultArrayColumn].add(PointModel(it.x, it.yForDe[j]))
                resultArrayColumn++
            }

            for (j in aeIndices) {
                tempResult[resultArrayColumn].add(PointModel(it.x, it.rhs[DaeSystem.RHS_AE_PART_IDX][j]))
                resultArrayColumn++
            }

            for (j in deIndices) {
                tempResult[resultArrayColumn].add(PointModel(it.x, it.rhs[DaeSystem.RHS_AE_PART_IDX][j]))
                resultArrayColumn++
            }
        }

        return@runBlocking tempResult
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