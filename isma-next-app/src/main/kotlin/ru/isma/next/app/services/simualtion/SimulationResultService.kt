package ru.isma.next.app.services.simualtion

import javafx.collections.FXCollections
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.javafx.JavaFx
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.models.IntgResultPoint
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import java.io.File
import java.io.Writer
import java.util.*

class SimulationResultService(private val grinIntegrationController: IntegrationController) {
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

        val xAxis = ConcatenationAxis(
            id = UUID.randomUUID(),
            name = "tt",
            order = 0,
            direction = Direction.LEFT,
            backGroundColor = Color.BLACK,
            fontColor = Color.CYAN,
            distanceBetweenMarks = 40.0,
            textSize = 12.0,
            font = "Arial"
        )

        val yAxis = ConcatenationAxis(
            id = UUID.randomUUID(),
            name = "tt",
            order = 0,
            direction = Direction.BOTTOM,
            backGroundColor = Color.BLACK,
            fontColor = Color.CYAN,
            distanceBetweenMarks = 40.0,
            textSize = 12.0,
            font = "Arial"
        )

        val headers = createColumnNamesArray(simulationResult)
        val columns = createResultColumns(simulationResult, headers.size)

        val functions = arrayListOf<ConcatenationFunction>()

        for (i in headers.indices) {
            functions.add(createSimpleConcatenationFunction(headers[i], columns[i].toList()))
        }

        val cartesianSpace = CartesianSpace(
            UUID.randomUUID(),
            "SomeSpace",
            functions,
            xAxis,
            yAxis)

        val spaces = listOf(cartesianSpace)
        grinIntegrationController.integrate(spaces)
    }

    fun exportToFile(simulationResult: CompletedSimulationModel){
        val selectedFiles = chooseFile (filters = fileFilers, mode = FileChooserMode.Save)

        val file = selectedFiles.firstOrNull() ?: return

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

    private fun createResultColumns(result: CompletedSimulationModel, columnsCount: Int) : Array<Array<Point>> = runBlocking {
        val it = result.resultPointProvider.results.toList()
        val rowsCount = it.size
        val tempArray = Array(columnsCount) { Array(rowsCount) { Point.Zero } }
        for (i in it.indices) {
            val x = it[i].x

            for (j in it[i].yForDe.indices) {
                tempArray[j][i] = Point(x, it[i].yForDe[j])
            }

            var offset = it[i].yForDe.size

            for (j in it[i].rhs[DaeSystem.RHS_AE_PART_IDX].indices) {
                tempArray[j + offset][i] = Point(x, it[i].rhs[DaeSystem.RHS_AE_PART_IDX][j])
            }

            offset = it[i].yForDe.size + it[i].rhs[DaeSystem.RHS_AE_PART_IDX].size

            for (j in it[i].rhs[DaeSystem.RHS_DE_PART_IDX].indices) {
                tempArray[j + offset][i] = Point(x, it[i].rhs[DaeSystem.RHS_DE_PART_IDX][j])
            }
        }

        return@runBlocking tempArray
    }

    private fun createSimpleConcatenationFunction(name: String, points: List<Point>) : ConcatenationFunction {
        return ConcatenationFunction(
            id = UUID.randomUUID(),
            name = name,
            points = points,
            isHide = false,
            isSelected = false,
            functionColor = Color.color(Math.random(), Math.random(), Math.random()),
            lineSize = 1.0,
            lineType = LineType.POLYNOM
        )
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